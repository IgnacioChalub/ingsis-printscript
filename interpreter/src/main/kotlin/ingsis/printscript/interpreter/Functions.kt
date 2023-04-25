package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.*

interface PrintFunction {
    fun print(value: Value)
}

object PrintFunctionImpl : PrintFunction {
    override fun print(value: Value) {
        when (value) {
            is StrValue -> println(value.value)
            is NumValue -> println(value.value)
            is BoolValue -> println(value.value)
        }
    }
}

class PrintFunctionMock(
    var printedValue: String,
) : PrintFunction {
    override fun print(value: Value) {
        printedValue = when (value) {
            is StrValue -> value.value
            is NumValue -> value.value.toString()
            is BoolValue -> value.value.toString()
        }
    }
}

class PrintManyFunctionMock(
    var printedValues: MutableList<String>,
) : PrintFunction {
    override fun print(value: Value) {
        val printedValue = when (value) {
            is StrValue -> value.value
            is NumValue -> value.value.toString()
            is BoolValue -> value.value.toString()
        }
        printedValues.add(printedValue)
    }
}

interface ReadInputFunction {
    fun read(message: String): String
}

object ReadInputFunctionImpl : ReadInputFunction {
    override fun read(message: String): String {
        print(message)
        return readLine() ?: ""
    }
}

object ReadStringFunctionMock : ReadInputFunction {
    override fun read(message: String): String {
        return "Some string"
    }
}

object ReadBoolFunctionMock : ReadInputFunction {
    override fun read(message: String): String {
        return "true"
    }
}

object ReadNumFunctionMock : ReadInputFunction {
    override fun read(message: String): String {
        return "1"
    }
}
