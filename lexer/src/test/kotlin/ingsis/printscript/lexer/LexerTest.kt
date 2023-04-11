package ingsis.printscript.lexer

import ingsis.printscript.utilities.enums.ADD
import ingsis.printscript.utilities.enums.ASSIGNATION
import ingsis.printscript.utilities.enums.CONST
import ingsis.printscript.utilities.enums.DIV
import ingsis.printscript.utilities.enums.IDENTIFIER
import ingsis.printscript.utilities.enums.LEFT_PAREN
import ingsis.printscript.utilities.enums.LET
import ingsis.printscript.utilities.enums.MUL
import ingsis.printscript.utilities.enums.NumValue
import ingsis.printscript.utilities.enums.PRINT
import ingsis.printscript.utilities.enums.RIGHT_PAREN
import ingsis.printscript.utilities.enums.SEMICOLON
import ingsis.printscript.utilities.enums.SUB
import ingsis.printscript.utilities.enums.StrValue
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
}
