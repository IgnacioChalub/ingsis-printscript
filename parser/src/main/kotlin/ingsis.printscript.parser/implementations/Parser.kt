package ingsis.printscript.parser.implementations
import ingsis.printscript.utilities.interfaces.IParser
import ingsis.printscript.utilities.types.Token
import ingsis.printscript.utilities.visitor.*

class Parser : IParser {
    override fun parse(tokenList: List<Token>): VisitableAST {
        val syntax = SyntaxProvider(tokenList).provide()
        return syntax.parse(tokenList)
    }
}
