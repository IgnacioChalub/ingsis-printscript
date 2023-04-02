package ingsis.printscript.parser

import ingsis.printscript.parser.implementations.Parser
import ingsis.printscript.utilities.enums.*
import ingsis.printscript.utilities.visitor.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MainTest {

    private val parser = Parser()

    @Test
    fun normalExpressionShouldReturnTree() {
        val tokenList = listOf(
            LET,
            IDENTIFIER("name"),
            COLON,
            STR,
            ASSIGNATION,
            StrValue("Fede"),
            SEMICOLON
        )

        val expectedTree = AssignationAST(
            DeclarationAST(
                "name",
                STR
            ),
            LiteralAST(
                StrValue("Fede")
            )
        )
        assertTrue { expectedTree == parser.parse(tokenList) }
    }

    @Test
    fun sumExpressionShouldReturnTree() {
        val tokenList = listOf(
            LET,
            IDENTIFIER("sum"),
            COLON,
            NUM,
            ASSIGNATION,
            NumValue(1.0),
            ADD,
            NumValue(3.0),
            SEMICOLON
        )

        val expectedTree = AssignationAST(
            DeclarationAST(
                "sum",
                NUM
            ),
            BinaryOperationAST(
                LiteralAST(NumValue(1.0)),
                LiteralAST(NumValue(3.0)),
                ADD
            )
        )
        assertTrue { expectedTree == parser.parse(tokenList) }
    }

    // println((2-1)-(3-1))
    @Test
    fun functionSubtExpressionShouldReturnTree() {
        val tokenList = listOf(
            PRINT,
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
            SEMICOLON
        )

        val expectedTree =
            UnaryOperationAST(
                PRINT,
                BinaryOperationAST(
                    BinaryOperationAST(
                        LiteralAST(NumValue(2.0)),
                        LiteralAST(NumValue(1.0)),
                        SUB
                    ),
                    BinaryOperationAST(
                        LiteralAST(NumValue(3.0)),
                        LiteralAST(NumValue(1.0)),
                        SUB
                    ),
                    SUB
                )
            )

        assertTrue { expectedTree == parser.parse(tokenList) }
    }

}
