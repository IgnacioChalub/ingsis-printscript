package ingsis.printscript.parser.interfaces

import ingsis.printscript.utilities.enums.Token

interface IStatement {

    val elements: List<SyntaxElement>

    fun isValidToken(token: Token, index: Int): Boolean = elements[index].get().any { it::class === token::class }

    fun get(): Array<SyntaxElement> = elements.toTypedArray()
}
