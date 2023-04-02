package ingsis.printscript.parser.interfaces

import ingsis.printscript.utilities.enums.Token

interface SyntaxElement {
    val types: List<Token>

    fun contains(type: Token): Boolean = type in types

    fun get(): Array<Token> = types.toTypedArray()
}
