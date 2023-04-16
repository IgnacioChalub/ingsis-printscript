package ingsis.printscript.interpreter

import ingsis.printscript.utilities.visitor.VisitableAST
import java.util.*

class Interpreter(
    private val interpreterVisitor: InterpreterVisitor,
) {

    companion object Factory {
        fun createDefault(): Interpreter = Interpreter(InterpreterVisitor(LocalMemory(mutableMapOf(), Optional.empty()), PrintFunctionImpl, ReadInputFunctionImpl))
        fun createMock(printFunction: PrintFunction, readInputFunction: ReadInputFunction): Interpreter = Interpreter(InterpreterVisitor(LocalMemory(mutableMapOf(), Optional.empty()), printFunction, readInputFunction))
    }

    fun interpret(ast: VisitableAST) = ast.accept(this.interpreterVisitor)

    fun getMemory(): LocalMemory = interpreterVisitor.memory
}
