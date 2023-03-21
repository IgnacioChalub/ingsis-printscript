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
        val text = input.substring(startPos, position - 1)
        return when (text) {
            "let" -> Token(TokenType.LET, text)
            "const" -> Token(TokenType.CONST, text)
            "if" -> Token(TokenType.IF, text)
            "else" -> Token(TokenType.ELSE, text)
            "print" -> Token(TokenType.PRINT, text)
            "string" -> Token(TokenType.STRING_TYPE, text)
            "number" -> Token(TokenType.NUMBER_TYPE, text)
            else -> Token(TokenType.IDENTIFIER, text)
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
        val text = input.substring(startPos, position - 1)
        val value = text.toDoubleOrNull() ?: throw Exception("Invalid number format")
        return Token(TokenType.NUMBER, value)
    }

    fun getNextToken(): Token {
        skipWhitespace()

        if (currentChar == null) {
            //end of file
            return Token(TokenType.EOF, "")
        }

        return when (currentChar) {
            ';' -> Token(TokenType.SEMICOLON, ";").also { advance() }
            ':' -> Token(TokenType.COLON, ":").also { advance() }
            '=' -> Token(TokenType.ASSIGNATION, "=").also { advance() }
            '(' -> Token(TokenType.LEFT_PAREN, "(").also { advance() }
            ')' -> Token(TokenType.RIGHT_PAREN, ")").also { advance() }
            '{' -> Token(TokenType.LEFT_CURLY_BRACES, "{").also { advance() }
            '}' -> Token(TokenType.RIGHT_CURLY_BRACES, "}").also { advance() }
            '-' -> Token(TokenType.MINUS, "-").also { advance() }
            '+' -> Token(TokenType.PLUS, "+").also { advance() }
            '*' -> Token(TokenType.MULTIPLY, "*").also { advance() }
            '/' -> Token(TokenType.DIVIDE, "/").also { advance() }
            '>' -> {
                advance()
                if
                        (currentChar == '=') {
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