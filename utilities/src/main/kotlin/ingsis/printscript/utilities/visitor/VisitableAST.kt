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

    override fun toString(): String {
        return "$variableName = $expression"
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

    override fun toString(): String {
        return "$declaration = $expression;"
    }
}

class DeclarationAST(
    val variableName: String,
    val variableType: Type,
    val isMutable: Boolean,
) : VisitableAST {
    override fun accept(visitor: Visitor) = visitor.visitDeclarationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is DeclarationAST && variableName == other.variableName && variableType == other.variableType && isMutable == other.isMutable
    }

    override fun toString(): String {
        val mutability = if (isMutable) "var" else "val"
        return "$variableName: $variableType = $mutability"
    }
}

sealed interface ExpressionAST : VisitableAST
sealed interface ConditionAST : VisitableAST

class BinaryOperationAST(
    val left: VisitableAST,
    val right: VisitableAST,
    val operation: Operation,
) : ExpressionAST {
    override fun accept(visitor: Visitor) = visitor.visitBinaryOperationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is BinaryOperationAST && left == other.left && right == other.right && operation == other.operation
    }

    override fun toString(): String {
        return "($left $operation $right)"
    }
}

class UnaryOperationAST(
    val function: Function,
    val args: ExpressionAST,
) : ExpressionAST {
    override fun accept(visitor: Visitor) = visitor.visitUnaryOperationAST(this)
    override fun equals(other: Any?): Boolean {
        return other is UnaryOperationAST && function == other.function && args == other.args
    }

    override fun toString(): String {
        return "$function($args)"
    }
}

class LiteralAST(
    val value: Value,
) : ExpressionAST, ConditionAST {
    override fun accept(visitor: Visitor) = visitor.visitLiteralAST(this)
    override fun equals(other: Any?): Boolean {
        return other is LiteralAST && value == other.value
    }

    override fun toString(): String {
        return value.toString()
    }
}

class VariableAST(
    val variableName: String,
) : ExpressionAST, ConditionAST {
    override fun accept(visitor: Visitor) = visitor.visitVariableAST(this)
    override fun equals(other: Any?): Boolean {
        return other is VariableAST && variableName == other.variableName
    }

    override fun toString(): String {
        return variableName
    }
}

class InputAST(
    val input: String,
) : VisitableAST {
    override fun accept(visitor: Visitor) = visitor.visitInputAST(this)
    override fun equals(other: Any?): Boolean {
        return other is InputAST && input == other.input
    }

    override fun toString(): String {
        return input
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

    override fun toString(): String {
        return "if ($condition) { ${truthBlock.joinToString("; ")} }"
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

    override fun toString(): String {
        return "if ($condition) { ${truthBlock.joinToString("; ")} } else { ${falseBlock.joinToString("; ")} }"
    }
}
