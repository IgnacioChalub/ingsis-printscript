package ingsis.printscript.formatter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FormatterTest {

    @Test
    fun `test if block formatting`() {
        val formatter = Formatter(4)
        val code = """
            if (a > b) {
                println("A is greater than B")
            }
        """.trimIndent()

        val expected = """
            if (a > b) {
                println("A is greater than B")
            }
        """.trimIndent()

        assertEquals(expected, formatter.format(code))
    }

    @Test
    fun `test nested if block formatting`() {
        val formatter = Formatter(4)
        val code = """
            if (a > b) {
                if (a > c) {
                    println("A is the greatest")
                }
            }
        """.trimIndent()

        val expected = """
            if (a > b) {
                if (a > c) {
                    println("A is the greatest")
                }
            }
        """.trimIndent()

        assertEquals(expected, formatter.format(code))
    }

    @Test
    fun `test configurable indentation`() {
        val formatter = Formatter(2)
        val code = """
            if (a > b) {
                println("A is greater than B")
            }
        """.trimIndent()

        val expected = """
            if (a > b) {
              println("A is greater than B")
            }
        """.trimIndent()

        assertEquals(expected, formatter.format(code))
    }
}
