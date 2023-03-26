package ingsis.printscript.utilities.enums

enum class TokenType {
    SEMICOLON,
    COLON,
    ASSIGNATION,
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_CURLY_BRACES,
    RIGHT_CURLY_BRACES,
    MINUS,
    PLUS,
    MULTIPLY,
    DIVIDE,
    GREATER_EQUAL,
    GREATER,
    LESS_EQUAL,
    LESS,
    IDENTIFIER,
    STRING,
    NUMBER,
    PRINT,
    LET,
    CONST,
    IF,
    ELSE,
    NUMBER_TYPE,
    STRING_TYPE,
    EOF,

    // Added for parser
    TYPE, // STRING, NUMBER
    FUNCTION, // FUNCTION VAL
    LITERAL,
    OPERATOR,
}
