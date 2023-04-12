package ingsis.printscript.parser.interfaces

import ingsis.printscript.utilities.enums.Token
import ingsis.printscript.utilities.visitor.VisitableAST

interface SyntaxMatcher {
    fun match(tokenList: List<Token>): VisitableAST?
}
