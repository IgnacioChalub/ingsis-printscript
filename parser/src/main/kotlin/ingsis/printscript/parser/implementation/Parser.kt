package implementation

import AST
import IAST
import Leaf
import LeafToken
import NodeToken
import Token
import UnaryAST
import UtilToken
import `interface`.IParser

class Parser : IParser {
    override fun parse(tokenList: List<Token>): IAST {
        return parseDeclaration(tokenList) ?: parseFunction(tokenList) ?: parseExpression(tokenList)
    }

    private fun parseDeclaration(tokenList: List<Token>): IAST? {
        // Check list is valid
        if (invalidDeclarationList(tokenList)) return null

        val declaration =
            AST(NodeToken.COLON, Leaf(tokenList[1] as LeafToken.IDENTIFIER), Leaf(tokenList[2] as LeafToken.TYPE))
        val assignment = if (tokenList[4] is LeafToken.IDENTIFIER) {
            Leaf(tokenList[4] as LeafToken.IDENTIFIER)
        } else {
            parse(tokenList.subList(4, tokenList.size - 1))
        }
        return AST(NodeToken.ASSIGNATION, declaration, assignment)
    }

    private fun parseFunction(tokenList: List<Token>): IAST? {
        if (invalidFunctionList(tokenList)) return null
        return UnaryAST(tokenList[0] as NodeToken, parse(tokenList.subList(1, tokenList.size - 1)))
    }

    private fun parseExpression(subList: List<Token>): IAST {
        return if (subList.size == 1) {
            Leaf(subList.first() as LeafToken.LITERAL)
        } else {
            AST(
                subList[1] as NodeToken.OPERATOR,
                Leaf(subList.first() as LeafToken.LITERAL),
                parse(subList.subList(2, subList.size))
            )
        }
    }

    private fun invalidDeclarationList(tokenList: List<Token>): Boolean {
        // TODO review with no assignation
        return tokenList.size < 6 ||
            tokenList.first() !is UtilToken.LET_KEY_WORD ||
            tokenList.last() !is UtilToken.SEMICOLON ||
            tokenList[1] !is LeafToken.IDENTIFIER ||
            tokenList[2] !is NodeToken.COLON ||
            tokenList[3] !is LeafToken.TYPE ||
            tokenList[4] !is NodeToken.ASSIGNATION
    }

    private fun invalidFunctionList(tokenList: List<Token>): Boolean {
        return tokenList.size < 2 ||
            tokenList.first() !is NodeToken.FUNCTION
    }
}
