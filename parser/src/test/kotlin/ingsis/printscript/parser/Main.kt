package ingsis.printscript.parser

import ingsis.printscript.parser.implementations.Parser
import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Main {

    private val parser = Parser(Version.VERSION_1_1)

    @Test
    fun normalExpressionShouldReturnTree() {
        val tokenList = listOf(
            LET,
            IDENTIFIER("name"),
            COLON,
            STR,
            ASSIGNATION,
            StrValue("Fede"),
            SEMICOLON,
        )

        val expectedTree = AssignationAST(
            DeclarationAST(
                "name",
                STR,
                true
            ),
            LiteralAST(
                StrValue("Fede"),
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList) }
    }

    @Test
    fun sumExpressionShouldReturnTree() {
        val tokenList = listOf(
            CONST,
            IDENTIFIER("sum"),
            COLON,
            NUM,
            ASSIGNATION,
            NumValue(1.0),
            ADD,
            NumValue(3.0),
            SEMICOLON,
        )

        val expectedTree = AssignationAST(
            DeclarationAST(
                "sum",
                NUM,
                false
            ),
            BinaryOperationAST(
                LiteralAST(NumValue(1.0)),
                LiteralAST(NumValue(3.0)),
                ADD,
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList) }
    }

    @Test
    fun functionSubtExpressionShouldReturnTree() {
        val tokenList = listOf(
            PRINT,
            LEFT_PAREN,
            LEFT_PAREN,
            NumValue(2.0),
            SUB,
            NumValue(1.0),
            RIGHT_PAREN,
            SUB,
            LEFT_PAREN,
            NumValue(3.0),
            SUB,
            NumValue(1.0),
            RIGHT_PAREN,
            RIGHT_PAREN,
            SEMICOLON,
        )

        val expectedTree = UnaryOperationAST(
            PRINT,
            BinaryOperationAST(
                BinaryOperationAST(
                    LiteralAST(NumValue(2.0)),
                    LiteralAST(NumValue(1.0)),
                    SUB,
                ),
                BinaryOperationAST(
                    LiteralAST(NumValue(3.0)),
                    LiteralAST(NumValue(1.0)),
                    SUB,
                ),
                SUB,
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList) }
    }

    @Test
    fun reassignExpressionShouldReturnTree() {
        val tokenList = listOf(
            IDENTIFIER("name"),
            ASSIGNATION,
            StrValue("Fede"),
            SEMICOLON,
        )

        val expectedTree = ReAssignationAST(
            "name",
            LiteralAST(
                StrValue("Fede"),
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList) }
    }

    // if(true) { print("Hello"); }
    @Test
    fun ifExpressionShouldReturnTree() {
        val tokenList = listOf(
            IF,
            LEFT_PAREN,
            BoolValue(true),
            RIGHT_PAREN,
            LEFT_CURLY_BRACES,
            PRINT,
            LEFT_PAREN,
            StrValue("Hello"),
            RIGHT_PAREN,
            SEMICOLON,
            RIGHT_CURLY_BRACES,
            SEMICOLON,
        )

        val expectedTree = IfAST(
            condition = LiteralAST(BoolValue(true)),
            truthBlock = listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(
                        StrValue("Hello"),
                    ),
                ),
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList) }
    }

    // if(true) { print("Hello"); }
    @Test
    fun ifElseExpressionShouldReturnTree() {
        val tokenList = listOf(
            IF,
            LEFT_PAREN,
            BoolValue(true),
            RIGHT_PAREN,
            LEFT_CURLY_BRACES,
            PRINT,
            LEFT_PAREN,
            StrValue("Hello"),
            RIGHT_PAREN,
            SEMICOLON,
            RIGHT_CURLY_BRACES,
            ELSE,
            LEFT_CURLY_BRACES,
            PRINT,
            LEFT_PAREN,
            StrValue("Bye"),
            RIGHT_PAREN,
            SEMICOLON,
            RIGHT_CURLY_BRACES,
            SEMICOLON,
        )

        val expectedTree = IfElseAST(
            condition = LiteralAST(BoolValue(true)),
            truthBlock = listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(
                        StrValue("Hello"),
                    ),
                ),
            ),
            falseBlock = listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(
                        StrValue("Bye"),
                    ),
                ),
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList) }
    }
}
