package ingsis.printscript.interpreter

import ingsis.printscript.utilities.visitor.VisitableAST

class Interpreter(
    private val interpreterVisitor: InterpreterVisitor
) {

    companion object Factory {
        fun create(): Interpreter = Interpreter(InterpreterVisitor(LocalMemory(mutableMapOf())))
    }

    fun interpret(ast: VisitableAST) = ast.accept(this.interpreterVisitor)

    fun getMemory(): LocalMemory = interpreterVisitor.memory

}