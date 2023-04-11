package ingsis.printscript.analyser

import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports

class AnalyserVisitor(
    private val rules: List<Rule>,
    val messages: MutableList<String>,
) : Visitor {

    override fun visitAssignationAST(ast: AssignationAST): VisitableAST {
        rules.map {
            it.validate(ast)
        }?.let {
            it.forEach {
                if (it != null) {
                    messages.add(it)
                }
            }
        }
        ast.expression.accept(this)
        ast.declaration.accept(this)
        return EmptyAST()
    }

    override fun visitReAssignationAST(ast: ReAssignationAST): VisitableAST {
        TODO("Not yet implemented")
    }

    override fun visitDeclarationAST(ast: DeclarationAST): VisitableAST {
        rules.map {
            it.validate(ast)
        }?.let {
            it.forEach {
                if (it != null) {
                    messages.add(it)
                }
            }
        }
        return ast
    }

    override fun visitBinaryOperationAST(ast: BinaryOperationAST): VisitableAST {
        rules.map {
            it.validate(ast)
        }?.let {
            it.forEach {
                if (it != null) {
                    messages.add(it)
                }
            }
        }
        ast.left.accept(this)
        ast.right.accept(this)
        return EmptyAST()
    }

    override fun visitUnaryOperationAST(ast: UnaryOperationAST): VisitableAST {
        rules.map {
            it.validate(ast)
        }?.let {
            it.forEach {
                if (it != null) {
                    messages.add(it)
                }
            }
        }
        ast.args.accept(this)
        return EmptyAST()
    }

    override fun visitLiteralAST(ast: LiteralAST): VisitableAST {
        rules.map {
            it.validate(ast)
        }?.let {
            it.forEach {
                if (it != null) {
                    messages.add(it)
                }
            }
        }
        return EmptyAST()
    }

    override fun visitVariableAST(ast: VariableAST): VisitableAST {
        rules.map {
            it.validate(ast)
        }?.let {
            it.forEach {
                if (it != null) {
                    messages.add(it)
                }
            }
        }
        return EmptyAST()
    }

    override fun visitEmptyAST(ast: EmptyAST): VisitableAST {
        rules.map {
            it.validate(ast)
        }?.let {
            it.forEach {
                if (it != null) {
                    messages.add(it)
                }
            }
        }
        return ast
    }
}
