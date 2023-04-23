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
    val variableName: Pair<String, Int>,
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
    val variableName: Pair<String, Int>,
    val variableType: Pair<Type, Int>,
    val isMutable: Boolean,
) : VisitableAST {
    override fun accept(visitor: Visitor) = visitor.visitDeclarationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is DeclarationAST && variableName == other.variableName && variableType == other.variableType && isMutable == other.isMutable
    }
}

sealed interface ExpressionAST : VisitableAST
sealed interface ConditionAST : VisitableAST

class BinaryOperationAST(
    val left: VisitableAST,
    val right: VisitableAST,
    val operation: Pair<Operation, Int>,
) : ExpressionAST {
    override fun accept(visitor: Visitor) = visitor.visitBinaryOperationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is BinaryOperationAST && left == other.left && right == other.right && operation == other.operation
    }
}

class UnaryOperationAST(
    val function: Pair<Function, Int>,
    val args: ExpressionAST,
) : ExpressionAST {
    override fun accept(visitor: Visitor) = visitor.visitUnaryOperationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is UnaryOperationAST && function == other.function && args == other.args
    }
}

class LiteralAST(
    val value: Pair<Value, Int>,
) : ExpressionAST, ConditionAST {
    override fun accept(visitor: Visitor) = visitor.visitLiteralAST(this)
    override fun equals(other: Any?): Boolean {
        return other is LiteralAST && value == other.value
    }
}

class VariableAST(
    val variableName: Pair<String, Int>,
) : ExpressionAST, ConditionAST {
    override fun accept(visitor: Visitor) = visitor.visitVariableAST(this)
    override fun equals(other: Any?): Boolean {
        return other is VariableAST && variableName == other.variableName
    }
}

class InputAST(
    val input: String,
) : VisitableAST {
    override fun accept(visitor: Visitor) = visitor.visitInputAST(this)
    override fun equals(other: Any?): Boolean {
        return other is InputAST && input == other.input
    }
}

class EmptyAST : ExpressionAST {
    override fun accept(visitor: Visitor) = visitor.visitEmptyAST(this)
    override fun equals(other: Any?): Boolean {
        return other is EmptyAST
    }
}

class IfAST(
    val condition: ConditionAST,
    val truthBlock: List<VisitableAST>,
) : VisitableAST {
    override fun accept(visitor: Visitor): VisitableAST = visitor.visitIfAST(this)
    override fun equals(other: Any?): Boolean {
        return other is IfAST && other.condition == condition && other.truthBlock == truthBlock
    }
}

class IfElseAST(
    val condition: ConditionAST,
    val truthBlock: List<VisitableAST>,
    val falseBlock: List<VisitableAST>,
) : VisitableAST {
    override fun accept(visitor: Visitor): VisitableAST = visitor.visitIfElseAST(this)
    override fun equals(other: Any?): Boolean {
        return other is IfElseAST && other.condition == condition && other.truthBlock == truthBlock && other.falseBlock == falseBlock
    }
}
