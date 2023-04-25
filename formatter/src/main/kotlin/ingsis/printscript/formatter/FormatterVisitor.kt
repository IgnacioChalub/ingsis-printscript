package ingsis.printscript.formatter

import ingsis.printscript.utilities.enums.EmptyValue
import ingsis.printscript.utilities.visitor.*

class FormatterVisitor(private val indentationSize: Int = 4) : Visitor {
    private val indentation: String = " ".repeat(indentationSize)
    private val output = StringBuilder()

    override fun toString(): String {
        return output.toString()
    }

    private fun appendWithIndentation(text: String, indentLevel: Int) {
        output.append(indentation.repeat(indentLevel)).append(text)
    }

    private fun appendWithoutIndentation(text: String) {
        output.append(text)
    }

    override fun visitAssignationAST(ast: AssignationAST): VisitableAST {
        appendWithIndentation("", 1)
        ast.declaration.accept(this)
        appendWithoutIndentation(" = ")
        ast.expression.accept(this)
        appendWithoutIndentation(";")
        output.append("\n") // Add this line
        return EmptyAST()
    }

    override fun visitReAssignationAST(ast: ReAssignationAST): VisitableAST {
        appendWithIndentation("", 1)
        appendWithoutIndentation("${ast.variableName} = ")
        ast.expression.accept(this)
        appendWithoutIndentation(";")
        output.append("\n") // Add this line
        return EmptyAST()
    }

    override fun visitDeclarationAST(ast: DeclarationAST): DeclarationAST {
        appendWithoutIndentation(if (ast.isMutable) "var " else "val ")
        appendWithoutIndentation("${ast.variableName}: ${ast.variableType}")
        return ast
    }

    override fun visitIfAST(ast: IfAST): VisitableAST {
        appendWithIndentation("if (", 1)
        ast.condition.accept(this)
        appendWithoutIndentation(") {")
        ast.truthBlock.forEach { it.accept(this) }
        appendWithIndentation("}", 0) // Change the indentLevel from 1 to 0
        return EmptyAST()
    }

    override fun visitIfElseAST(ast: IfElseAST): VisitableAST {
        appendWithIndentation("if (", 1)
        ast.condition.accept(this)
        appendWithoutIndentation(") {")
        ast.truthBlock.forEach { it.accept(this) }
        appendWithIndentation("} else {", 0) // Change the indentLevel from 1 to 0
        ast.falseBlock.forEach { it.accept(this) }
        appendWithIndentation("}", 0) // Change the indentLevel from 1 to 0
        return EmptyAST()
    }

    override fun visitBinaryOperationAST(ast: BinaryOperationAST): LiteralAST {
        ast.left.accept(this)
        appendWithoutIndentation(" ${ast.operation} ")
        ast.right.accept(this)
        return LiteralAST(EmptyValue())
    }

    override fun visitUnaryOperationAST(ast: UnaryOperationAST): VisitableAST {
        appendWithoutIndentation("${ast.function}(")
        ast.args.accept(this)
        appendWithoutIndentation(")")
        return EmptyAST()
    }

    override fun visitLiteralAST(ast: LiteralAST): LiteralAST {
        appendWithoutIndentation(ast.value.toString())
        return ast
    }

    override fun visitVariableAST(ast: VariableAST): LiteralAST {
        appendWithoutIndentation(ast.variableName)
        return LiteralAST(EmptyValue())
    }

    override fun visitEmptyAST(ast: EmptyAST): EmptyAST {
        return ast
    }

    override fun visitInputAST(ast: InputAST): VisitableAST {
        appendWithoutIndentation(ast.input)
        return ast
    }
}

//    private var currentIndentation = 0
//
//    private fun indent(): String {
//        return " ".repeat(currentIndentation)
//    }
//
//    fun formatCode(ast: VisitableAST): String {
//        return ast.accept(this).toString()
//    }
//
//
//    override fun visitAssignationAST(ast: AssignationAST): VisitableAST {
//        val declaration = ast.declaration.accept(this) as DeclarationAST
//        val expression = ast.expression.accept(this) as ExpressionAST
//        val mutability = if (declaration.isMutable) "var" else "val"
//        val formattedString =
//            "${indent()}${declaration.variableName}: ${declaration.variableType} = $mutability ${expression};"
//        println(formattedString)
//
//        return AssignationAST(declaration, expression)
//    }
//
//
//    override fun visitReAssignationAST(ast: ReAssignationAST): VisitableAST {
//        val expression = ast.expression.accept(this) as ExpressionAST
//        val formattedString = "${indent()}${ast.variableName} = ${expression};"
//        println(formattedString)
//        return ast
//    }
//
//    override fun visitDeclarationAST(ast: DeclarationAST): DeclarationAST {
//        val mutability = if (ast.isMutable) "var" else "val"
//        val formattedString = "${indent()}${ast.variableName}: ${ast.variableType} = $mutability"
//        println(formattedString)
//        return ast
//    }
//
//    override fun visitBinaryOperationAST(ast: BinaryOperationAST): ExpressionAST {
//        val left = ast.left.accept(this) as ExpressionAST
//        val right = ast.right.accept(this) as ExpressionAST
//        val formattedString = "(${left} ${ast.operation} ${right})"
//        println(formattedString)
//        return BinaryOperationAST(left, right, ast.operation)
//    }
//
//    override fun visitUnaryOperationAST(ast: UnaryOperationAST): ExpressionAST {
//        val args = ast.args.accept(this) as ExpressionAST
//        val formattedString = "${indent()}${ast.function}(${args})"
//        println(formattedString)
//        return UnaryOperationAST(ast.function, args)
//    }
//
//    override fun visitLiteralAST(ast: LiteralAST): LiteralAST {
//        val formattedString = "${indent()}${ast.value}"
//        println(formattedString)
//        return ast
//    }
//
//    override fun visitVariableAST(ast: VariableAST): VariableAST {
//        val formattedString = "${indent()}${ast.variableName}"
//        println(formattedString)
//        return ast
//    }
//
//
//    override fun visitEmptyAST(ast: EmptyAST): EmptyAST {
//        return ast
//    }
//
//    override fun visitIfAST(ast: IfAST): VisitableAST {
//        val condition = ast.condition.accept(this) as ConditionAST
//        val truthBlock = ast.truthBlock.map {
//            currentIndentation += indentLevel
//            val result = it.accept(this)
//            currentIndentation -= indentLevel
//            result.toString()
//        }.joinToString("\n")
//        println("${indent()}if (${condition}) {\n$truthBlock\n${indent()}}")
//        return ast
//    }
//
//    override fun visitIfElseAST(ast: IfElseAST): VisitableAST {
//        val condition = ast.condition.accept(this) as ConditionAST
//        println("${indent()}if (${condition}) {")
//        currentIndentation += indentLevel
//        ast.truthBlock.forEach { it.accept(this) }
//        currentIndentation -= indentLevel
//        println("${indent()}} else {")
//        currentIndentation += indentLevel
//        ast.falseBlock.forEach { it.accept(this) }
//        currentIndentation -= indentLevel
//        println("${indent()}}")
//        return ast
//    }
//
//    override fun visitInputAST(ast: InputAST): VisitableAST {
//        return ast
//    }