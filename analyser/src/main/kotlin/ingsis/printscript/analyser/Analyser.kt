package ingsis.printscript.analyser

import ingsis.printscript.utilities.visitor.*

enum class Configs {
    CAMEL_CASE,
    SNAKE_CASE,
    LIMIT_PRINTLN,
    LIMIT_READ_INPUT,
}

class Analyser(
    private val rules: List<Rule>,
) {

    object Factory {
        fun getDefault(config: List<Configs>): Analyser {
            return Analyser(
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

    fun analyse(inputs: List<VisitableAST>): List<String> {
        return inputs.fold(listOf()) { acc, input -> acc + analyse(input) }
    }

    fun analyse(ast: VisitableAST): List<String> {
        return visit(ast)
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
                return validateRules(ast) + ast.truthBlock.map { this.visit(it) }.flatten()
            }
            is IfElseAST -> {
                validateRules(ast) + ast.truthBlock.map { this.visit(it) }.flatten() + ast.falseBlock.map { this.visit(it) }.flatten()
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
