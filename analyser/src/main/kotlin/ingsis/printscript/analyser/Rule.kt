package ingsis.printscript.analyser

import ingsis.printscript.utilities.enums.PRINT
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports

sealed interface Rule {
    fun validate(ast: VisitableAST): String?
}

object CamelCaseRule : Rule {
    override fun validate(ast: VisitableAST): String? {
        return when (ast) {
            is DeclarationAST -> {
                val camelRegex = "([a-z][a-z0-9]+[A-Z])+[a-z0-9]+".toRegex()
                if (!ast.variableName.matches(camelRegex)) {
                    return "Variable name with camel case required"
                }
                return null
            }
            else -> null
        }
    }
}

object SnakeCaseRule : Rule {
    override fun validate(ast: VisitableAST): String? {
        return when (ast) {
            is DeclarationAST -> {
                val snakeRegex = "[a-z][a-z0-9]*(_[a-z0-9]+)*".toRegex()
                if (!ast.variableName.matches(snakeRegex)) {
                    return "Variable name with snake case required"
                }
                return null
            }
            else -> null
        }
    }
}

object LimitPrint : Rule {
    override fun validate(ast: VisitableAST): String? {
        return when (ast) {
            is UnaryOperationAST -> {
                if (ast.function !is PRINT) return null
                if (ast.args !is VariableAST && ast.args !is LiteralAST) {
                    return "Print can only be invoked with a variable or literal"
                }
                return null
            }
            else -> null
        }
    }
}

object LimitRead : Rule {
    override fun validate(ast: VisitableAST): String? {
        TODO("Not yet implemented")
    }
}
