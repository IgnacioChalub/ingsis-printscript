package ingsis.printscript.analyser

import org.junit.jupiter.api.Test

class Test {

    @Test
    fun test1() {
        val analyser = Analyser.Factory.getDefault()
        val messages = analyser.analyse("print(3*2);", listOf(Configs.LIMIT_PRINTLN))
        assert(messages[0] == "Print can only be invoked with a variable or literal")
    }

    @Test
    fun test2() {
        val analyser = Analyser.Factory.getDefault()
        val messages = analyser.analyse("print(3);", listOf(Configs.LIMIT_PRINTLN))
        assert(messages.isEmpty())
    }

    @Test
    fun test3() {
        val analyser = Analyser.Factory.getDefault()
        val messages = analyser.analyse("let some_number: Number = 1;", listOf(Configs.CAMEL_CASE))
        assert(messages[0] == "Variable name with camel case required")
    }

    @Test
    fun test4() {
        val analyser = Analyser.Factory.getDefault()
        val messages = analyser.analyse("let someNumber: Number = 1;", listOf(Configs.CAMEL_CASE))
        assert(messages.isEmpty())
    }

    @Test
    fun test5() {
        val analyser = Analyser.Factory.getDefault()
        val messages = analyser.analyse("let someNumber: Number = 1;", listOf(Configs.SNAKE_CASE))
        assert(messages[0] == "Variable name with snake case required")
    }

    @Test
    fun test6() {
        val analyser = Analyser.Factory.getDefault()
        val messages = analyser.analyse("let some_number: Number = 1;", listOf(Configs.SNAKE_CASE))
        assert(messages.isEmpty())
    }
}
