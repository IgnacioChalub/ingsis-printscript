package ingsis.printscript.formatter

import ingsis.printscript.lexer.Lexer
import ingsis.printscript.parser.implementations.Parser
import ingsis.printscript.utilities.enums.Version

class Formatter {
    private val lexer = Lexer()
    private val parser = Parser(Version.VERSION_1_1)
    fun format(input: String): String {
        val fv = FormatterVisitor()
        parser.parse(lexer.tokenize(input)).accept(fv)
        return fv.toString()
    }
}
