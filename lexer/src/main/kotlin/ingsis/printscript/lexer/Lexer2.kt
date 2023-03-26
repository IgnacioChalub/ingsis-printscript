package ingsis.printscript.lexer

import Token
import TokenType

class Lexer2(private val input: String) {
    private var position = 0
    private var currentChar: Char? = null

    // por tip de Tomi mejor usar mapa para hacerlo extensible a futura agregacion de nuevo token
    private val tokenMap: Map<Char, () -> Token> = mapOf(
        ';' to { Token(TokenType.SEMICOLON, ";").also { advance() } },
        ':' to { Token(TokenType.COLON, ":").also { advance() } },
        '=' to { Token(TokenType.ASSIGNATION, "=").also { advance() } },
        '(' to { Token(TokenType.LEFT_PAREN, "(").also { advance() } },
        ')' to { Token(TokenType.RIGHT_PAREN, ")").also { advance() } },
        '{' to { Token(TokenType.LEFT_CURLY_BRACES, "{").also { advance() } },
        '}' to { Token(TokenType.RIGHT_CURLY_BRACES, "}").also { advance() } },
        '-' to { Token(TokenType.MINUS, "-").also { advance() } },
        '+' to { Token(TokenType.PLUS, "+").also { advance() } },
        '*' to { Token(TokenType.MULTIPLY, "*").also { advance() } },
        '/' to { Token(TokenType.DIVIDE, "/").also { advance() } },
    )

    // lo mismo
    private val keywordMap: Map<String, TokenType> = mapOf(
        "let" to TokenType.LET,
        "const" to TokenType.CONST,
        "if" to TokenType.IF,
        "else" to TokenType.ELSE,
        "print" to TokenType.PRINT,
        "string" to TokenType.STRING_TYPE,
        "number" to TokenType.STRING_TYPE,
    )

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
        val text = input.substring(startPos, position)
        val tokenType = keywordMap[text] ?: TokenType.IDENTIFIER
        return Token(tokenType, text)
    }

    private fun parseString(): Token {
        advance() // Move past the opening double quote
        val startPos = position
        while (currentChar != null && currentChar != '"') {
            advance()
        }
        if (currentChar == null) {
            throw Exception("Unterminated string")
        }
        val text = input.substring(startPos, position)
        advance() // Move past the closing double quote
        return Token(TokenType.STRING, text)
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
        val text = input.substring(startPos, position)
        val value = text.toDoubleOrNull() ?: throw Exception("Invalid number format")
        return Token(TokenType.NUMBER_TYPE, value)
    }

    fun getNextToken(): Token {
        skipWhitespace()

        if (currentChar == null) {
            // end of file
            return Token(TokenType.EOF, "")
        }

        tokenMap[currentChar]?.let { return it() }

        return when (currentChar) {
            '>' -> {
                advance()
                if (currentChar == '=') {
                    Token(TokenType.GREATER_EQUAL, ">=").also { advance() }
                } else {
                    Token(TokenType.GREATER, ">")
                }
            }
            '<' -> {
                advance()
                if (currentChar == '=') {
                    Token(TokenType.LESS_EQUAL, "<=").also { advance() }
                } else {
                    Token(TokenType.LESS, "<")
                }
            }
            '"' -> parseString()
            in '0'..'9' -> parseNumber()
            in 'a'..'z', in 'A'..'Z', '_' -> parseIdentifierOrKeyword()
            else -> throw Exception("Invalid character: $currentChar")
        }
    }
}
