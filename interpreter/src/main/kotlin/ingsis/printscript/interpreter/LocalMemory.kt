package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.Value


class LocalMemory(private val memory: MutableMap<String, Value>) {

    fun getValue(key: String): Value {
        return memory[key] ?: throw Error("Variable " + key + "not found")
    }

    fun put(key: String, value: Value) = run { memory[key] = value }
}
