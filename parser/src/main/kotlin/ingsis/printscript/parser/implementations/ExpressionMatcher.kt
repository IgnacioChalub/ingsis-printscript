package ingsis.printscript.parser.implementations

import ingsis.printscript.parser.interfaces.SyntaxMatcher
import ingsis.printscript.parser.interfaces.SyntaxParser
import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports
import ingsis.printscript.utilities.enums.Function
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class Expression(val matcher: ExpressionMatcher) : SyntaxParser {
    fun indexOfOperation(tokenList: List<Token>, syntaxElements: SyntaxElements): Int? {
        var parens = 0
        for ((index, element) in tokenList.withIndex()) {
            if (element is LEFT_PAREN) {
                parens++
            } else if (element is RIGHT_PAREN) {
                parens--
            } else if (parens == 0 && syntaxElements.contains(element)) return index
        }
        return null
    }
}

class ExpressionMatcher(matchers: List<KClass<out Expression>>) : SyntaxMatcher {

    private val expressions: List<Expression> = matchers.mapNotNull { it.primaryConstructor?.call(this) }

    override fun match(tokenList: List<Token>): VisitableAST =
        expressions.firstNotNullOfOrNull { it.parse(tokenList) } ?: throw Exception("Expression not found!")
}

// (identifier)
class IdentifierExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size != 1) return null

        val identifier = if (tokenList[0] is IDENTIFIER) tokenList[0] as IDENTIFIER else null

        return if (identifier != null) {
            VariableAST(identifier.value)
        } else {
            null
        }
    }
}

// (literal)
class LiteralExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size != 1) return null

        val literal = if (tokenList[0] is Value) tokenList[0] as Value else null

        return if (literal != null) {
            LiteralAST(literal)
        } else {
            null
        }
    }
}

// (expression) ( * | / ) (expression)
class OperationExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size < 3) return null

        val operation = indexOfOperation(tokenList, SyntaxElements.OPERATION) ?: return null
        val left = matcher.match(tokenList.subList(0, operation))
        val right = matcher.match(tokenList.subList(operation + 1, tokenList.size))

        return BinaryOperationAST(left, right, tokenList[operation] as Operation)
    }
}

// ( ( ) (expression) ( ) )
class ParenthesisExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size < 3) return null

        val openParen = tokenList[0] is LEFT_PAREN
        val closeParen = tokenList[tokenList.size - 1] is RIGHT_PAREN

        return if (openParen && closeParen) {
            matcher.match(tokenList.subList(1, tokenList.size - 1))
        } else {
            null
        }
    }
}

// readinput...
class FunctionExpression(matcher: ExpressionMatcher) : Expression(matcher) {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size < 4) return null

        val function = tokenList[0] is Function
        val leftParen = tokenList[1] is LEFT_PAREN
        val args = tokenList[2] is StrValue
        val rightParen = tokenList[3] is RIGHT_PAREN
        return if (function && leftParen && rightParen && args) {
            UnaryOperationAST(tokenList[0] as Function, LiteralAST(tokenList[2] as StrValue))
        } else {
            null
        }
    }
}
