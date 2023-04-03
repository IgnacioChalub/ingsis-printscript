package ingsis.printscript.parser

import ingsis.printscript.parser.implementations.Parser
import ingsis.printscript.utilities.enums.Token
import ingsis.printscript.utilities.enums.TokenType
import ingsis.printscript.utilities.visitor.AssignationAST
import ingsis.printscript.utilities.visitor.DeclarationAST
import ingsis.printscript.utilities.visitor.LiteralAST
import ingsis.printscript.utilities.visitor.STR
import ingsis.printscript.utilities.visitor.StrValue
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MainTest {

    private val parser = Parser()

    @Test
    fun normalExpressionShouldReturnTree() {
        val tokenList = listOf(
            Token(TokenType.LET),
            Token(TokenType.IDENTIFIER, "name"),
            Token(TokenType.COLON),
            Token(TokenType.TYPE, "String"),
            Token(TokenType.ASSIGNATION),
            Token(TokenType.LITERAL, "Fede"),
            Token(TokenType.SEMICOLON),
        )

        val expectedTree = AssignationAST(
            DeclarationAST(
                "name",
                STR,
            ),
            LiteralAST(
                StrValue("Fede"),
            ),
        )
        val res = (parser.parse(tokenList) as AssignationAST)
        assertTrue { expectedTree == parser.parse(tokenList) }
    }
}
