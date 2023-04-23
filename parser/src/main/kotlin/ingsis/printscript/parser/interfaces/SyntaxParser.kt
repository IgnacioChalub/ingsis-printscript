package ingsis.printscript.parser.interfaces

import ingsis.printscript.utilities.enums.Token
import ingsis.printscript.utilities.visitor.VisitableAST

interface SyntaxParser {
    fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST?
}
