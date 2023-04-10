
sealed interface Token

sealed interface TokenType : Token

object LET : TokenType
object CONST : TokenType
object IF : TokenType
object ELSE : TokenType
object COLON : TokenType
object SEMICOLON : TokenType
object ASSIGNATION : TokenType

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

sealed interface Operation : Token

object LEFT_PAREN : Operation
object RIGHT_PAREN : Operation
object ADD : Operation
object SUB : Operation
object DIV : Operation
object MUL : Operation

sealed interface Type : Token
object NUM : Type
object STR : Type

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
}

sealed interface Function : Token
object PRINT : Function
