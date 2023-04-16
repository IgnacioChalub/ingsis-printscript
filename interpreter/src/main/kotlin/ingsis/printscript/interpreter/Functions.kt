package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports
import java.lang.Error

sealed interface PrintFunction {
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

sealed interface ReadInputFunction {
    fun read(message: String, type: Type): Value
}

object ReadInputFunctionImpl : ReadInputFunction {
    override fun read(message: String, type: Type): Value {
        print(message)
        val input = readLine()
        return when (type) {
            BOOL -> {
                val boolInput = input?.toBooleanStrictOrNull() ?: throw Error("Value provided is not a boolean")
                BoolValue(boolInput)
            }
            NUM -> {
                val numInput = input?.toDoubleOrNull() ?: throw Error("Value provided is not a number")
                NumValue(numInput)
            }
            STR -> {
                if (input == null) throw Error("Value provided is not a string")
                StrValue(input)
            }
        }
    }
}
