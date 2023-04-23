package ingsis.printscript.parser.implementations

import ingsis.printscript.parser.interfaces.SyntaxMatcher
import ingsis.printscript.parser.interfaces.SyntaxParser
import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports
import ingsis.printscript.utilities.enums.Function
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class Expression(val matcher: ExpressionMatcher) : SyntaxParser {
    fun indexOfOperation(tokenList: List<Pair<Token, Int>>, syntaxElements: SyntaxElements): Int? {
        var parens = 0
        for ((index, element) in tokenList.withIndex()) {
            if (element.first is LEFT_PAREN) {
                parens++
            } else if (element.first is RIGHT_PAREN) {
                parens--
            } else if (parens == 0 && syntaxElements.contains(element.first)) return index
        }
        return null
    }
}

class ExpressionMatcher(matchers: List<KClass<out Expression>>) : SyntaxMatcher {

    private val expressions: List<Expression> = matchers.mapNotNull { it.primaryConstructor?.call(this) }

    override fun match(tokenList: List<Pair<Token, Int>>): VisitableAST =
        expressions.firstNotNullOfOrNull { it.parse(tokenList) } ?: throw Exception("Expression not found!")
}

// (identifier)
class IdentifierExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        if (tokenList.size != 1) return null

        val identifier = if (tokenList[0].first is IDENTIFIER) tokenList[0].first as IDENTIFIER else return null

        return VariableAST(identifier.value to tokenList[0].second)
    }
}

// (literal)
class LiteralExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        if (tokenList.size != 1) return null

        val literal = if (tokenList[0].first is Value) tokenList[0].first as Value else return null

        return LiteralAST(literal to tokenList[0].second)
    }
}

// (expression) ( * | / ) (expression)
class OperationExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        if (tokenList.size < 3) return null

        val operation = indexOfOperation(tokenList, SyntaxElements.OPERATION) ?: return null
        val left = matcher.match(tokenList.subList(0, operation))
        val right = matcher.match(tokenList.subList(operation + 1, tokenList.size))

        return BinaryOperationAST(left, right, tokenList[operation].first as Operation to tokenList[operation].second)
    }
}

// ( ( ) (expression) ( ) )
class ParenthesisExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        if (tokenList.size < 3) return null

        val openParen = tokenList[0].first is LEFT_PAREN
        val closeParen = tokenList[tokenList.size - 1].first is RIGHT_PAREN

        return if (openParen && closeParen) {
            matcher.match(tokenList.subList(1, tokenList.size - 1))
        } else {
            null
        }
    }
}

// readinput...
class FunctionExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        if (tokenList.size < 4) return null

        val function = tokenList[0].first is Function
        val leftParen = tokenList[1].first is LEFT_PAREN
        val args = tokenList[2].first is StrValue
        val rightParen = tokenList[3].first is RIGHT_PAREN
        return if (function && leftParen && rightParen && args) {
            UnaryOperationAST(tokenList[0].first as Function to tokenList[0].second, LiteralAST(tokenList[2].first as StrValue to tokenList[2].second))
        } else {
            null
        }
    }
}
