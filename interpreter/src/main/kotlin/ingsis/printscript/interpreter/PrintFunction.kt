package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.NumValue
import ingsis.printscript.utilities.enums.StrValue
import ingsis.printscript.utilities.enums.Value


sealed interface PrintFunction {
    fun print(value: Value)
}

object PrintFunctionImpl : PrintFunction {
    override fun print(value: Value) {
        when (value) {
            is StrValue -> println(value.value)
            is NumValue -> println(value.value)
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
        }
    }
}
