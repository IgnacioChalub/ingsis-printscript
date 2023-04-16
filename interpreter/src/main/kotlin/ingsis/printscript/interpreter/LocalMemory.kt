package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.Value
import java.util.Optional

class LocalMemory(
    private val memory: MutableMap<String, Pair<Value, Boolean>>,
    private val fatherMemory: Optional<LocalMemory>,
) {

    fun getValue(key: String): Value {
        if (memory.containsKey(key)) {
            return memory[key]!!.first
        }
        if(fatherMemory.isEmpty) {
            throw Error("Variable " + key + "not found")
        } else {
            return fatherMemory.get().getValue(key)
        }
    }

    fun put(key: String, value: Value, isMutable: Boolean) {
        if (!keyIsUsed(key)) {
            memory[key] = Pair(value, isMutable)
        } else {
            replaceVariable(key, value)
        }
    }

    fun replaceVariable(key: String, value: Value) {
        if (memory.containsKey(key)) {
            val isMutable = memory[key]?.second
            if (isMutable == false) throw Error("Can not change immutable variable")
            memory[key] = Pair(value, true)
            return
        }
        if(fatherMemory.isEmpty) {
            return
        } else {
            return fatherMemory.get().replaceVariable(key, value)
        }
    }

    private fun keyIsUsed(key: String): Boolean {
        return if(fatherMemory.isEmpty) {
            memory.containsKey(key)
        } else {
            fatherMemory.get().keyIsUsed(key)
        }
    }

    fun getNewChildMemory(): LocalMemory {
        return LocalMemory(
            mutableMapOf(),
            Optional.of(this),
        )
    }
}
