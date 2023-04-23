package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.StrValue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class LocalMemoryTest {

    @Test()
    fun shouldThrowErrorIsVariableNotFound() {
        val memory = LocalMemory(mutableMapOf(), Optional.empty())
        assertThrows<Error> {
            memory.getValue("some value")
        }
    }

    @Test()
    fun shouldGetValueIfExists() {
        val memory = LocalMemory(mutableMapOf(Pair("some value", Pair(StrValue("value"), true))), Optional.empty())
        assert((memory.getValue("some value") as StrValue).value === "value")
    }

    @Test
    fun shouldReplaceValueIfExists() {
        val memory = LocalMemory(mutableMapOf(Pair("some value", Pair(StrValue("value"), true))), Optional.empty())
        memory.put("some value", StrValue("new value"), true)
        assert((memory.getValue("some value") as StrValue).value === "new value")
    }

    @Test
    fun shouldThrowErrorIfVariableIsImmutable() {
        val memory = LocalMemory(mutableMapOf(Pair("some value", Pair(StrValue("value"), false))), Optional.empty())
        val err = assertThrows<Error> {
            memory.replaceVariable("some value", StrValue("new value"))
        }
        assert(err.message === "Can not change immutable variable")
    }

    @Test
    fun returnTrueIfKeyIsUsedInFather() {
        val memory = LocalMemory(
            mutableMapOf(Pair("some value", Pair(StrValue("value"), false))),
            Optional.of(
                LocalMemory(
                    mutableMapOf(Pair("used", Pair(StrValue("value"), false))),
                    Optional.empty(),
                ),
            ),
        )
        assert(memory.keyIsUsed("used") === true)
    }
}
