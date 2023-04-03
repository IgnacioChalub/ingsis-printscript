// package ingsis.printscript.lexer
//
// import ingsis.printscript.utilities.enums.*
// import org.junit.jupiter.api.Assertions.assertEquals
// import org.junit.jupiter.api.Test
//
// class Test {
//
//    @Test
//    fun testSymbolTokens() {
//        val input = ";:=(){}-+*/><"
//        val lexer = Lexer(input)
//        val expectedTokens = listOf(
//            SEMICOLON,
//            COLON,
//            ASSIGNATION,
//            LEFT_PAREN,
//            RIGHT_PAREN,
//            SUB,
//            ADD,
//            MUL,
//            DIV,
//        )
//
//        expectedTokens.forEach { expectedTokenType ->
//            val token = lexer.getNextToken()
//            assertEquals(expectedTokenType, token)
//        }
//    }
//
//    @Test
//    fun testNumberToken() {
//        val input = "42.5"
//        val lexer = Lexer2(input)
//        val numberToken = lexer.getNextToken()
//
//        assertEquals(NUMBER_TYPE, numberToken.type)
//        assertEquals(42.5, numberToken.value)
//
//        val eofToken = lexer.getNextToken()
//        assertEquals(EOF, eofToken.type)
//    }
//
//    @Test
//    fun testIdentifierToken() {
//        val input = "testIdentifier"
//        val lexer = Lexer2(input)
//        val identifierToken = lexer.getNextToken()
//
//        assertEquals(IDENTIFIER, identifierToken.type)
//        assertEquals("testIdentifier", identifierToken.value)
//
//        val eofToken = lexer.getNextToken()
//        assertEquals(EOF, eofToken.type)
//    }
//
//    @Test
//    fun testOperators() {
//        val input = ">= <= < > = + - * /"
//        val lexer = Lexer2(input)
//        val expectedTokens = listOf(
//            GREATER_EQUAL,
//            LESS_EQUAL,
//            LESS,
//            GREATER,
//            ASSIGNATION,
//            PLUS,
//            MINUS,
//            MULTIPLY,
//            DIVIDE,
//            EOF,
//        )
//
//        expectedTokens.forEach { expectedTokenType ->
//            val token = lexer.getNextToken()
//            assertEquals(expectedTokenType, token.type)
//        }
//    }
//
//    @Test
//    fun testString() {
//        val input = "\"Hello, world!\""
//        val lexer = Lexer2(input)
//        val token = lexer.getNextToken()
//
//        assertEquals(STRING, token.type)
//        assertEquals("Hello, world!", token.value)
//        assertEquals(EOF, lexer.getNextToken().type)
//    }
//
//    @Test
//    fun testNumbers() {
//        val input = "42 3.14"
//        val lexer = Lexer2(input)
//        val expectedTokens = listOf(
//            Token(NUMBER_TYPE, 42.0),
//            Token(NUMBER_TYPE, 3.14),
//            Token(EOF, ""),
//        )
//
//        expectedTokens.forEach { expectedToken ->
//            val token = lexer.getNextToken()
//            assertEquals(expectedToken.type, token.type)
//            assertEquals(expectedToken.value, token.value)
//        }
//    }
//
//    @Test
//    fun testIdentifiers() {
//        val input = "foo bar_123"
//        val lexer = Lexer2(input)
//        val expectedTokens = listOf(
//            Token(IDENTIFIER, "foo "),
//            Token(IDENTIFIER, "bar_123"),
//            Token(EOF, ""),
//        )
//
//        expectedTokens.forEach { expectedToken ->
//            val token = lexer.getNextToken()
//            assertEquals(expectedToken.type, token.type)
//            assertEquals(expectedToken.value, token.value)
//        }
//    }
// }
