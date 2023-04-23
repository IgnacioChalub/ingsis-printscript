package ingsis.printscript.parser.implementations

import ingsis.printscript.parser.interfaces.SyntaxMatcher
import ingsis.printscript.parser.interfaces.SyntaxParser
import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports
import ingsis.printscript.utilities.enums.Function
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class StatementMatcher(matchers: List<Pair<KClass<out SyntaxParser>, List<Any>>>) : SyntaxMatcher {
    private val statements = matchers.mapNotNull { it.first.primaryConstructor?.call(*it.second.toTypedArray()) }

    override fun match(tokenList: List<Pair<Token, Int>>): VisitableAST? =
        statements.firstNotNullOfOrNull { it.parse(tokenList) }
}

// let a: String;
class DeclarationParser : SyntaxParser {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        if (tokenList.size != 5) return null

        if (!SyntaxElements.VARIABLE.contains(tokenList[0].first)) return null
        if (tokenList.size < 5) throw Exception("Missing tokens for declaration statement")

        val identifier = if (SyntaxElements.IDENTIFIER_SYNTAX.contains(tokenList[1].first)) tokenList[1] else throw ParserException("identifier", tokenList[1].second)
        if (!SyntaxElements.TYPEASSIGNMENT.contains(tokenList[2].first)) throw ParserException("colon", tokenList[2].second)
        val type = if (SyntaxElements.TYPE.contains(tokenList[3].first)) tokenList[3] else throw ParserException("type", tokenList[3].second)

        return DeclarationAST(
            (identifier.first as IDENTIFIER).value to identifier.second,
            type.first as Type to type.second,
            isMutable = tokenList[0].first is LET,
        )
    }
}

// a = "Hola";
class AssignationParser(private val version: Version) : SyntaxParser {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        val identifier = if (SyntaxElements.IDENTIFIER_SYNTAX.contains(tokenList[0].first)) tokenList[0] else return null
        if (tokenList.size < 3) throw Exception("Missing tokens for assignation statement")

        if (!SyntaxElements.ASSIGNATION_SYNTAX.contains(tokenList[1].first)) throw ParserException("assignation", tokenList[1].second)
        if (!SyntaxElements.END.contains(tokenList[tokenList.size - 1].first)) throw ParserException("semicolon", tokenList[tokenList.size - 1].second)
        return ReAssignationAST(
            (identifier.first as IDENTIFIER).value to identifier.second,
            ExpressionMatcher(ExpressionProvider.getExpressions(version)).match(tokenList.slice(2 until tokenList.size - 1)) as ExpressionAST,
        )
    }
}

// let a: String = "Hello";
class AssignationDeclarationParser(private val version: Version) : SyntaxParser {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        val variable = if (SyntaxElements.VARIABLE.contains(tokenList[0].first)) tokenList[0] else return null
        if (tokenList.size < 6) throw Exception("Missing tokens for full assignation statement")

        val identifier = if (SyntaxElements.IDENTIFIER_SYNTAX.contains(tokenList[1].first)) tokenList[1] else throw ParserException("identifier", tokenList[1].second)
        if (!SyntaxElements.TYPEASSIGNMENT.contains(tokenList[2].first)) throw ParserException("colon", tokenList[2].second)
        val type = if (SyntaxElements.TYPE.contains(tokenList[3].first)) tokenList[3] else throw ParserException("type", tokenList[3].second)
        if (!SyntaxElements.ASSIGNATION_SYNTAX.contains(tokenList[4].first)) throw ParserException("assignation", tokenList[4].second)
        if (!SyntaxElements.END.contains(tokenList[tokenList.size - 1].first)) throw ParserException("semicolon", tokenList[tokenList.size - 1].second)

        return AssignationAST(
            DeclarationAST(
                (identifier.first as IDENTIFIER).value to identifier.second,
                type.first as Type to type.second,
                isMutable = variable.first is LET,
            ),
            ExpressionMatcher(ExpressionProvider.getExpressions(version)).match(tokenList.slice(5 until tokenList.size - 1)) as ExpressionAST,
        )
    }
}

// print("hello")
class FunctionParser(private val version: Version) : SyntaxParser {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        if (tokenList.size < 4) return null

        val function = if (SyntaxElements.VOIDFUNCTION.contains(tokenList[0].first)) tokenList[0] else return null
        if (tokenList.size < 4) throw Exception("Missing tokens for function statement")

        if (!SyntaxElements.LEFT_PAREN_SYNTAX.contains(tokenList[1].first)) throw ParserException("left parenthesis", tokenList[1].second)
        if (!SyntaxElements.RIGHT_PAREN_SYNTAX.contains(tokenList[tokenList.size - 2].first)) throw ParserException("right parenthesis", tokenList[tokenList.size - 2].second)
        if (!SyntaxElements.END.contains(tokenList[tokenList.size - 1].first)) throw ParserException("semicolon", tokenList[tokenList.size - 1].second)
        return UnaryOperationAST(
            function.first as Function to function.second,
            ExpressionMatcher(ExpressionProvider.getExpressions(version)).match(tokenList.slice(2 until tokenList.size - 2)) as ExpressionAST,
        )
    }
}

private fun splitList(list: List<Pair<Token, Int>>): List<List<Pair<Token, Int>>> {
    val result = mutableListOf<MutableList<Pair<Token, Int>>>()
    var sublist = mutableListOf<Pair<Token, Int>>()

    for (item in list) {
        if (item.first == SEMICOLON) {
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
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        if (!SyntaxElements.IF_SYNTAX.contains(tokenList[0].first)) return null
        if (tokenList.map { it.first }.contains(ELSE)) return null
        if (tokenList.size < 6) throw Exception("Missing tokens for if statement")
        if (!SyntaxElements.LEFT_PAREN_SYNTAX.contains(tokenList[1].first)) throw ParserException("left parenthesis", tokenList[1].second)
        if (!tokenList.map { it.first }.contains(RIGHT_PAREN)) throw ParserException("right parenthesis")
        val leftCurlyBrace = if (tokenList.map { it.first }.indexOf(LEFT_CURLY_BRACES) >= 0) tokenList.map { it.first }.indexOf(LEFT_CURLY_BRACES) else throw ParserException("left curly braces")
        val rightCurlyBrace = if (tokenList.map { it.first }.indexOf(RIGHT_CURLY_BRACES) >= 0) tokenList.map { it.first }.indexOf(RIGHT_CURLY_BRACES) else throw ParserException("right curly braces")
        if (!SyntaxElements.END.contains(tokenList[tokenList.size - 1].first)) throw ParserException("semicolon", tokenList[tokenList.size - 1].second)
        if (tokenList.map { it.first }.contains(ELSE)) throw ParserException("else")
        val blockParser = Parser(version)
        return IfAST(
            condition = when (tokenList[2].first) {
                is BoolValue -> LiteralAST(tokenList[2].first as BoolValue to tokenList[2].second)
                is IDENTIFIER -> VariableAST((tokenList[2].first as IDENTIFIER).value to tokenList[2].second)
                else -> throw Exception("Condition not supported")
            },
            truthBlock = splitList(tokenList.slice(leftCurlyBrace + 1 until rightCurlyBrace)).map {
                blockParser.parse(
                    it,
                )
            },
        )
    }
}

class IfElseStatementParser(private val version: Version) : SyntaxParser {
    override fun parse(tokenList: List<Pair<Token, Int>>): VisitableAST? {
        if (!SyntaxElements.IF_SYNTAX.contains(tokenList[0].first)) return null
        val elseSyntax = if (tokenList.map { it.first }.indexOf(ELSE) >= 0) tokenList.map { it.first }.indexOf(ELSE) else return null
        if (tokenList.size < 9) throw Exception("Missing tokens for if else statement")
        if (!SyntaxElements.LEFT_PAREN_SYNTAX.contains(tokenList[1].first)) throw ParserException("left parenthesis", tokenList[1].second)
        if (!SyntaxElements.RIGHT_PAREN_SYNTAX.contains(tokenList[3].first)) throw ParserException("right parenthesis", tokenList[3].second)
        val leftCurlyBrace = if (tokenList.map { it.first }.contains(LEFT_CURLY_BRACES)) tokenList.map { it.first }.indexOfFirst { it is LEFT_CURLY_BRACES } else throw ParserException("left curly braces")
        val rightCurlyBrace = if (tokenList[elseSyntax - 1].first is RIGHT_CURLY_BRACES) (elseSyntax - 1) else throw ParserException("first right curly braces")

        if (tokenList[elseSyntax + 1].first !is LEFT_CURLY_BRACES) throw ParserException("else left curly braces")
        val elseRightCurlyBrace = if (tokenList[tokenList.size - 2].first is RIGHT_CURLY_BRACES) (tokenList.size - 2) else throw ParserException("else right curly braces")

        if (!SyntaxElements.END.contains(tokenList[tokenList.size - 1].first)) throw ParserException("semicolon")
        val blockParser = Parser(version)
        return IfElseAST(
            condition = when (tokenList[2].first) {
                is BoolValue -> LiteralAST(tokenList[2].first as BoolValue to tokenList[2].second)
                is IDENTIFIER -> VariableAST((tokenList[2].first as IDENTIFIER).value to tokenList[2].second)
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
    }
}
