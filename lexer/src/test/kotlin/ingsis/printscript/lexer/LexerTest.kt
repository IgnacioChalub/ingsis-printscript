package ingsis.printscript.lexer

import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LexerTest {
    @Test
    fun testBasicKeywords() {
        val lexer = Lexer()
        val tokens = lexer.tokenize("let x = 5; const y = 10;")
        val expected = listOf(
            LET, IDENTIFIER("x"), ASSIGNATION, NumValue(5.0), SEMICOLON,
            CONST, IDENTIFIER("y"), ASSIGNATION, NumValue(10.0), SEMICOLON,
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testOperations() {
        val lexer = Lexer()
        val tokens = lexer.tokenize("x = x + 5;")
        val expected = listOf(
            IDENTIFIER("x"),
            ASSIGNATION,
            IDENTIFIER("x"),
            ADD,
            NumValue(5.0),
            SEMICOLON,
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testStrings() {
        val lexer = Lexer()
        val tokens = lexer.tokenize("""let s = "Hello, World!";""")
        val expected = listOf(
            LET,
            IDENTIFIER("s"),
            ASSIGNATION,
            StrValue("Hello, World!"),
            SEMICOLON,
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testComplexOperations() {
        val lexer = Lexer()
        val tokens = lexer.tokenize("let result = (x * 5) / (y - 3);")
        val expected = listOf(
            LET, IDENTIFIER("result"), ASSIGNATION, LEFT_PAREN, IDENTIFIER("x"), MUL, NumValue(5.0), RIGHT_PAREN, DIV, LEFT_PAREN, IDENTIFIER("y"), SUB, NumValue(3.0), RIGHT_PAREN, SEMICOLON,
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testMultipleLines() {
        val lexer = Lexer()
        val tokens = lexer.tokenize(
            """
            let x = 5;
            let y = 10;
            let z = x + y;
            """.trimIndent(),
        )
        val expected = listOf(
            LET, IDENTIFIER("x"), ASSIGNATION, NumValue(5.0), SEMICOLON,
            LET, IDENTIFIER("y"), ASSIGNATION, NumValue(10.0), SEMICOLON,
            LET, IDENTIFIER("z"), ASSIGNATION, IDENTIFIER("x"), ADD, IDENTIFIER("y"), SEMICOLON,
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testFunction() {
        val lexer = Lexer()
        val tokens = lexer.tokenize("print(x);")
        val expected = listOf(
            PRINT,
            LEFT_PAREN,
            IDENTIFIER("x"),
            RIGHT_PAREN,
            SEMICOLON,
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun `test boolean literals`() {
        val lexer = Lexer()

        // Test boolean literals
        val input1 = "true"
        val expectedTokens1 = listOf(BoolValue(true))
        assertEquals(expectedTokens1, lexer.tokenize(input1))

        val input2 = "false"
        val expectedTokens2 = listOf(BoolValue(false))
        assertEquals(expectedTokens2, lexer.tokenize(input2))

        // Test boolean literals in an expression
        val input3 = "let a = true; let b = false;"
        val expectedTokens3 = listOf(
            LET,
            IDENTIFIER("a"),
            ASSIGNATION,
            BoolValue(true),
            SEMICOLON,
            LET,
            IDENTIFIER("b"),
            ASSIGNATION,
            BoolValue(false),
            SEMICOLON
        )
        assertEquals(expectedTokens3, lexer.tokenize(input3))

        // Test boolean literals with other data types
        val input4 = """
            let a = true;
            let b = 42;
            let c = "Hello";
        """.trimIndent()
        val expectedTokens4 = listOf(
            LET,
            IDENTIFIER("a"),
            ASSIGNATION,
            BoolValue(true),
            SEMICOLON,
            LET,
            IDENTIFIER("b"),
            ASSIGNATION,
            NumValue(42.0),
            SEMICOLON,
            LET,
            IDENTIFIER("c"),
            ASSIGNATION,
            StrValue("Hello"),
            SEMICOLON
        )
        assertEquals(expectedTokens4, lexer.tokenize(input4))
    }
}
