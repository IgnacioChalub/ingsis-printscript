package ingsis.printscript.lexer
import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports

class Lexer() {

    private var start = 0
    private var current = 0
    private var line = 1

    private val keywords = mapOf(
        "let" to LET,
        "const" to CONST,
        "if" to IF,
        "else" to ELSE,
        "Number" to NUM,
        "String" to STR,
        "print" to PRINT,
        "readinput" to READINPUT,
        "Boolean" to BOOL,
    )

    fun tokenize(input: String): List<Pair<Token, Int>> {
        val tokens = mutableListOf<Pair<Token, Int>>()
        while (!isAtEnd(input)) {
            start = current
            val c = advance(input)
            when (c) {
                '\n' -> line++
                '(' -> tokens.add(Pair(LEFT_PAREN, line))
                ')' -> tokens.add(Pair(RIGHT_PAREN, line))
                '+' -> tokens.add(Pair(ADD, line))
                '-' -> tokens.add(Pair(SUB, line))
                '*' -> tokens.add(Pair(MUL, line))
                '/' -> tokens.add(Pair(DIV, line))
                ';' -> tokens.add(Pair(SEMICOLON, line))
                ':' -> tokens.add(Pair(COLON, line))
                '=' -> tokens.add(Pair(ASSIGNATION, line))
                in '0'..'9' -> number(input, tokens)
                in 'a'..'z', in 'A'..'Z', '_' -> identifier(input, tokens)
                '"' -> string(input, tokens)
            }
        }
        start = 0
        current = 0
        line = 1
        return tokens
    }

    private fun identifier(input: String, tokens: MutableList<Pair<Token, Int>>) {
        while (isAlphaNumeric(peek(input))) {
            advance(input)
        }
        val text = input.substring(start, current)
        val token = when (text) {
            "true" -> BoolValue(true)
            "false" -> BoolValue(false)
            else -> keywords[text] ?: IDENTIFIER(text)
        }
        tokens.add(Pair(token, line))
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return c.isLetterOrDigit() || c == '_'
    }

    private fun number(input: String, tokens: MutableList<Pair<Token, Int>>) {
        while (peek(input).isDigit()) {
            advance(input)
        }

        if (peek(input) == '.' && peekNext(input).isDigit()) {
            advance(input)
            while (peek(input).isDigit()) {
                advance(input)
            }
        }

        val value = input.substring(start, current).toDouble()
        tokens.add(Pair(NumValue(value), line))
    }

    private fun string(input: String, tokens: MutableList<Pair<Token, Int>>) {
        while (peek(input) != '"' && !isAtEnd(input)) {
            advance(input)
        }
        if (isAtEnd(input)) {
            throw RuntimeException("Unterminated string")
        }
        advance(input)
        val value = input.substring(start + 1, current - 1)
        tokens.add(Pair(StrValue(value), line))
    }

    private fun advance(input: String): Char {
        current++
        return input[current - 1]
    }

    private fun peek(input: String): Char {
        return if (isAtEnd(input)) '\u0000' else input[current]
    }

    private fun peekNext(input: String): Char {
        return if (current + 1 >= input.length) '\u0000' else input[current + 1]
    }

    private fun isAtEnd(input: String): Boolean {
        return current >= input.length
    }
}
