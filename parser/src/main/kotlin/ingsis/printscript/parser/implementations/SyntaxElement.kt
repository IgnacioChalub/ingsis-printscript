package ingsis.printscript.parser.implementations

import ingsis.printscript.parser.interfaces.IStatement
import ingsis.printscript.parser.interfaces.SyntaxElement
import ingsis.printscript.utilities.enums.*

enum class SyntaxElements(override val types: List<Token>) : SyntaxElement {

    TYPE(listOf(STR, NUM)),

    VARIABLE(listOf(LET)),

    OPERATION(listOf(ADD, SUB, DIV, MUL)),

    IDENTIFIER_SYNTAX(listOf(IDENTIFIER("any"))),

    ASSIGNATION_SYNTAX(listOf(ASSIGNATION)),

    TYPEASSIGNMENT(listOf(COLON)),

    VOIDFUNCTION(listOf(PRINT)),

    LITERAL(listOf(STR, NUM)),

    END(listOf(SEMICOLON)),
}

enum class Statement(override val elements: List<SyntaxElement>) : IStatement {
    // LET id: String = "name"
    ASSIGNATION(
        listOf(
            SyntaxElements.VARIABLE,
            SyntaxElements.IDENTIFIER_SYNTAX,
            SyntaxElements.TYPEASSIGNMENT,
            SyntaxElements.TYPE
        )
    ),

    // println("hello world")
    FUNCTION(
        listOf(
            SyntaxElements.VOIDFUNCTION
        )
    ),
}

object ExpressionProvider {
    val expressions = listOf(
        IdentifierExpression::class,
        LiteralExpression::class,
        OperationExpression::class,
        ParenthesisExpression::class

    )
}
