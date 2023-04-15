package ingsis.printscript.analyser

import ingsis.printscript.utilities.enums.PRINT
import ingsis.printscript.utilities.enums.READINPUT
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports

sealed interface Rule {
    fun validate(ast: VisitableAST): RuleResult
}

sealed interface RuleResult
object ValidResult : RuleResult
data class InvalidResult(val message: String) : RuleResult

object CamelCaseRule : Rule {
    override fun validate(ast: VisitableAST): RuleResult {
        return when (ast) {
            is DeclarationAST -> {
                val camelRegex = "([a-z][a-z0-9]+[A-Z])+[a-z0-9]+".toRegex()
                if (!ast.variableName.matches(camelRegex)) {
                    return InvalidResult("Variable name with camel case required")
                }
                return ValidResult
            }
            else -> ValidResult
        }
    }
}

object SnakeCaseRule : Rule {
    override fun validate(ast: VisitableAST): RuleResult {
        return when (ast) {
            is DeclarationAST -> {
                val snakeRegex = "[a-z][a-z0-9]*(_[a-z0-9]+)*".toRegex()
                if (!ast.variableName.matches(snakeRegex)) {
                    return InvalidResult("Variable name with snake case required")
                }
                return ValidResult
            }
            else -> ValidResult
        }
    }
}

object LimitPrint : Rule {
    override fun validate(ast: VisitableAST): RuleResult {
        return when (ast) {
            is UnaryOperationAST -> {
                if (ast.function !is PRINT) return ValidResult
                if (ast.args !is VariableAST && ast.args !is LiteralAST) {
                    return InvalidResult("Print can only be invoked with a variable or literal")
                }
                return ValidResult
            }
            else -> ValidResult
        }
    }
}

object LimitRead : Rule {
    override fun validate(ast: VisitableAST): RuleResult {
        return when (ast) {
            is UnaryOperationAST -> {
                if (ast.function !is READINPUT) return ValidResult
                if (ast.args !is VariableAST && ast.args !is LiteralAST) {
                    return InvalidResult("Print can only be invoked with a variable or literal")
                }
                return ValidResult
            }
            else -> ValidResult
        }
    }
}
