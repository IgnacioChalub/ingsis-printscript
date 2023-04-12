package ingsis.printscript.utilities.visitor

interface Visitor {
    fun visitAssignationAST(ast: AssignationAST): VisitableAST

    fun visitReAssignationAST(ast: ReAssignationAST): VisitableAST
    fun visitDeclarationAST(ast: DeclarationAST): VisitableAST
    fun visitBinaryOperationAST(ast: BinaryOperationAST): VisitableAST
    fun visitUnaryOperationAST(ast: UnaryOperationAST): VisitableAST
    fun visitLiteralAST(ast: LiteralAST): VisitableAST
    fun visitVariableAST(ast: VariableAST): VisitableAST
    fun visitEmptyAST(ast: EmptyAST): VisitableAST
    fun visitIfAST(ast: IfAST): VisitableAST
    fun visitIfElseAST(ast: IfElseAST): VisitableAST
}
