package ingsis.printscript.lexer

import ingsis.printscript.utilities.enums.*

class Lexer(private val input: String) {
    private var position = 0
    private var currentChar: Char? = null

    init {
        advance()
    }

    private fun advance() {
        if (position < input.length) {
            currentChar = input[position]
            position++
        } else {
            currentChar = null
        }
    }

    private fun skipWhitespace() {
        while (currentChar?.isWhitespace() == true) {
            advance()
        }
    }

    private fun parseIdentifierOrKeyword(): Token {
        val startPos = position - 1
        while (currentChar?.let { it.isLetterOrDigit() || it == '_' } == true) {
            advance()
        }
        return when (val text = input.substring(startPos, position - 1)) {
            "let" -> LET
            "const" -> CONST
            "if" -> IF
            "else" -> ELSE
            "print" -> PRINT
            "string" -> STR
            "number" -> NUM
            else -> IDENTIFIER(text)
        }
    }

    private fun parseString(): Token {
        val startPos = position
        while (currentChar != null && currentChar != '"') {
            advance()
        }
        if (currentChar == null) {
            throw Exception("Unterminated string")
        }
        val text = input.substring(startPos, position)
        advance()
        return StrValue(text)
    }

    private fun parseNumber(): Token {
        val startPos = position - 1
        while (currentChar?.isDigit() == true) {
            advance()
        }
        if (currentChar == '.') {
            advance()
            while (currentChar?.isDigit() == true) {
                advance()
            }
        }
        val text = input.substring(startPos, position - 1)
        val value = text.toDoubleOrNull() ?: throw Exception("Invalid number format")
        return NumValue(value)
    }

    fun getNextToken(): Token {
        skipWhitespace()

//        if (currentChar == null) {
//            // end of file
//            return Token(TokenType.EOF, "")
//        }

        return when (currentChar) {
            ';' -> SEMICOLON.also { advance() }
            ':' -> COLON.also { advance() }
            '=' -> ASSIGNATION.also { advance() }
            '(' -> LEFT_PAREN.also { advance() }
            ')' -> RIGHT_PAREN.also { advance() }
            '-' -> SUB.also { advance() }
            '+' -> ADD.also { advance() }
            '*' -> MUL.also { advance() }
            '/' -> DIV.also { advance() }
//            '>' -> {
//                advance()
//                if
//                    (currentChar == '=') {
//                    Token(TokenType.GREATER_EQUAL, ">=").also { advance() }
//                } else {
//                    Token(TokenType.GREATER, ">")
//                }
//            }

//            '<' -> {
//                advance()
//                if (currentChar == '=') {
//                    Token(TokenType.LESS_EQUAL, "<=").also { advance() }
//                } else {
//                    Token(TokenType.LESS, "<")
//                }
//            }

            '"' -> parseString()
            in '0'..'9' -> parseNumber()
            in 'a'..'z', in 'A'..'Z', '_' -> parseIdentifierOrKeyword()
            else -> throw Exception("Invalid character: $currentChar")
        }
    }
}
