package ingsis.printscript.parser.implementations

import ingsis.printscript.parser.interfaces.SyntaxElement
import ingsis.printscript.parser.interfaces.SyntaxParser
import ingsis.printscript.utilities.enums.*
import kotlin.reflect.KClass

enum class SyntaxElements(override val types: List<Token>) : SyntaxElement {

    TYPE(listOf(STR, NUM, BOOL)),

    VARIABLE(listOf(LET, CONST)),

    OPERATION(listOf(ADD, SUB, DIV, MUL)),

    IDENTIFIER_SYNTAX(listOf(IDENTIFIER("any"))),

    ASSIGNATION_SYNTAX(listOf(ASSIGNATION)),

    TYPEASSIGNMENT(listOf(COLON)),

    VOIDFUNCTION(listOf(PRINT)),

    LEFT_PAREN_SYNTAX(listOf(LEFT_PAREN)),
    RIGHT_PAREN_SYNTAX(listOf(RIGHT_PAREN)),

    LITERAL(listOf(StrValue(""), NumValue(0.0))),

    END(listOf(SEMICOLON)),

    IF_SYNTAX(listOf(IF)),

    ELSE_SYNTAX(listOf(ELSE)),

    ANY(listOf()),
}

object StatementsProvider {
    fun getStatements(version: Version): List<Pair<KClass<out SyntaxParser>, List<Any>>> {
        return when (version) {
            Version.VERSION_1_0 -> listOf(
                DeclarationParser::class to listOf(),
                AssignationParser::class to listOf(version),
                AssignationDeclarationParser::class to listOf(version),
                FunctionParser::class to listOf(version),
            )

            Version.VERSION_1_1 -> listOf(
                DeclarationParser::class to listOf(),
                AssignationParser::class to listOf(version),
                AssignationDeclarationParser::class to listOf(version),
                FunctionParser::class to listOf(version),
                IfStatementParser(version)::class to listOf(version),
                IfElseStatementParser(version)::class to listOf(version),
            )
        }
    }
}

object ExpressionProvider {
    fun getExpressions(version: Version): List<KClass<out Expression>> {
        return when (version) {
            Version.VERSION_1_0 -> listOf(
                IdentifierExpression::class,
                LiteralExpression::class,
                OperationExpression::class,
                ParenthesisExpression::class,
            )
            Version.VERSION_1_1 -> listOf(
                IdentifierExpression::class,
                LiteralExpression::class,
                OperationExpression::class,
                ParenthesisExpression::class,
                FunctionExpression::class,
            )
        }
    }
}
