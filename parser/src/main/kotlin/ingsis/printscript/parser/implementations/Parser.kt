package ingsis.printscript.parser.implementations
import ingsis.printscript.utilities.enums.Token
import ingsis.printscript.utilities.interfaces.IParser
import ingsis.printscript.utilities.visitor.VisitableAST

class Parser : IParser {
    override fun parse(tokenList: List<Token>): VisitableAST {
        return SyntaxProvider(tokenList).parse()
    }
}