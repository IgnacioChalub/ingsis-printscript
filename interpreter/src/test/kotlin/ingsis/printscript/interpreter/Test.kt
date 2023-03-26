package ingsis.printscript.interpreter

import ingsis.printscript.utilities.visitor.ADD
import ingsis.printscript.utilities.visitor.AssignationAST
import ingsis.printscript.utilities.visitor.BinaryOperationAST
import ingsis.printscript.utilities.visitor.DeclarationAST
import ingsis.printscript.utilities.visitor.LiteralAST
import ingsis.printscript.utilities.visitor.MUL
import ingsis.printscript.utilities.visitor.NUM
import ingsis.printscript.utilities.visitor.NumValue
import ingsis.printscript.utilities.visitor.PRINT
import ingsis.printscript.utilities.visitor.STR
import ingsis.printscript.utilities.visitor.StrValue
import ingsis.printscript.utilities.visitor.UnaryOperationAST
import ingsis.printscript.utilities.visitor.VariableAST
import org.junit.jupiter.api.Test

class Test {

    @Test
    fun test1() {
        // let someNumber: Number = 3*2+1;
        val tree = AssignationAST(
            DeclarationAST(
                "someNumber",
                NUM,
            ),
            BinaryOperationAST(
                BinaryOperationAST(
                    LiteralAST(NumValue(3.0)),
                    LiteralAST(NumValue(2.0)),
                    MUL,
                ),
                LiteralAST(NumValue(1.0)),
                ADD,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        interpreter.interpret(tree)
        assert((interpreter.getMemory().getValue("someNumber") as NumValue).value == 7.0)
    }

    @Test
    fun test2() {
        // let someString: String = "some" + "1";
        val tree = AssignationAST(
            DeclarationAST(
                "someString",
                STR,
            ),
            BinaryOperationAST(
                LiteralAST(StrValue("some")),
                LiteralAST(NumValue(1.0)),
                ADD,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        interpreter.interpret(tree)
        assert((interpreter.getMemory().getValue("someString") as StrValue).value == "some1.0")
    }

    @Test
    fun test3() {
        // let someString: String = "some" + "1";
        // let newSomeString: String = "new" + someString;

        val tree1 = AssignationAST(
            DeclarationAST(
                "someString",
                STR,
            ),
            BinaryOperationAST(
                LiteralAST(StrValue("some")),
                LiteralAST(NumValue(1.0)),
                ADD,
            ),
        )
        val tree2 = AssignationAST(
            DeclarationAST(
                "newSomeString",
                STR,
            ),
            BinaryOperationAST(
                LiteralAST(StrValue("new ")),
                VariableAST("someString"),
                ADD,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        interpreter.interpret(tree1)
        interpreter.interpret(tree2)
        assert((interpreter.getMemory().getValue("someString") as StrValue).value == "some1.0")
        assert((interpreter.getMemory().getValue("newSomeString") as StrValue).value == "new some1.0")
    }

    @Test
    fun test4() {
        // let someString: String = "some" + "1";
        // print(someString);

        val tree1 = AssignationAST(
            DeclarationAST(
                "someString",
                STR,
            ),
            BinaryOperationAST(
                LiteralAST(StrValue("some")),
                LiteralAST(NumValue(1.0)),
                ADD,
            ),
        )
        val tree2 = UnaryOperationAST(
            PRINT,
            VariableAST("someString"),
        )
        val printFunctionMock = PrintFunctionMock("")
        val interpreter = Interpreter.Factory.createMock(printFunctionMock)
        interpreter.interpret(tree1)
        interpreter.interpret(tree2)
        assert(printFunctionMock.printedValue == "some1.0")
    }
}
