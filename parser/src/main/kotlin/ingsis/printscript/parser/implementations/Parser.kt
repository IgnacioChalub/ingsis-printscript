package ingsis.printscript.parser.implementations
import ingsis.printscript.utilities.enums.Token
import ingsis.printscript.utilities.enums.Version
import ingsis.printscript.utilities.interfaces.IParser
import ingsis.printscript.utilities.visitor.VisitableAST

class Parser(private val version: Version) : IParser {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST {
        val ast = StatementMatcher(StatementsProvider.getStatements(version)).match(tokenList)
        return ast ?: throw Exception("Couldn't parse tree")
    }
}
