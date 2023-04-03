package ingsis.printscript.utilities.interfaces

import ingsis.printscript.utilities.enums.Token
import ingsis.printscript.utilities.visitor.VisitableAST

interface IParser {
    fun parse(tokenList: List<Token>): VisitableAST
}
