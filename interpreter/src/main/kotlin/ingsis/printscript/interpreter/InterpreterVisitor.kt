package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports

class InterpreterVisitor(
    val memory: LocalMemory,
    private val printFunction: PrintFunction,
) : Visitor {

    private fun getCopy(): InterpreterVisitor {
        return InterpreterVisitor(memory.getNewChildMemory(), printFunction)
    }

    override fun visitAssignationAST(ast: AssignationAST): EmptyAST {
        val literalAST = ast.expression.accept(this)
        val declarationAST = ast.declaration.accept(this)
        if (declarationAST is DeclarationAST && literalAST is LiteralAST) {
            if(memory.keyIsUsed(declarationAST.variableName)) throw Error("Variable already declared")
            if (isSameType(declarationAST.variableType, literalAST.value)) {
                memory.put(declarationAST.variableName, literalAST.value, declarationAST.isMutable)
            } else {
                throw Error("Invalid type assignation")
            }
        } else {
            throw Error("Invalid tree")
        }
        return EmptyAST()
    }

    override fun visitReAssignationAST(ast: ReAssignationAST): VisitableAST {
        val literalAST = ast.expression.accept(this)
        val oldValue = memory.getValue(ast.variableName)
        if (literalAST is LiteralAST) {
            if (oldValue::class == literalAST.value::class) {
                memory.replaceVariable(ast.variableName, literalAST.value)
            }
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
            else -> throw Exception("Todo")
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
}
