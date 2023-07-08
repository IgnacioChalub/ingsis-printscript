package ingsis.printscript.analyser

import ingsis.printscript.utilities.enums.*
import ingsis.printscript.utilities.visitor.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Test {

    @Test
    fun shouldValidateLimitPrint() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.LIMIT_PRINTLN))
        val tree = UnaryOperationAST(
            PRINT,
            BinaryOperationAST(
                LiteralAST(NumValue(3.0)),
                LiteralAST(NumValue(4.0)),
                ADD,
            ),
        )
        val messages = analyser.analyse(tree)
        assert(messages[0] == "Print can only be invoked with a variable or literal")
    }

    @Test
    fun shouldPrintLiteral() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.LIMIT_PRINTLN))
        val tree = UnaryOperationAST(
            PRINT,
            LiteralAST(NumValue(3.0)),
        )
        val messages = analyser.analyse(tree)
        assert(messages.isEmpty())
    }

    @Test
    fun shouldRequireCamelCase() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.CAMEL_CASE))
        val tree = AssignationAST(
            DeclarationAST(
                "some_number",
                NUM,
                true,
            ),
            LiteralAST(NumValue(1.0)),
        )
        val messages = analyser.analyse(tree)
        assert(messages[0] == "Variable name with camel case required")
    }

    @Test
    fun shouldNotRequireCamelCase() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.CAMEL_CASE))
        val tree = AssignationAST(
                DeclarationAST(
                        "some",
                        NUM,
                        true,
                ),
                LiteralAST(NumValue(1.0)),
        )
        val messages = analyser.analyse(tree)
        assert(messages.isEmpty())
    }

    @Test
    fun shouldNotRequireSnakeCase() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE))
        val tree = AssignationAST(
                DeclarationAST(
                        "some",
                        NUM,
                        true,
                ),
                LiteralAST(NumValue(1.0)),
        )
        val messages = analyser.analyse(tree)
        assert(messages.isEmpty())
    }

    @Test
    fun shouldAcceptCamelCase() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.CAMEL_CASE))
        val tree = AssignationAST(
            DeclarationAST(
                "someNumber",
                NUM,
                true,
            ),
            LiteralAST(NumValue(1.0)),
        )
        val messages = analyser.analyse(tree)
        assert(messages.isEmpty())
    }

    @Test
    fun shouldRequireSnakeCase() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE))
        val tree = AssignationAST(
            DeclarationAST(
                "someNumber",
                NUM,
                true,
            ),
            LiteralAST(NumValue(1.0)),
        )
        val messages = analyser.analyse(tree)
        assert(messages[0] == "Variable name with snake case required")
    }

    @Test
    fun snakeCaseShouldBeAllowed() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE))
        val tree = AssignationAST(
            DeclarationAST(
                "some_number",
                NUM,
                true,
            ),
            LiteralAST(NumValue(1.0)),
        )
        val messages = analyser.analyse(tree)
        assert(messages.isEmpty())
    }

    @Test
    fun shouldValidateSnakeCaseAndLimitPrintln() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE, Configs.LIMIT_PRINTLN))
        val tree1 = UnaryOperationAST(
            PRINT,
            BinaryOperationAST(
                LiteralAST(NumValue(3.0)),
                LiteralAST(NumValue(4.0)),
                ADD,
            ),
        )
        val tree2 = AssignationAST(
            DeclarationAST(
                "someNumber",
                NUM,
                true,
            ),
            LiteralAST(NumValue(1.0)),
        )
        val messages = analyser.analyse(listOf(tree1, tree2))
        assert(messages.size == 2)
        assert(messages[0] == "Print can only be invoked with a variable or literal")
        assert(messages[1] == "Variable name with snake case required")
    }

    @Test
    fun shouldLimitPrintInsideIf() {
        val tree = IfAST(
            LiteralAST(BoolValue(true)),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    BinaryOperationAST(LiteralAST(NumValue(1.0)), LiteralAST(NumValue(1.0)), MUL),
                ),
                UnaryOperationAST(
                    PRINT,
                    BinaryOperationAST(LiteralAST(NumValue(3.0)), LiteralAST(NumValue(2.0)), SUB),
                ),
            ),
        )
        val tree2 = IfAST(
            LiteralAST(BoolValue(true)),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    BinaryOperationAST(LiteralAST(NumValue(1.0)), LiteralAST(NumValue(1.0)), MUL),
                ),
                UnaryOperationAST(
                    PRINT,
                    BinaryOperationAST(LiteralAST(NumValue(3.0)), LiteralAST(NumValue(2.0)), SUB),
                ),
                tree,
            ),
        )
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE, Configs.LIMIT_PRINTLN))
        val messages = analyser.analyse(tree2)
        assert(messages.size == 4)
        assert(messages[0] == "Print can only be invoked with a variable or literal")
        assert(messages[1] == "Print can only be invoked with a variable or literal")
        assert(messages[2] == "Print can only be invoked with a variable or literal")
        assert(messages[3] == "Print can only be invoked with a variable or literal")
    }

    @Test
    fun shouldLimitPrintInsideIfElse() {
        val tree = IfElseAST(
            LiteralAST(BoolValue(true)),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    BinaryOperationAST(LiteralAST(NumValue(1.0)), LiteralAST(NumValue(1.0)), MUL),
                ),
                UnaryOperationAST(
                    PRINT,
                    BinaryOperationAST(LiteralAST(NumValue(3.0)), LiteralAST(NumValue(2.0)), SUB),
                ),
            ),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    BinaryOperationAST(LiteralAST(NumValue(1.0)), LiteralAST(NumValue(1.0)), MUL),
                ),
                UnaryOperationAST(
                    PRINT,
                    BinaryOperationAST(LiteralAST(NumValue(3.0)), LiteralAST(NumValue(2.0)), SUB),
                ),
            ),
        )
        val tree2 = IfAST(
            LiteralAST(BoolValue(true)),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    BinaryOperationAST(LiteralAST(NumValue(1.0)), LiteralAST(NumValue(1.0)), MUL),
                ),
                UnaryOperationAST(
                    PRINT,
                    BinaryOperationAST(LiteralAST(NumValue(3.0)), LiteralAST(NumValue(2.0)), SUB),
                ),
                tree,
            ),
        )
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE, Configs.LIMIT_PRINTLN))
        val messages = analyser.analyse(tree2)
        assert(messages.size == 6)
        assert(messages[0] == "Print can only be invoked with a variable or literal")
        assert(messages[1] == "Print can only be invoked with a variable or literal")
        assert(messages[2] == "Print can only be invoked with a variable or literal")
        assert(messages[3] == "Print can only be invoked with a variable or literal")
        assert(messages[4] == "Print can only be invoked with a variable or literal")
        assert(messages[5] == "Print can only be invoked with a variable or literal")
    }

    @Test()
    fun shouldAllowSnakeCaseAndPrint() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE, Configs.LIMIT_PRINTLN))
        val tree1 = AssignationAST(
            DeclarationAST(
                "some_number",
                NUM,
                true,
            ),
            LiteralAST(NumValue(1.0)),
        )
        val tree2 = UnaryOperationAST(
            PRINT,
            VariableAST("someNumber"),
        )
        val messages = analyser.analyse(listOf(tree1, tree2))
        assert(messages.isEmpty())
    }

    @Test()
    fun shouldTestLimitReadInput() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE, Configs.LIMIT_READ_INPUT))
        val tree1 = AssignationAST(
            DeclarationAST(
                "some_number",
                NUM,
                true,
            ),
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some message")),
            ),
        )
        val messages = analyser.analyse(listOf(tree1))
        assert(messages.isEmpty())
    }

    @Test()
    fun shouldValidateReadInputIsNotCalledWithExpression() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE, Configs.LIMIT_READ_INPUT))
        val tree1 = AssignationAST(
            DeclarationAST(
                "some_number",
                NUM,
                true,
            ),
            UnaryOperationAST(
                READINPUT,
                BinaryOperationAST(
                    LiteralAST(StrValue("Some ")),
                    LiteralAST(StrValue("value:")),
                    ADD,
                ),
            ),
        )
        val messages = analyser.analyse(listOf(tree1))
        assert(messages[0] === "Read input can only be invoked with a variable or literal")
    }

    @Test()
    fun shouldThrowErrorIfCreateAnalyserWithCamelCaseAndSnakeCaseRule() {
        val err = assertThrows<Error> { Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE, Configs.CAMEL_CASE)) }
        assert(err.message === "Variable names can not be snake case and camel case")
    }
}
