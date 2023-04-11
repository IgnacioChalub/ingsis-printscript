package ingsis.printscript.lexer

import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports

class Lexer() {

    private var start = 0
    private var current = 0

    private val keywords = mapOf(
        "let" to LET,
        "const" to CONST,
        "if" to IF,
        "else" to ELSE,
        "Number" to NUM,
        "String" to STR,
        "print" to PRINT,
    )

    fun tokenize(input: String): List<Token> {
        val tokens = mutableListOf<Token>()
        while (!isAtEnd(input)) {
            start = current
            val c = advance(input)
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
                in '0'..'9' -> number(input, tokens)
                in 'a'..'z', in 'A'..'Z', '_' -> identifier(input, tokens)
                '"' -> string(input, tokens)
            }
        }
        start = 0
        current = 0
        return tokens
    }

    private fun identifier(input: String, tokens: MutableList<Token>) {
        while (isAlphaNumeric(peek(input))) {
            advance(input)
        }
        val text = input.substring(start, current)
        val token = keywords[text] ?: IDENTIFIER(text)
        tokens.add(token)
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return c.isLetterOrDigit() || c == '_'
    }

    private fun number(input: String, tokens: MutableList<Token>) {
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
        tokens.add(NumValue(value))
    }

    private fun string(input: String, tokens: MutableList<Token>) {
        while (peek(input) != '"' && !isAtEnd(input)) {
            advance(input)
        }
        if (isAtEnd(input)) {
            throw RuntimeException("Unterminated string")
        }
        advance(input)
        val value = input.substring(start + 1, current - 1)
        tokens.add(StrValue(value))
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
