package ingsis.printscript.analyser

import ingsis.printscript.utilities.enums.PRINT
import ingsis.printscript.utilities.enums.READINPUT
import ingsis.printscript.utilities.visitor.*

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
//                val camelRegex = "([a-z][a-z0-9]+[A-Z])+[a-z0-9]+".toRegex()
                if (!isCamelCase(ast.variableName)) {
                    return InvalidResult("Variable name with camel case required")
                }
                return ValidResult
            }
            else -> ValidResult
        }
    }

    fun isCamelCase(input: String): Boolean {
        return !input.contains("_")
    }
}

object SnakeCaseRule : Rule {
    override fun validate(ast: VisitableAST): RuleResult {
        return when (ast) {
            is DeclarationAST -> {
//                val snakeRegex =  Regex("^[a-zA-Z0-9]+(?:_[a-zA-Z0-9]*)*$")
                if (!isSnakeCase(ast.variableName)) {
                    return InvalidResult("Variable name with snake case required")
                }
                return ValidResult
            }
            else -> ValidResult
        }
    }

    private fun isSnakeCase(input: String): Boolean {
        val onlyLetters = input.filter { it.isLetter() }
        val isUpperCase = onlyLetters.all { it.isUpperCase() }
        val isLowerCase = onlyLetters.all { it.isLowerCase() }
        return isUpperCase || isLowerCase
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
                    return InvalidResult("Read input can only be invoked with a variable or literal")
                }
                return ValidResult
            }
            else -> ValidResult
        }
    }
}
