package ingsis.printscript.parser.implementations

import ingsis.printscript.parser.interfaces.IStatement
import ingsis.printscript.parser.interfaces.SyntaxParser
import ingsis.printscript.utilities.enums.Function
import ingsis.printscript.utilities.enums.IDENTIFIER
import ingsis.printscript.utilities.enums.Token
import ingsis.printscript.utilities.enums.Type
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports

class SyntaxProvider {
    fun parse(tokenList: List<Token>): VisitableAST {
        return if (matches(tokenList, Statement.ASSIGNATION)) {
            AssignationParser().parse(tokenList)
        } else if (matches(tokenList, Statement.FUNCTION)) {
            FunctionParser().parse(tokenList)
        } else if (matches(tokenList, Statement.RE_ASSIGNATION)) {
            ReAssignationParser().parse(tokenList)
        } else {
            throw Exception("Couldn't find syntax")
        }
    }

    private fun matches(tokenList: List<Token>, statementFormat: IStatement): Boolean {
        val minSize = minOf(tokenList.size, statementFormat.elements.size)
        return tokenList.take(minSize).withIndex().all { (token, index) -> statementFormat.isValidToken(index, token) }
    }
}

class AssignationParser : SyntaxParser {
    override fun parse(tokenList: List<Token>): VisitableAST {
        return AssignationAST(
            DeclarationAST(
                (tokenList[1] as IDENTIFIER).value,
                tokenList[3] as Type,
            ),
            if (tokenList.size > 5) {
                ExpressionMatcher(ExpressionProvider.expressions).match(tokenList.slice(5 until tokenList.size - 1)) as ExpressionAST
            } else {
                EmptyAST()
            },
        )
    }
}

class FunctionParser : SyntaxParser {
    override fun parse(tokenList: List<Token>): VisitableAST {
        return UnaryOperationAST(
            tokenList[0] as Function,
            ExpressionMatcher(ExpressionProvider.expressions).match(tokenList.slice(1 until tokenList.size - 1)) as ExpressionAST,
        )
    }
}

class ReAssignationParser : SyntaxParser {
    override fun parse(tokenList: List<Token>): VisitableAST {
        return ReAssignationAST(
            (tokenList[0] as IDENTIFIER).value,
            ExpressionMatcher(ExpressionProvider.expressions).match(tokenList.slice(2 until tokenList.size - 1)) as ExpressionAST,

        )
    }
}
