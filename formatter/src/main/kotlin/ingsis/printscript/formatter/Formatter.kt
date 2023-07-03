package ingsis.printscript.formatter

import ingsis.printscript.lexer.Lexer
import ingsis.printscript.parser.implementations.Parser
import ingsis.printscript.utilities.enums.Version

data class FormattingRules(
    val spaceBeforeColon: Boolean = true,
    val spaceAfterColon: Boolean = true,
    val spaceAroundEquals: Boolean = true,
    val newLineBeforePrintln: Int = 0,
    val indentSize: Int = 4
)

class Formatter(private val rules: FormattingRules) {
    private val lexer = Lexer()
    private val parser = Parser(Version.VERSION_1_1)

    fun format(input: String): String {
        val fv = FormatterVisitor(rules)
        parser.parse(lexer.tokenize(input)).accept(fv)
        return fv.toString()
    }
}

