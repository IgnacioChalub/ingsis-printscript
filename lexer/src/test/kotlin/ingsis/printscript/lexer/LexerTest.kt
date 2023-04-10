package ingsis.printscript.lexer

import ADD
import ASSIGNATION
import CONST
import DIV
import IDENTIFIER
import LEFT_PAREN
import LET
import MUL
import NumValue
import PRINT
import RIGHT_PAREN
import SEMICOLON
import SUB
import StrValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LexerTest {
    @Test
    fun testBasicKeywords() {
        val lexer = Lexer("let x = 5; const y = 10;")
        val tokens = lexer.tokenize()
        val expected = listOf(
            LET, IDENTIFIER("x"), ASSIGNATION, NumValue(5.0), SEMICOLON,
            CONST, IDENTIFIER("y"), ASSIGNATION, NumValue(10.0), SEMICOLON
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testOperations() {
        val lexer = Lexer("x = x + 5;")
        val tokens = lexer.tokenize()
        val expected = listOf(
            IDENTIFIER("x"), ASSIGNATION, IDENTIFIER("x"), ADD, NumValue(5.0), SEMICOLON
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testStrings() {
        val lexer = Lexer("""let s = "Hello, World!";""")
        val tokens = lexer.tokenize()
        val expected = listOf(
            LET, IDENTIFIER("s"), ASSIGNATION, StrValue("Hello, World!"), SEMICOLON
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testComplexOperations() {
        val lexer = Lexer("let result = (x * 5) / (y - 3);")
        val tokens = lexer.tokenize()
        val expected = listOf(
            LET, IDENTIFIER("result"), ASSIGNATION, LEFT_PAREN, IDENTIFIER("x"), MUL, NumValue(5.0), RIGHT_PAREN, DIV, LEFT_PAREN, IDENTIFIER("y"), SUB, NumValue(3.0), RIGHT_PAREN, SEMICOLON
        )
        assertEquals(expected, tokens)
    }

    @Test
    fun testMultipleLines() {
        val lexer = Lexer("""
            let x = 5;
            let y = 10;
            let z = x + y;
        """.trimIndent())
        val tokens = lexer.tokenize()
        val expected = listOf(
            LET, IDENTIFIER("x"), ASSIGNATION, NumValue(5.0), SEMICOLON,
            LET, IDENTIFIER("y"), ASSIGNATION, NumValue(10.0), SEMICOLON,
            LET, IDENTIFIER("z"), ASSIGNATION, IDENTIFIER("x"), ADD, IDENTIFIER("y"), SEMICOLON
        )
        assertEquals(expected, tokens)
    }


    @Test
    fun testFunction() {
        val lexer = Lexer("print(x);")
        val tokens = lexer.tokenize()
        val expected = listOf(
            PRINT, LEFT_PAREN, IDENTIFIER("x"), RIGHT_PAREN, SEMICOLON
        )
        assertEquals(expected, tokens)
    }
}

