package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports

class InterpreterVisitor(
    val memory: LocalMemory,
    private val printFunction: PrintFunction,
    private val readInputFunction: ReadInputFunction
) : Visitor {

    private fun getCopy(): InterpreterVisitor {
        return InterpreterVisitor(memory.getNewChildMemory(), printFunction, readInputFunction)
    }

    override fun visitAssignationAST(ast: AssignationAST): EmptyAST {
        val newValueAst = ast.expression.accept(this)
        val declarationAST = ast.declaration.accept(this)
        if (declarationAST is DeclarationAST && newValueAst is LiteralAST) {
            if (memory.keyIsUsed(declarationAST.variableName)) throw Error("Variable already declared")
            if (isSameType(declarationAST.variableType, newValueAst.value)) {
                memory.put(declarationAST.variableName, newValueAst.value, declarationAST.isMutable)
            } else {
                throw Error("Invalid type assignation")
            }
        } else if (declarationAST is DeclarationAST && newValueAst is InputAST) {
            if (memory.keyIsUsed(declarationAST.variableName)) throw Error("Variable already declared")
            when (declarationAST.variableType) {
                is BOOL -> {
                    val boolInput = newValueAst.input.toBooleanStrictOrNull() ?: throw java.lang.Error("Value provided is not a boolean")
                    memory.put(declarationAST.variableName, BoolValue(boolInput), declarationAST.isMutable)
                }
                is NUM -> {
                    val numInput = newValueAst.input.toDoubleOrNull() ?: throw java.lang.Error("Value provided is not a number")
                    memory.put(declarationAST.variableName, NumValue(numInput), declarationAST.isMutable)
                }
                is STR -> {
                    memory.put(declarationAST.variableName, StrValue(newValueAst.input), declarationAST.isMutable)
                }
            }
        } else {
            throw Error("Invalid tree")
        }
        return EmptyAST()
    }

    override fun visitReAssignationAST(ast: ReAssignationAST): VisitableAST {
        val newValueAst = ast.expression.accept(this)
        val oldValue = memory.getValue(ast.variableName)
        when(newValueAst) {
            is LiteralAST -> {
                if (oldValue::class == newValueAst.value::class) {
                    memory.replaceVariable(ast.variableName, newValueAst.value)
                }
            }
            is InputAST -> {
                when (oldValue) {
                    is BoolValue -> {
                        val boolInput = newValueAst.input.toBooleanStrictOrNull() ?: throw java.lang.Error("Value provided is not a boolean")
                        memory.replaceVariable(ast.variableName, BoolValue(boolInput))
                    }
                    is NumValue -> {
                        val numInput = newValueAst.input.toDoubleOrNull() ?: throw java.lang.Error("Value provided is not a number")
                        memory.replaceVariable(ast.variableName, NumValue(numInput))
                    }
                    is StrValue -> {
                        memory.replaceVariable(ast.variableName, StrValue(newValueAst.input))
                    }
                }
            }
            else -> throw Error("Invalid variable assignment")
        }
        return EmptyAST()
    }

    private fun isSameType(declarationType: Type, value: Value): Boolean {
        return if (declarationType is NUM && value is NumValue) {
            true
        } else if (declarationType is STR && value is StrValue) {
            true
        } else {
            declarationType is BOOL && value is BoolValue
        }
    }

    override fun visitDeclarationAST(ast: DeclarationAST): DeclarationAST {
        return ast
    }

    override fun visitBinaryOperationAST(ast: BinaryOperationAST): LiteralAST {
        val leftResult = ast.left.accept(this)
        val rightResult = ast.right.accept(this)
        if (leftResult is LiteralAST && rightResult is LiteralAST) {
            return when (ast.operation) {
                is ADD -> LiteralAST(sumValues(leftResult.value, rightResult.value))
                is SUB -> LiteralAST(subtractValues(leftResult.value, rightResult.value))
                is DIV -> LiteralAST(divideValues(leftResult.value, rightResult.value))
                is MUL -> LiteralAST(multiplyValues(leftResult.value, rightResult.value))
                else -> throw Error("Invalid operation")
            }
        }
        throw Error("Invalid expression")
    }

    private fun sumValues(left: Value, right: Value): Value {
        return when {
            left is NumValue && right is NumValue -> NumValue(left.value + right.value)
            left is StrValue && right is NumValue -> StrValue(left.value + right.value)
            left is NumValue && right is StrValue -> StrValue(left.value.toString() + right.value)
            left is StrValue && right is StrValue -> StrValue(left.value + right.value)
            else -> throw Error("Can not sum values")
        }
    }

    private fun subtractValues(left: Value, right: Value): Value {
        return when {
            left is NumValue && right is NumValue -> NumValue(left.value - right.value)
            else -> throw Error("Can not subtract values")
        }
    }

    private fun divideValues(left: Value, right: Value): Value {
        return when {
            left is NumValue && right is NumValue -> NumValue(left.value / right.value)
            else -> throw Error("Can not divide values")
        }
    }

    private fun multiplyValues(left: Value, right: Value): Value {
        return when {
            left is NumValue && right is NumValue -> NumValue(left.value * right.value)
            else -> throw Error("Can not multiply values")
        }
    }

    override fun visitUnaryOperationAST(ast: UnaryOperationAST): VisitableAST {
        return when (ast.function) {
            is PRINT -> printFunctionImpl(ast.args.accept(this))
            is READINPUT -> readInputFunctionImpl(ast.args.accept(this))
        }
    }

    private fun printFunctionImpl(ast: VisitableAST): EmptyAST {
        return when (ast) {
            is LiteralAST -> {
                printFunction.print(ast.value)
                EmptyAST()
            }
            else -> throw Error("Can not print value")
        }
    }

    private fun readInputFunctionImpl(ast: VisitableAST): InputAST {
        return when (ast) {
            is LiteralAST -> {
                if(ast.value !is StrValue) throw Error("Read input message should be a string")
                val value = readInputFunction.read((ast.value as StrValue).value)
                InputAST(value)
            }
            else -> throw Error("Invalid message for read input")
        }
    }

    override fun visitLiteralAST(ast: LiteralAST): LiteralAST {
        return ast
    }

    override fun visitVariableAST(ast: VariableAST): LiteralAST {
        return LiteralAST(memory.getValue(ast.variableName))
    }

    override fun visitEmptyAST(ast: EmptyAST): EmptyAST {
        return ast
    }

    override fun visitIfAST(ast: IfAST): VisitableAST {
        val value = when (val conditionAst = ast.condition) {
            is LiteralAST -> conditionAst.value
            is VariableAST -> this.memory.getValue(conditionAst.variableName)
        }
        if (value is BoolValue) {
            if (value.value) {
                ast.truthBlock.forEach { it.accept(getCopy()) }
            }
            return EmptyAST()
        } else {
            throw Error("Invalid if statement condition")
        }
    }

    override fun visitIfElseAST(ast: IfElseAST): VisitableAST {
        val value = when (val conditionAst = ast.condition) {
            is LiteralAST -> conditionAst.value
            is VariableAST -> this.memory.getValue(conditionAst.variableName)
        }
        if (value is BoolValue) {
            if (value.value) {
                ast.truthBlock.forEach { it.accept(getCopy()) }
            } else {
                ast.truthBlock.forEach { it.accept(getCopy()) }
            }
            return EmptyAST()
        } else {
            throw Error("Invalid if statement condition")
        }
    }

    override fun visitInputAST(ast: InputAST): VisitableAST {
        TODO("Not yet implemented")
    }

}
