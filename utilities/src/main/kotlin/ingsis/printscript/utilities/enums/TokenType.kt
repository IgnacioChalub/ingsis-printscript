package ingsis.printscript.utilities.enums

sealed interface Token

object LET : Token
object CONST : Token
object IF : Token
object ELSE : Token
object COLON : Token
object SEMICOLON : Token
object ASSIGNATION : Token

sealed interface UtilToken : Token
data class IDENTIFIER(val value: String) : UtilToken {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as IDENTIFIER

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

sealed interface Operation : Token {
    override fun toString(): String
}

object LEFT_PAREN : Operation {
    override fun toString(): String = "("
}

object RIGHT_PAREN : Operation {
    override fun toString(): String = ")"
}

object LEFT_CURLY_BRACES : Operation {
    override fun toString(): String = "{"
}

object RIGHT_CURLY_BRACES : Operation {
    override fun toString(): String = "}"
}

object ADD : Operation {
    override fun toString(): String = "+"
}

object SUB : Operation {
    override fun toString(): String = "-"
}

object DIV : Operation {
    override fun toString(): String = "/"
}

object MUL : Operation {
    override fun toString(): String = "*"
}

sealed interface Type : Token {
    override fun toString(): String
}

object NUM : Type {
    override fun toString(): String {
        return "NUM"
    }
}

object STR : Type {
    override fun toString(): String {
        return "STR"
    }
}

object BOOL : Type {
    override fun toString(): String {
        return "BOOL"
    }
}

sealed interface Value : Token
data class StrValue(val value: String) : Value {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as StrValue

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value
    }
}

data class NumValue(val value: Double) : Value {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as NumValue

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value.toString()
    }
}

data class BoolValue(val value: Boolean) : Value {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BoolValue

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value.toString()
    }
}

sealed interface Function : Token
object PRINT : Function {
    override fun toString(): String = "PRINT"
}

object READINPUT : Function


class EmptyValue : Value {
    override fun toString(): String {
        return ""
    }
}
