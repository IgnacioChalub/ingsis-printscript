package ingsis.printscript.analyser

import ingsis.printscript.lexer.Lexer
import ingsis.printscript.parser.implementations.Parser
import ingsis.printscript.utilities.interfaces.IParser

enum class Configs {
    CAMEL_CASE,
    SNAKE_CASE,
    LIMIT_PRINTLN,
    LIMIT_READ_INPUT,
}

class Analyser(
    private val lexer: Lexer,
    private val parser: IParser,
) {

    object Factory {
        fun getDefault(): Analyser {
            return Analyser(
                Lexer(),
                Parser(),
            )
        }
    }

    fun analyse(input: String, config: List<Configs>): List<String> {
        val ast = parser.parse(lexer.tokenize(input))
        val visitor = AnalyserVisitor(generateRules(config), mutableListOf())
        ast.accept(visitor)
        return visitor.messages
    }

    private fun generateRules(config: List<Configs>): List<Rule> {
        return config.map {
            when (it) {
                Configs.CAMEL_CASE -> CamelCaseRule
                Configs.SNAKE_CASE -> SnakeCaseRule
                Configs.LIMIT_PRINTLN -> LimitPrint
                Configs.LIMIT_READ_INPUT -> LimitRead
            }
        }
    }
}
