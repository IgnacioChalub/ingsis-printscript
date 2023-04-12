package ingsis.printscript.analyser

import ingsis.printscript.lexer.Lexer
import ingsis.printscript.parser.implementations.Parser
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
//        val visitor = AnalyserVisitor(generateRules(config), mutableListOf())
//        ast.accept(visitor)
//        return visitor.messages
        val rules = generateRules(config)
        return visit(ast, rules)
    }

    private fun visit(ast: VisitableAST, rules: List<Rule>): List<String> {
        return when (ast) {
            is ReAssignationAST -> {
                return this.visit(ast.expression, rules) + validateRules(ast, rules)
            }
            is AssignationAST -> {
                return this.visit(ast.expression, rules) + this.visit(ast.declaration, rules) + validateRules(ast, rules)
            }
            is DeclarationAST -> {
                return validateRules(ast, rules)
            }
            is BinaryOperationAST -> {
                return this.visit(ast.left, rules) + this.visit(ast.right, rules) + validateRules(ast, rules)
            }
            is UnaryOperationAST -> {
                return this.visit(ast.args, rules) + validateRules(ast, rules)
            }
            is LiteralAST -> {
                return validateRules(ast, rules)
            }
            is VariableAST -> {
                return validateRules(ast, rules)
            }
            is EmptyAST -> {
                return validateRules(ast, rules)
            }
        }
    }

    private fun validateRules(ast: VisitableAST, rules: List<Rule>): List<String> {
        val messages = mutableListOf<String>()
        for (rule in rules) {
            val message = rule.validate(ast)
            if (message !== null) messages.add(message)
        }
        return messages
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
