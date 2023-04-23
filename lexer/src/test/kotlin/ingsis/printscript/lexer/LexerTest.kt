package ingsis.printscript.lexer

import ingsis.printscript.utilities.enums.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LexerTest {
    @Test
    fun testBasicKeywords() {
        val lexer = Lexer()
        val tokens = lexer.tokenize("let x = 5; const y = 10;")
        val expected = listOf(
            Pair(LET, 1), Pair(IDENTIFIER("x"), 1), Pair(ASSIGNATION, 1), Pair(NumValue(5.0), 1), Pair(SEMICOLON, 1),
            Pair(CONST, 1), Pair(IDENTIFIER("y"), 1), Pair(ASSIGNATION, 1), Pair(NumValue(10.0), 1), Pair(SEMICOLON, 1),
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testOperations() {
        val lexer = Lexer()
        val tokens = lexer.tokenize("x = x + 5;")
        val expected = listOf(
            Pair(IDENTIFIER("x"), 1),
            Pair(ASSIGNATION, 1),
            Pair(IDENTIFIER("x"), 1),
            Pair(ADD, 1),
            Pair(NumValue(5.0), 1),
            Pair(SEMICOLON, 1),
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testStrings() {
        val lexer = Lexer()
        val tokens = lexer.tokenize("""let s = "Hello, World!";""")
        val expected = listOf(
            Pair(LET, 1),
            Pair(IDENTIFIER("s"), 1),
            Pair(ASSIGNATION, 1),
            Pair(StrValue("Hello, World!"), 1),
            Pair(SEMICOLON, 1),
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testComplexOperations() {
        val lexer = Lexer()
        val tokens = lexer.tokenize("let result = (x * 5) / (y - 3);")
        val expected = listOf(
            Pair(LET, 1),
            Pair(IDENTIFIER("result"), 1),
            Pair(ASSIGNATION, 1),
            Pair(LEFT_PAREN, 1),
            Pair(IDENTIFIER("x"), 1),
            Pair(MUL, 1),
            Pair(NumValue(5.0), 1),
            Pair(RIGHT_PAREN, 1),
            Pair(DIV, 1),
            Pair(LEFT_PAREN, 1),
            Pair(IDENTIFIER("y"), 1),
            Pair(SUB, 1),
            Pair(NumValue(3.0), 1),
            Pair(RIGHT_PAREN, 1),
            Pair(SEMICOLON, 1),
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
            Pair(LET, 1),
            Pair(IDENTIFIER("x"), 1),
            Pair(ASSIGNATION, 1),
            Pair(NumValue(5.0), 1),
            Pair(SEMICOLON, 1),
            Pair(LET, 2),
            Pair(IDENTIFIER("y"), 2),
            Pair(ASSIGNATION, 2),
            Pair(
                NumValue(
                    10.0,
                ),
                2,
            ),
            Pair(SEMICOLON, 2),
            Pair(LET, 3),
            Pair(IDENTIFIER("z"), 3),
            Pair(ASSIGNATION, 3),
            Pair(IDENTIFIER("x"), 3),
            Pair(ADD, 3),
            Pair(IDENTIFIER("y"), 3),
            Pair(SEMICOLON, 3),
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testFunction() {
        val lexer = Lexer()
        val tokens = lexer.tokenize("print(x);")
        val expected = listOf(
            Pair(PRINT, 1),
            Pair(LEFT_PAREN, 1),
            Pair(IDENTIFIER("x"), 1),
            Pair(RIGHT_PAREN, 1),
            Pair(SEMICOLON, 1),
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun `test boolean literals`() {
        val lexer = Lexer()

        // Test boolean literals
        val input1 = "true"
        val expectedTokens1 = listOf(Pair(BoolValue(true), 1))
        assertEquals(expectedTokens1, lexer.tokenize(input1))

        val input2 = "false"
        val expectedTokens2 = listOf(Pair(BoolValue(false), 1))
        assertEquals(expectedTokens2, lexer.tokenize(input2))

        // Test boolean literals in an expression
        val input3 = "let a = true; let b = false;"
        val expectedTokens3 = listOf(
            Pair(LET, 1),
            Pair(IDENTIFIER("a"), 1),
            Pair(ASSIGNATION, 1),
            Pair(BoolValue(true), 1),
            Pair(SEMICOLON, 1),
            Pair(LET, 1),
            Pair(IDENTIFIER("b"), 1),
            Pair(ASSIGNATION, 1),
            Pair(BoolValue(false), 1),
            Pair(SEMICOLON, 1),
        )
        assertEquals(expectedTokens3, lexer.tokenize(input3))

        // Test boolean literals with other data types
        val input4 = """
        let a = true;
        let b = 42;
        let c = "Hello";
        """.trimIndent()
        val expectedTokens4 = listOf(
            Pair(LET, 1),
            Pair(IDENTIFIER("a"), 1),
            Pair(ASSIGNATION, 1),
            Pair(BoolValue(true), 1),
            Pair(SEMICOLON, 1),
            Pair(LET, 2),
            Pair(IDENTIFIER("b"), 2),
            Pair(ASSIGNATION, 2),
            Pair(NumValue(42.0), 2),
            Pair(SEMICOLON, 2),
            Pair(LET, 3),
            Pair(IDENTIFIER("c"), 3),
            Pair(ASSIGNATION, 3),
            Pair(StrValue("Hello"), 3),
            Pair(SEMICOLON, 3),
        )
        assertEquals(expectedTokens4, lexer.tokenize(input4))
    }
}
