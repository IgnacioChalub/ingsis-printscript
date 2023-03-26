package ingsis.printscript.lexer

import Token
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Lexer2Test {

    @Test
    fun testSymbolTokens() {
        val input = ";:=(){}-+*/><"
        val lexer = Lexer2(input)
        val expectedTokens = listOf(
            TokenType.SEMICOLON,
            TokenType.COLON,
            TokenType.ASSIGNATION,
            TokenType.LEFT_PAREN,
            TokenType.RIGHT_PAREN,
            TokenType.LEFT_CURLY_BRACES,
            TokenType.RIGHT_CURLY_BRACES,
            TokenType.MINUS,
            TokenType.PLUS,
            TokenType.MULTIPLY,
            TokenType.DIVIDE,
            TokenType.GREATER,
            TokenType.LESS,
            TokenType.EOF
        )

        expectedTokens.forEach { expectedTokenType ->
            val token = lexer.getNextToken()
            assertEquals(expectedTokenType, token.type)
        }
    }


    @Test
    fun testNumberToken() {
        val input = "42.5"
        val lexer = Lexer2(input)
        val numberToken = lexer.getNextToken()

        assertEquals(TokenType.NUMBER_TYPE, numberToken.type)
        assertEquals(42.5, numberToken.value)

        val eofToken = lexer.getNextToken()
        assertEquals(TokenType.EOF, eofToken.type)
    }

    @Test
    fun testIdentifierToken() {
        val input = "testIdentifier"
        val lexer = Lexer2(input)
        val identifierToken = lexer.getNextToken()

        assertEquals(TokenType.IDENTIFIER, identifierToken.type)
        assertEquals("testIdentifier", identifierToken.value)

        val eofToken = lexer.getNextToken()
        assertEquals(TokenType.EOF, eofToken.type)
    }

    @Test
    fun testOperators() {
        val input = ">= <= < > = + - * /"
        val lexer = Lexer2(input)
        val expectedTokens = listOf(
            TokenType.GREATER_EQUAL,
            TokenType.LESS_EQUAL,
            TokenType.LESS,
            TokenType.GREATER,
            TokenType.ASSIGNATION,
            TokenType.PLUS,
            TokenType.MINUS,
            TokenType.MULTIPLY,
            TokenType.DIVIDE,
            TokenType.EOF
        )

        expectedTokens.forEach { expectedTokenType ->
            val token = lexer.getNextToken()
            assertEquals(expectedTokenType, token.type)
        }
    }

    @Test
    fun testString() {
        val input = "\"Hello, world!\""
        val lexer = Lexer2(input)
        val token = lexer.getNextToken()

        assertEquals(TokenType.STRING, token.type)
        assertEquals("Hello, world!", token.value)
        assertEquals(TokenType.EOF, lexer.getNextToken().type)
    }

    @Test
    fun testNumbers() {
        val input = "42 3.14"
        val lexer = Lexer2(input)
        val expectedTokens = listOf(
            Token(TokenType.NUMBER_TYPE, 42.0),
            Token(TokenType.NUMBER_TYPE, 3.14),
            Token(TokenType.EOF, "")
        )

        expectedTokens.forEach { expectedToken ->
            val token = lexer.getNextToken()
            assertEquals(expectedToken.type, token.type)
            assertEquals(expectedToken.value, token.value)
        }
    }

    @Test
    fun testIdentifiers() {
        val input = "foo bar_123"
        val lexer = Lexer2(input)
        val expectedTokens = listOf(
            Token(TokenType.IDENTIFIER, "foo "),
            Token(TokenType.IDENTIFIER, "bar_123"),
            Token(TokenType.EOF, "")
        )

        expectedTokens.forEach { expectedToken ->
            val token = lexer.getNextToken()
            assertEquals(expectedToken.type, token.type)
            assertEquals(expectedToken.value, token.value)
        }
    }
}

