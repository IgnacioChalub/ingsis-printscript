package ingsis.printscript.parser.implementations

import ingsis.printscript.parser.interfaces.IStatement
import ingsis.printscript.parser.interfaces.SyntaxElement
import ingsis.printscript.utilities.enums.ADD
import ingsis.printscript.utilities.enums.ASSIGNATION
import ingsis.printscript.utilities.enums.COLON
import ingsis.printscript.utilities.enums.DIV
import ingsis.printscript.utilities.enums.IDENTIFIER
import ingsis.printscript.utilities.enums.LET
import ingsis.printscript.utilities.enums.MUL
import ingsis.printscript.utilities.enums.NUM
import ingsis.printscript.utilities.enums.PRINT
import ingsis.printscript.utilities.enums.SEMICOLON
import ingsis.printscript.utilities.enums.STR
import ingsis.printscript.utilities.enums.SUB
import ingsis.printscript.utilities.enums.Token

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
    // ingsis.printscript.utilities.enums.LET id: String = "name"
    ASSIGNATION(
        listOf(
            SyntaxElements.VARIABLE,
            SyntaxElements.IDENTIFIER_SYNTAX,
            SyntaxElements.TYPEASSIGNMENT,
            SyntaxElements.TYPE,
        ),
    ),

    // println("hello world")
    FUNCTION(
        listOf(
            SyntaxElements.VOIDFUNCTION,
        ),
    ),
}

object ExpressionProvider {
    val expressions = listOf(
        IdentifierExpression::class,
        LiteralExpression::class,
        OperationExpression::class,
        ParenthesisExpression::class,

    )
}
