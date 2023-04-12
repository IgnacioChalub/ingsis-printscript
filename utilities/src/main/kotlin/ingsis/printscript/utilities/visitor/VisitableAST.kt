package ingsis.printscript.utilities.visitor

import ingsis.printscript.utilities.enums.Function
import ingsis.printscript.utilities.enums.Operation
import ingsis.printscript.utilities.enums.Type
import ingsis.printscript.utilities.enums.Value

sealed interface VisitableAST {
    fun accept(visitor: Visitor): VisitableAST
    override fun equals(other: Any?): Boolean
}

class ReAssignationAST(
    val variableName: String,
    val expression: ExpressionAST,
) : VisitableAST {
    override fun accept(visitor: Visitor) = visitor.visitReAssignationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is ReAssignationAST && variableName == other.variableName && expression == other.expression
    }
}

class AssignationAST(
    val declaration: DeclarationAST,
    val expression: ExpressionAST,
) : VisitableAST {
    override fun accept(visitor: Visitor) = visitor.visitAssignationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is AssignationAST && declaration == other.declaration && expression == other.expression
    }
}

class DeclarationAST(
    val variableName: String,
    val variableType: Type,
) : VisitableAST {
    override fun accept(visitor: Visitor) = visitor.visitDeclarationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is DeclarationAST && variableName == other.variableName && variableType == other.variableType
    }
}

sealed interface ExpressionAST : VisitableAST

class BinaryOperationAST(
    val left: VisitableAST,
    val right: VisitableAST,
    val operation: Operation,
) : ExpressionAST {
    override fun accept(visitor: Visitor) = visitor.visitBinaryOperationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is BinaryOperationAST && left == other.left && right == other.right && operation == other.operation
    }
}

class UnaryOperationAST(
    val function: Function,
    val args: VisitableAST,
) : VisitableAST {
    override fun accept(visitor: Visitor) = visitor.visitUnaryOperationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is UnaryOperationAST && function == other.function && args == other.args
    }
}

class LiteralAST(
    val value: Value,
) : ExpressionAST {
    override fun accept(visitor: Visitor) = visitor.visitLiteralAST(this)
    override fun equals(other: Any?): Boolean {
        return other is LiteralAST && value == other.value
    }
}

class VariableAST(
    val variableName: String,
) : ExpressionAST {
    override fun accept(visitor: Visitor) = visitor.visitVariableAST(this)
    override fun equals(other: Any?): Boolean {
        return other is VariableAST && variableName == other.variableName
    }
}

class EmptyAST() : ExpressionAST {
    override fun accept(visitor: Visitor) = visitor.visitEmptyAST(this)
    override fun equals(other: Any?): Boolean {
        return other is EmptyAST
    }
}
