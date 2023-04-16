package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.Value
import java.util.Optional

class LocalMemory(
    private val memory: MutableMap<String, Pair<Value, Boolean>>,
    private val fatherMemory: Optional<LocalMemory>,
) {

    fun getValue(key: String): Value {
        while (fatherMemory.isPresent) {
            return if (memory.containsKey(key)) {
                memory[key]!!.first
            } else {
                fatherMemory.get().getValue(key)
            }
        }
        if (fatherMemory.isEmpty) {
            return if (memory.containsKey(key)) {
                memory[key]!!.first
            } else {
                throw Error("Variable " + key + "not found")
            }
        }
        throw Error("Variable " + key + "not found")
    }

    fun put(key: String, value: Value, isMutable: Boolean) {
        if (!keyIsUsed(key)) {
            memory[key] = Pair(value, isMutable)
        } else {
            replaceVariable(key, value)
        }
    }

    private fun replaceVariable(key: String, value: Value) {
        while (fatherMemory.isPresent) {
            if (memory.containsKey(key)) {
                val isMutable = memory[key]?.second
                if (isMutable == false) throw Error("Can not change immutable variable")
                memory[key] = Pair(value, true)
            } else {
                fatherMemory.get().keyIsUsed(key)
            }
        }
        if (fatherMemory.isEmpty) {
            if (memory.containsKey(key)) {
                val isMutable = memory[key]?.second
                if (isMutable == false) throw Error("Can not change immutable variable")
                memory[key] = Pair(value, true)
            }
        }
    }

    private fun keyIsUsed(key: String): Boolean {
        while (fatherMemory.isPresent) {
            return if (memory.containsKey(key)) {
                true
            } else {
                fatherMemory.get().keyIsUsed(key)
            }
        }
        if (fatherMemory.isEmpty) {
            return memory.containsKey(key)
        }
        return false
    }

    fun getNewChildMemory(): LocalMemory {
        return LocalMemory(
            mutableMapOf(),
            Optional.of(this),
        )
    }
}
