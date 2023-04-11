package ingsis.printscript.lexer // package ingsis.printscript.lexer
//
// import ingsis.printscript.utilities.enums.ingsis.printscript.lexer.TokenType
// import ingsis.printscript.utilities.types.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token
//
// class Lexer2(private val input: String) {
//    private var position = 0
//    private var currentChar: Char? = null
//
//    // por tip de Tomi mejor usar mapa para hacerlo extensible a futura agregacion de nuevo token
//    private val tokenMap: Map<Char, () -> ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token> = mapOf(
//        ';' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.SEMICOLON, ";").also { advance() } },
//        ':' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.COLON, ":").also { advance() } },
//        '=' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.ASSIGNATION, "=").also { advance() } },
//        '(' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.LEFT_PAREN, "(").also { advance() } },
//        ')' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.RIGHT_PAREN, ")").also { advance() } },
//        '{' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.LEFT_CURLY_BRACES, "{").also { advance() } },
//        '}' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.RIGHT_CURLY_BRACES, "}").also { advance() } },
//        '-' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.MINUS, "-").also { advance() } },
//        '+' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.PLUS, "+").also { advance() } },
//        '*' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.MULTIPLY, "*").also { advance() } },
//        '/' to { ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.DIVIDE, "/").also { advance() } },
//    )
//
//    // lo mismo
//    private val keywordMap: Map<String, ingsis.printscript.lexer.TokenType> = mapOf(
//        "let" to ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.LET,
//        "const" to ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.CONST,
//        "if" to ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.IF,
//        "else" to ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.ELSE,
//        "print" to ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.PRINT,
//        "string" to ingsis.printscript.lexer.TokenType.STRING_TYPE,
//        "number" to ingsis.printscript.lexer.TokenType.STRING_TYPE,
//    )
//
//    init {
//        advance()
//    }
//
//    private fun advance() {
//        if (position < input.length) {
//            currentChar = input[position]
//            position++
//        } else {
//            currentChar = null
//        }
//    }
//
//    private fun skipWhitespace() {
//        while (currentChar?.isWhitespace() == true) {
//            advance()
//        }
//    }
//
//    private fun parseIdentifierOrKeyword(): ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token {
//        val startPos = position - 1
//        while (currentChar?.let { it.isLetterOrDigit() || it == '_' } == true) {
//            advance()
//        }
//        val text = input.substring(startPos, position)
//        val tokenType = keywordMap[text] ?: ingsis.printscript.lexer.TokenType.ingsis.printscript.lexer.ingsis.printscript.utilities.enums.IDENTIFIER
//        return ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(tokenType, text)
//    }
//
//    private fun parseString(): ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token {
//        advance() // Move past the opening double quote
//        val startPos = position
//        while (currentChar != null && currentChar != '"') {
//            advance()
//        }
//        if (currentChar == null) {
//            throw Exception("Unterminated string")
//        }
//        val text = input.substring(startPos, position)
//        advance() // Move past the closing double quote
//        return ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.STRING, text)
//    }
//
//    private fun parseNumber(): ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token {
//        val startPos = position - 1
//        while (currentChar?.isDigit() == true) {
//            advance()
//        }
//        if (currentChar == '.') {
//            advance()
//            while (currentChar?.isDigit() == true) {
//                advance()
//            }
//        }
//        val text = input.substring(startPos, position)
//        val value = text.toDoubleOrNull() ?: throw Exception("Invalid number format")
//        return ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.NUMBER_TYPE, value)
//    }
//
//    fun getNextToken(): ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token {
//        skipWhitespace()
//
//        if (currentChar == null) {
//            // end of file
//            return ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.EOF, "")
//        }
//
//        tokenMap[currentChar]?.let { return it() }
//
//        return when (currentChar) {
//            '>' -> {
//                advance()
//                if (currentChar == '=') {
//                    ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.GREATER_EQUAL, ">=").also { advance() }
//                } else {
//                    ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.GREATER, ">")
//                }
//            }
//            '<' -> {
//                advance()
//                if (currentChar == '=') {
//                    ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.LESS_EQUAL, "<=").also { advance() }
//                } else {
//                    ingsis.printscript.lexer.ingsis.printscript.utilities.enums.Token(ingsis.printscript.lexer.TokenType.LESS, "<")
//                }
//            }
//            '"' -> parseString()
//            in '0'..'9' -> parseNumber()
//            in 'a'..'z', in 'A'..'Z', '_' -> parseIdentifierOrKeyword()
//            else -> throw Exception("Invalid character: $currentChar")
//        }
//    }
// }
