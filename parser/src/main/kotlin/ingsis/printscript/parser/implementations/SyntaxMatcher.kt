package ingsis.printscript.parser.implementations

import ingsis.printscript.parser.interfaces.SyntaxMatcher
import ingsis.printscript.parser.interfaces.SyntaxParser
import ingsis.printscript.utilities.enums.*
import ingsis.printscript.utilities.enums.Function
import ingsis.printscript.utilities.visitor.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class StatementMatcher(matchers: List<Pair<KClass<out SyntaxParser>, List<Any>>>) : SyntaxMatcher {
    private val statements = matchers.mapNotNull { it.first.primaryConstructor?.call(*it.second.toTypedArray()) }

    override fun match(tokenList: List<Token>): VisitableAST? =
        statements.firstNotNullOfOrNull { it.parse(tokenList) }
}

// let a: String;
class DeclarationParser : SyntaxParser {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size != 5) return null

        val variable = if (SyntaxElements.VARIABLE.contains(tokenList[0])) tokenList[0] else null
        val identifier = if (SyntaxElements.IDENTIFIER_SYNTAX.contains(tokenList[1])) tokenList[1] else null
        val colon = SyntaxElements.TYPEASSIGNMENT.contains(tokenList[2])
        val type = if (SyntaxElements.TYPE.contains(tokenList[3])) tokenList[3] else null

        return if (variable != null && identifier != null && colon && type != null) {
            DeclarationAST(
                (identifier as IDENTIFIER).value,
                type as Type,
                isMutable = variable is LET,
            )
        } else {
            null
        }
    }
}

// a = "Hola";
class AssignationParser(private val version: Version) : SyntaxParser {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size < 3) return null

        val identifier = if (SyntaxElements.IDENTIFIER_SYNTAX.contains(tokenList[0])) tokenList[0] else null
        val assignation = if (SyntaxElements.ASSIGNATION_SYNTAX.contains(tokenList[1])) tokenList[1] else null
        val semicolon =
            if (SyntaxElements.END.contains(tokenList[tokenList.size - 1])) tokenList[tokenList.size - 1] else null
        return if (assignation != null && identifier != null && semicolon != null) {
            ReAssignationAST(
                (identifier as IDENTIFIER).value,
                ExpressionMatcher(ExpressionProvider.getExpressions(version)).match(tokenList.slice(2 until tokenList.size - 1)) as ExpressionAST,
            )
        } else {
            null
        }
    }
}

// let a: String = "Hello";
class AssignationDeclarationParser(private val version: Version) : SyntaxParser {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size < 6) return null
        val variable = if (SyntaxElements.VARIABLE.contains(tokenList[0])) tokenList[0] else null
        val identifier = if (SyntaxElements.IDENTIFIER_SYNTAX.contains(tokenList[1])) tokenList[1] else null
        val colon = SyntaxElements.TYPEASSIGNMENT.contains(tokenList[2])
        val type = if (SyntaxElements.TYPE.contains(tokenList[3])) tokenList[3] else null
        val assignation = if (SyntaxElements.ASSIGNATION_SYNTAX.contains(tokenList[4])) tokenList[4] else null
        val semicolon =
            if (SyntaxElements.END.contains(tokenList[tokenList.size - 1])) tokenList[tokenList.size - 1] else null

        return if (variable != null && identifier != null && colon && type != null && assignation != null && semicolon != null) {
            AssignationAST(
                DeclarationAST(
                    (identifier as IDENTIFIER).value,
                    type as Type,
                    isMutable = variable is LET,
                ),
                ExpressionMatcher(ExpressionProvider.getExpressions(version)).match(tokenList.slice(5 until tokenList.size - 1)) as ExpressionAST,
            )
        } else {
            null
        }
    }
}

// print("hello")
class FunctionParser(private val version: Version) : SyntaxParser {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size < 4) return null

        val function = if (SyntaxElements.VOIDFUNCTION.contains(tokenList[0])) tokenList[0] else null
        val leftparen = if (SyntaxElements.LEFT_PAREN_SYNTAX.contains(tokenList[1])) tokenList[1] else null
        val rightparen =
            if (SyntaxElements.RIGHT_PAREN_SYNTAX.contains(tokenList[tokenList.size - 2])) tokenList[tokenList.size - 2] else null
        val semicolon =
            if (SyntaxElements.END.contains(tokenList[tokenList.size - 1])) tokenList[tokenList.size - 1] else null
        return if (function != null && leftparen != null && rightparen != null && semicolon != null) {
            UnaryOperationAST(
                function as Function,
                ExpressionMatcher(ExpressionProvider.getExpressions(version)).match(tokenList.slice(2 until tokenList.size - 2)) as ExpressionAST,
            )
        } else {
            null
        }
    }
}

private fun splitList(list: List<Token>): List<List<Token>> {
    val result = mutableListOf<MutableList<Token>>()
    var sublist = mutableListOf<Token>()

    for (item in list) {
        if (item == SEMICOLON) {
            sublist.add(item)
            result.add(sublist)
            sublist = mutableListOf()
        } else {
            sublist.add(item)
        }
    }
    if (sublist.isNotEmpty()) {
        result.add(sublist)
    }

    return result
}

class IfStatementParser(private val version: Version) : SyntaxParser {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size < 6) return null

        val ifsyntax = if (SyntaxElements.IF_SYNTAX.contains(tokenList[0])) tokenList[0] else null
        val leftparen = if (SyntaxElements.LEFT_PAREN_SYNTAX.contains(tokenList[1])) tokenList[1] else null
        val rightparen = tokenList.indexOf(RIGHT_PAREN)
        val leftCurlyBrace = tokenList.indexOf(LEFT_CURLY_BRACES)
        val rightCurlyBrace = tokenList.indexOf(RIGHT_CURLY_BRACES)
        val semicolon =
            if (SyntaxElements.END.contains(tokenList[tokenList.size - 1])) tokenList[tokenList.size - 1] else null
        val blockParser = Parser(version)
        return if (ifsyntax != null && leftparen != null && semicolon != null && rightparen < leftCurlyBrace && leftCurlyBrace < rightCurlyBrace && !tokenList.contains(
                ELSE,
            )
        ) {
            IfAST(
                condition = when (tokenList[2]) {
                    is BoolValue -> LiteralAST(tokenList[2] as BoolValue)
                    is IDENTIFIER -> VariableAST((tokenList[2] as IDENTIFIER).value)
                    else -> throw Exception("Condition not supported")
                },
                truthBlock = splitList(tokenList.slice(leftCurlyBrace + 1 until rightCurlyBrace)).map {
                    blockParser.parse(
                        it,
                    )
                },
            )
        } else {
            null
        }
    }
}

class IfElseStatementParser(private val version: Version) : SyntaxParser {
    override fun parse(tokenList: List<Token>): VisitableAST? {
        if (tokenList.size < 9) return null
        val ifsyntax = if (SyntaxElements.IF_SYNTAX.contains(tokenList[0])) tokenList[0] else null
        val leftparen = if (SyntaxElements.LEFT_PAREN_SYNTAX.contains(tokenList[1])) tokenList[1] else null
        val rightparen = tokenList.indexOf(RIGHT_PAREN)
        val leftCurlyBrace = tokenList.indexOfFirst { it is LEFT_CURLY_BRACES }
        val rightCurlyBrace = tokenList.indexOfFirst { it is RIGHT_CURLY_BRACES }

        val elseSyntax = tokenList.indexOf(ELSE)

        val elseLeftCurlyBrace = tokenList.indexOfLast { it is LEFT_CURLY_BRACES }
        val elseRightCurlyBrace = tokenList.indexOfLast { it is RIGHT_CURLY_BRACES }
        val semicolon =
            if (SyntaxElements.END.contains(tokenList[tokenList.size - 1])) tokenList[tokenList.size - 1] else null
        val blockParser = Parser(version)
        return if (ifsyntax != null && leftparen != null && semicolon != null && rightparen < leftCurlyBrace && leftCurlyBrace < rightCurlyBrace && leftCurlyBrace < elseLeftCurlyBrace && rightCurlyBrace < elseRightCurlyBrace) {
            IfElseAST(
                condition = when (tokenList[2]) {
                    is BoolValue -> LiteralAST(tokenList[2] as BoolValue)
                    is IDENTIFIER -> VariableAST((tokenList[2] as IDENTIFIER).value)
                    else -> throw Exception("Condition not supported")
                },
                truthBlock = splitList(tokenList.slice(leftCurlyBrace + 1 until rightCurlyBrace)).map {
                    blockParser.parse(
                        it,
                    )
                },
                falseBlock = splitList(tokenList.slice(elseSyntax + 2 until elseRightCurlyBrace)).map {
                    blockParser.parse(
                        it,
                    )
                },
            )
        } else {
            null
        }
    }
}
