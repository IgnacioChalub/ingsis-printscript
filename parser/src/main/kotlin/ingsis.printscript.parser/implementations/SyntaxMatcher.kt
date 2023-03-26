package ingsis.printscript.parser.implementations

import ingsis.printscript.utilities.enums.TokenType
import ingsis.printscript.utilities.types.Token
import ingsis.printscript.utilities.visitor.*
import ingsis.printscript.utilities.visitor.Function

interface SyntaxFormat

object AssignationFormat : SyntaxFormat {
    val format = listOf(
        TokenType.LET,
        TokenType.IDENTIFIER,
        TokenType.COLON,
        TokenType.TYPE
    )
}

object FunctionFormat : SyntaxFormat {
    val format = listOf(
        // Include other functions
        TokenType.FUNCTION
    )
}

class SyntaxProvider(private val tokenList: List<Token>) {
    fun provide(): SyntaxMatcher {
        return if (matchesAssignation(tokenList)) {
            AssignationMatcher()
        } else if (matchesFunction(tokenList)) {
            FunctionMatcher()
        } else {
            throw Exception("Couldn't find syntax")
        }
    }

    // Could be better (not duplicated)
    private fun matchesAssignation(tokenList: List<Token>): Boolean {
        val size = minOf(tokenList.size, AssignationFormat.format.size)
        return tokenList.take(size).zip(AssignationFormat.format.take(size)).all { (token, type) -> token.type == type }
    }

    private fun matchesFunction(tokenList: List<Token>): Boolean {
        val size = minOf(tokenList.size, FunctionFormat.format.size)
        return tokenList.take(size).zip(FunctionFormat.format.take(size)).all { (token, type) -> token.type == type }
    }
}

interface SyntaxMatcher {
    fun parse(tokenList: List<Token>): VisitableAST
}

class AssignationMatcher : SyntaxMatcher {
    override fun parse(tokenList: List<Token>): VisitableAST {
        return AssignationAST(
            DeclarationAST(
                tokenList[1].value as String,
                STR
            ), // Should be dynamic but WE NEED UNIFIED TOKENSSSSSSSSSSS CARAMBA
            ExpressionProvider.parse(tokenList.slice(5 until tokenList.size))
        )
    }
}

class FunctionMatcher : SyntaxMatcher {
    override fun parse(tokenList: List<Token>): VisitableAST {
        return UnaryOperationAST(tokenList[0].value as Function, ExpressionProvider.parse(tokenList.slice(1 until tokenList.size)))
    }
}
