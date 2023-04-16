package ingsis.printscript.analyser

import ingsis.printscript.lexer.Lexer
import ingsis.printscript.parser.implementations.Parser
import ingsis.printscript.utilities.enums.Version
import ingsis.printscript.utilities.interfaces.IParser
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports

enum class Configs {
    CAMEL_CASE,
    SNAKE_CASE,
    LIMIT_PRINTLN,
    LIMIT_READ_INPUT,
}

class Analyser(
    private val lexer: Lexer,
    private val parser: IParser,
    private val rules: List<Rule>,
) {

    object Factory {
        fun getDefault(config: List<Configs>): Analyser {
            return Analyser(
                Lexer(),
                Parser(Version.VERSION_1_1),
                generateRules(config),
            )
        }
        private fun generateRules(config: List<Configs>): List<Rule> {
            if (config.contains(Configs.SNAKE_CASE) && config.contains(Configs.CAMEL_CASE)) {
                throw Error("Variable names can not be snake case and camel case")
            }
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

    fun analyse(inputs: List<String>): List<String> {
        return inputs.fold(listOf()) { acc, input -> acc + analyse(input) }
    }

    fun analyse(input: String): List<String> {
        val tokens = lexer.tokenize(input)
        val ast = parser.parse(tokens)
        val msg = visit(ast)
        return msg
    }

    private fun visit(ast: VisitableAST): List<String> {
        return when (ast) {
            is ReAssignationAST -> {
                this.visit(ast.expression) + validateRules(ast)
            }
            is AssignationAST -> {
                this.visit(ast.expression) + this.visit(ast.declaration) + validateRules(ast)
            }
            is DeclarationAST -> {
                validateRules(ast)
            }
            is BinaryOperationAST -> {
                this.visit(ast.left) + this.visit(ast.right) + validateRules(ast)
            }
            is UnaryOperationAST -> {
                this.visit(ast.args) + validateRules(ast)
            }
            is LiteralAST -> {
                validateRules(ast)
            }
            is VariableAST -> {
                validateRules(ast)
            }
            is EmptyAST -> {
                validateRules(ast)
            }
            is IfAST -> {
                validateRules(ast)
            }
            is IfElseAST -> {
                validateRules(ast)
            }
            is InputAST -> {
                validateRules(ast)
            }
        }
    }

    private fun validateRules(ast: VisitableAST): List<String> {
        return rules.mapNotNull { rule ->
            when (val result = rule.validate(ast)) {
                is InvalidResult -> result.message
                is ValidResult -> null
            }
        }
    }
}
