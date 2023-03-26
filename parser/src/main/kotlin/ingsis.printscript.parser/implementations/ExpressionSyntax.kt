package ingsis.printscript.parser.implementations

import ingsis.printscript.utilities.enums.TokenType
import ingsis.printscript.utilities.types.Token
import ingsis.printscript.utilities.visitor.*

interface ExpressionFormat

object LiteralFormat : SyntaxFormat {
    val format = listOf(
        TokenType.LITERAL
    )
}

object OperationFormat : SyntaxFormat {
    val format = listOf(
        TokenType.LITERAL,
        TokenType.OPERATOR,
        TokenType.LITERAL
    )
}

object ExpressionProvider {
    fun parse(tokenList: List<Token>): ExpressionAST {
        return if (matchesLiteral(tokenList)) {
            LiteralSyntax.parse(tokenList)
        } else if (matchesOperation(tokenList)) {
            OperationSyntax.parse(tokenList)
        } else {
            throw Exception("Couldn't parse syntax")
        }
    }

    private fun matchesLiteral(tokenList: List<Token>): Boolean {
        return tokenList.zip(LiteralFormat.format).all { (token, type) -> token.type == type }
    }

    private fun matchesOperation(tokenList: List<Token>): Boolean {
        return tokenList.zip(OperationFormat.format).all { (token, type) -> token.type == type }
    }
}

interface ExpressionSyntax {
    fun parse(tokenList: List<Token>): ExpressionAST
}

object LiteralSyntax : ExpressionSyntax {
    override fun parse(tokenList: List<Token>): ExpressionAST {
        return when (tokenList[0].value) {
            is String -> {
                LiteralAST(StrValue(tokenList[0].value as String))
            }

            is Number -> {
                LiteralAST(NumValue(tokenList[0].value as Double))
            }

            else -> {
                throw Exception("Not valid literal")
            }
        }
    }
}

object OperationSyntax : ExpressionSyntax {
    override fun parse(tokenList: List<Token>): ExpressionAST {
        TODO("Not yet implemented")
    }
}
