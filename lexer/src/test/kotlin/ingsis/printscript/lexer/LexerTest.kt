package ingsis.printscript.lexer

import ingsis.printscript.utilities.enums.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LexerTest {
    private val lexer = Lexer()

    @Test
    fun testSingleStatement() {
        val input = "let x = 5;"
        val expected = listOf(
            LET,
            IDENTIFIER("x"),
            ASSIGNATION,
            NumValue(5.0),
            SEMICOLON,
        )
        val result = lexer.tokenize(input)
        assertEquals(expected, result)
    }

//    @Test
//    fun testMultipleStatements() {
//        val input = """
//            let x = 5;
//            let y = 10;
//            let z = x + y;
//        """.trimIndent()
//        val expected = listOf(
//            listOf(
//                Pair(LET, 1),
//                Pair(IDENTIFIER("x"), 1),
//                Pair(ASSIGNATION, 1),
//                Pair(NumValue(5.0), 1),
//                Pair(SEMICOLON, 1),
//            ),
//            listOf(
//                Pair(LET, 2),
//                Pair(IDENTIFIER("y"), 2),
//                Pair(ASSIGNATION, 2),
//                Pair(NumValue(10.0), 2),
//                Pair(SEMICOLON, 2),
//            ),
//            listOf(
//                Pair(LET, 3),
//                Pair(IDENTIFIER("z"), 3),
//                Pair(ASSIGNATION, 3),
//                Pair(IDENTIFIER("x"), 3),
//                Pair(ADD, 3),
//                Pair(IDENTIFIER("y"), 3),
//                Pair(SEMICOLON, 3),
//            ),
//        )
//        val result = lexer.tokenize(input)
//        assertEquals(expected, result)
//    }

    @Test
    fun testEmptyInput() {
        val input = ""
        val expected = emptyList<List<Pair<Token, Int>>>()
        val result = lexer.tokenize(input)
        assertEquals(expected, result)
    }

//    @Test
//    fun testIfElseStatement() {
//        val input = """
//        if (x > 5) {
//            let message = "Greater than 5";
//        } else {
//            let message = "Not greater than 5";
//        }
//    """.trimIndent()
//        val expected = listOf(
//            listOf(
//                Pair(IF, 1),
//                Pair(LEFT_PAREN, 1),
//                Pair(IDENTIFIER("x"), 1),
//                Pair(GREATER, 1),
//                Pair(NumValue(5.0), 1),
//                Pair(RIGHT_PAREN, 1),
//                Pair(LEFT_BRACE, 1)
//            ),
//            listOf(
//                Pair(LET, 2),
//                Pair(IDENTIFIER("message"), 2),
//                Pair(ASSIGNATION, 2),
//                Pair(StrValue("Greater than 5"), 2),
//                Pair(SEMICOLON, 2)
//            ),
//            listOf(
//                Pair(RIGHT_BRACE, 3),
//                Pair(ELSE, 3),
//                Pair(LEFT_BRACE, 3)
//            ),
//            listOf(
//                Pair(LET, 4),
//                Pair(IDENTIFIER("message"), 4),
//                Pair(ASSIGNATION, 4),
//                Pair(StrValue("Not greater than 5"), 4),
//                Pair(SEMICOLON, 4)
//            ),
//            listOf(
//                Pair(RIGHT_BRACE, 5)
//            )
//        )
//        val result = lexer.tokenize(input)
//        assertEquals(expected, result)
//    }

    @Test
    fun testFunctionCall() {
        val input = "print(\"Hello, world!\");"
        val expected = listOf(
            PRINT,
            LEFT_PAREN,
            StrValue("Hello, world!"),
            RIGHT_PAREN,
            SEMICOLON,
        )
        val result = lexer.tokenize(input)
        assertEquals(expected, result)
    }
}
