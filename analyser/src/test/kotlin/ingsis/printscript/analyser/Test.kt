package ingsis.printscript.analyser

import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test

class Test {

    @Test
    fun test1() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.LIMIT_PRINTLN))
        val messages = analyser.analyse("print(3+4);")
        assert(messages[0] == "Print can only be invoked with a variable or literal")
    }

    @Test
    fun test2() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.LIMIT_PRINTLN))
        val messages = analyser.analyse("print(3);")
        assert(messages.isEmpty())
    }

    @Test
    fun test3() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.CAMEL_CASE))
        val messages = analyser.analyse("let some_number: Number = 1;")
        assert(messages[0] == "Variable name with camel case required")
    }

    @Test
    fun test4() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.CAMEL_CASE))
        val messages = analyser.analyse("let someNumber: Number = 1;")
        assert(messages.isEmpty())
    }

    @Test
    fun test5() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE))
        val messages = analyser.analyse("let someNumber: Number = 1;")
        assert(messages[0] == "Variable name with snake case required")
    }

    @Test
    fun test6() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE))
        val messages = analyser.analyse("let some_number: Number = 1;")
        assert(messages.isEmpty())
    }

    @Test
    fun test7() {
        val analyser = Analyser.Factory.getDefault(listOf(Configs.SNAKE_CASE, Configs.LIMIT_PRINTLN))
        val messages = analyser.analyse(listOf("print(3+4);", "let someNumber: Number = 1;"))
        assert(messages.size == 2)
        assert(messages[0] == "Print can only be invoked with a variable or literal")
        assert(messages[1] == "Variable name with snake case required")
    }

    @Test
    fun test8() {
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
        val messages = analyser.analyseTree(tree2)
        assert(messages.size == 4)
        assert(messages[0] == "Print can only be invoked with a variable or literal")
        assert(messages[1] == "Print can only be invoked with a variable or literal")
        assert(messages[2] == "Print can only be invoked with a variable or literal")
        assert(messages[3] == "Print can only be invoked with a variable or literal")
    }

    @Test
    fun test9() {
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
        val messages = analyser.analyseTree(tree2)
        assert(messages.size == 6)
        assert(messages[0] == "Print can only be invoked with a variable or literal")
        assert(messages[1] == "Print can only be invoked with a variable or literal")
        assert(messages[2] == "Print can only be invoked with a variable or literal")
        assert(messages[3] == "Print can only be invoked with a variable or literal")
        assert(messages[4] == "Print can only be invoked with a variable or literal")
        assert(messages[5] == "Print can only be invoked with a variable or literal")
    }
}
