package ingsis.printscript.parser.implementations

import ingsis.printscript.parser.interfaces.SyntaxMatcher
import ingsis.printscript.parser.interfaces.SyntaxParser
import ingsis.printscript.utilities.enums.*
import ingsis.printscript.utilities.visitor.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class Expression(val matcher: ExpressionMatcher) : SyntaxParser {
    fun indexOfOperation(tokenList: List<Token>, syntaxElements: SyntaxElements): Int? {
        var parens = 0
        for ((index, element) in tokenList.withIndex()) {
            if (element is LEFT_PAREN) parens++
            else if (element is RIGHT_PAREN) parens--
            else if (parens == 0 && syntaxElements.contains(element)) return index
        }
        return null
    }
}

class ExpressionMatcher(matchers: List<KClass<out Expression>>) : SyntaxMatcher {

    private val expressions: List<Expression> = matchers.mapNotNull { it.primaryConstructor?.call(this) }

    override fun match(content: List<Token>): VisitableAST? =
        expressions.firstNotNullOfOrNull { it.parse(content) }
}

// (identifier)
class IdentifierExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size != 1) return null

        val identifier = if (tokenList[0] is IDENTIFIER) tokenList[0] as IDENTIFIER else null

        return if (identifier != null) VariableAST(identifier.value)
        else null
    }
}

// (literal)
class LiteralExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size != 1) return null

        val literal = if (tokenList[0] is Value) tokenList[0] as Value else null

        return if (literal != null) LiteralAST(literal)
        else null
    }
}

// (expression) ( * | / ) (expression)
class OperationExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size < 3) return null

        val operation = indexOfOperation(tokenList, SyntaxElements.OPERATION) ?: return null
        val left = matcher.match(tokenList.subList(0, operation))
        val right = matcher.match(tokenList.subList(operation + 1, tokenList.size))

        return if (left != null && right != null) BinaryOperationAST(left, right, tokenList[operation] as Operation)
        else null
    }
}

// ( ( ) (expression) ( ) )
class ParenthesisExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size < 3) return null

        val openParen = tokenList[0] is LEFT_PAREN
        val expression = matcher.match(tokenList.subList(1, tokenList.size - 1))
        val closeParen = tokenList[tokenList.size - 1] is RIGHT_PAREN

        return if (openParen && closeParen && expression != null) expression
        else null
    }
}
