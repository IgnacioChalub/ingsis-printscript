package ingsis.printscript.interpreter

import ingsis.printscript.utilities.visitor.VisitableAST

class Interpreter(
    private val interpreterVisitor: InterpreterVisitor,
) {

    companion object Factory {
        fun createDefault(): Interpreter = Interpreter(InterpreterVisitor(LocalMemory(mutableMapOf()), PrintFunctionImpl))
        fun createMock(printFunction: PrintFunction): Interpreter = Interpreter(InterpreterVisitor(LocalMemory(mutableMapOf()), printFunction))
    }

    fun interpret(ast: VisitableAST) = ast.accept(this.interpreterVisitor)

    fun getMemory(): LocalMemory = interpreterVisitor.memory
}
