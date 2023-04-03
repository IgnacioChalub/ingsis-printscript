package ingsis.printscript.utilities.enums

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
class IDENTIFIER(val value: String) : UtilToken

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
class StrValue(val value: String) : Value {
    override fun equals(other: Any?): Boolean {
        return other is StrValue && value == other.value
    }
}

class NumValue(val value: Double) : Value {
    override fun equals(other: Any?): Boolean {
        return other is NumValue && value == other.value
    }
}

sealed interface Function : Token
object PRINT : Function
