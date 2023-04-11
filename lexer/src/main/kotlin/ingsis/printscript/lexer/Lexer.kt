package ingsis.printscript.lexer

import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports

class Lexer(private val input: String) {
    private val tokens = mutableListOf<Token>()
    private var start = 0
    private var current = 0

    private val keywords = mapOf(
        "let" to LET,
        "const" to CONST,
        "if" to IF,
        "else" to ELSE,
        "num" to NUM,
        "str" to STR,
        "print" to PRINT
    )

    fun tokenize(): List<Token> {
        while (!isAtEnd()) {
            start = current
            val c = advance()
            when (c) {
                '(' -> tokens.add(LEFT_PAREN)
                ')' -> tokens.add(RIGHT_PAREN)
                '+' -> tokens.add(ADD)
                '-' -> tokens.add(SUB)
                '*' -> tokens.add(MUL)
                '/' -> tokens.add(DIV)
                ';' -> tokens.add(SEMICOLON)
                ':' -> tokens.add(COLON)
                '=' -> tokens.add(ASSIGNATION)
                in '0'..'9' -> number()
                in 'a'..'z', in 'A'..'Z', '_' -> identifier()
                '"' -> string()
                // ... other cases ...
            }
        }
        return tokens
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) {
            advance()
        }

        val text = input.substring(start, current)
        val token = keywords[text] ?: IDENTIFIER(text)
        tokens.add(token)
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return c.isLetterOrDigit() || c == '_'
    }

    private fun number() {
        while (peek().isDigit()) {
            advance()
        }

        if (peek() == '.' && peekNext().isDigit()) {
            advance()
            while (peek().isDigit()) {
                advance()
            }
        }

        val value = input.substring(start, current).toDouble()
        tokens.add(NumValue(value))
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            advance()
        }

        if (isAtEnd()) {
            throw RuntimeException("Unterminated string")
        }

        advance()
        val value = input.substring(start + 1, current - 1)
        tokens.add(StrValue(value))
    }

    private fun advance(): Char {
        current++
        return input[current - 1]
    }

    private fun peek(): Char {
        return if (isAtEnd()) '\u0000' else input[current]
    }

    private fun peekNext(): Char {
        return if (current + 1 >= input.length) '\u0000' else input[current + 1]
    }

    private fun isAtEnd(): Boolean {
        return current >= input.length
    }
}
