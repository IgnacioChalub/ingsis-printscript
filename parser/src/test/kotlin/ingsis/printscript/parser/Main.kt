package ingsis.printscript.parser

import ingsis.printscript.parser.implementations.Parser
import ingsis.printscript.utilities.enums.ADD
import ingsis.printscript.utilities.enums.ASSIGNATION
import ingsis.printscript.utilities.enums.COLON
import ingsis.printscript.utilities.enums.IDENTIFIER
import ingsis.printscript.utilities.enums.LEFT_PAREN
import ingsis.printscript.utilities.enums.LET
import ingsis.printscript.utilities.enums.NUM
import ingsis.printscript.utilities.enums.NumValue
import ingsis.printscript.utilities.enums.PRINT
import ingsis.printscript.utilities.enums.RIGHT_PAREN
import ingsis.printscript.utilities.enums.SEMICOLON
import ingsis.printscript.utilities.enums.STR
import ingsis.printscript.utilities.enums.SUB
import ingsis.printscript.utilities.enums.StrValue
import ingsis.printscript.utilities.visitor.AssignationAST
import ingsis.printscript.utilities.visitor.BinaryOperationAST
import ingsis.printscript.utilities.visitor.DeclarationAST
import ingsis.printscript.utilities.visitor.LiteralAST
import ingsis.printscript.utilities.visitor.UnaryOperationAST
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Main {

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
            SEMICOLON,
        )

        val expectedTree = AssignationAST(
            DeclarationAST(
                "name",
                STR,
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
            LET,
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
            SEMICOLON,
        )

        val expectedTree =
            UnaryOperationAST(
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
}
