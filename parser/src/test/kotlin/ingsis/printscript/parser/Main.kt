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
                "name" to 0,
                STR to 0,
                true,
            ),
            LiteralAST(
                StrValue("Fede") to 0,
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList.map { it to 0 }) }
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
                "sum" to 0,
                NUM to 0,
                false,
            ),
            BinaryOperationAST(
                LiteralAST(NumValue(1.0) to 0),
                LiteralAST(NumValue(3.0) to 0),
                ADD to 0,
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList.map{it to 0}) }
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
            PRINT to 0,
            BinaryOperationAST(
                BinaryOperationAST(
                    LiteralAST(NumValue(2.0) to 0),
                    LiteralAST(NumValue(1.0) to 0),
                    SUB to 0,
                ),
                BinaryOperationAST(
                    LiteralAST(NumValue(3.0) to 0),
                    LiteralAST(NumValue(1.0) to 0),
                    SUB to 0,
                ),
                SUB to 0,
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList.map { it to 0 }) }
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
            "name" to 0,
            LiteralAST(
                StrValue("Fede") to 0,
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList.map { it to 0 }) }
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
            condition = LiteralAST(BoolValue(true) to 0),
            truthBlock = listOf(
                UnaryOperationAST(
                    PRINT to 0,
                    LiteralAST(
                        StrValue("Hello") to 0,
                    ),
                ),
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList.map { it to 0 }) }
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
            condition = LiteralAST(BoolValue(true) to 0),
            truthBlock = listOf(
                UnaryOperationAST(
                    PRINT to 0,
                    LiteralAST(
                        StrValue("Hello") to 0,
                    ),
                ),
            ),
            falseBlock = listOf(
                UnaryOperationAST(
                    PRINT to 0,
                    LiteralAST(
                        StrValue("Bye") to 0,
                    ),
                ),
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList.map { it to 0 }) }
    }

    @Test
    fun normalExpressionWithReadInputShouldReturnTree() {
        val tokenList = listOf(
            LET,
            IDENTIFIER("name"),
            COLON,
            STR,
            ASSIGNATION,
            READINPUT,
            LEFT_PAREN,
            StrValue(""),
            RIGHT_PAREN,
            SEMICOLON,
        )

        val expectedTree = AssignationAST(
            DeclarationAST(
                "name" to 0,
                STR to 0,
                true,
            ),
            UnaryOperationAST(
                READINPUT to 0,
                LiteralAST(StrValue("") to 0),
            ),
        )
        assertTrue { expectedTree == parser.parse(tokenList.map { it to 0 }) }
    }
}
