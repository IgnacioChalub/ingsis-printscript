package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.* // ktlint-disable no-wildcard-imports
import ingsis.printscript.utilities.visitor.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.Test

class Test {

    @Test
    fun test1() {
        // let someNumber: Number = 3*2+1;
        val tree = AssignationAST(
            DeclarationAST(
                "someNumber",
                NUM,
                false,
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
                false,
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
                false,
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
                false,
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
                false,
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

    @Test
    fun test5() {
        val tree = IfAST(
            LiteralAST(BoolValue(true)),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("First if")),
                ),
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("Second if")),
                ),
            ),
        )
        val printFunctionMock = PrintManyFunctionMock(mutableListOf())
        val interpreter = Interpreter.Factory.createMock(printFunctionMock)
        interpreter.interpret(tree)
        assert(printFunctionMock.printedValues[0] == "First if")
        assert(printFunctionMock.printedValues[1] == "Second if")
    }

    @Test
    fun test6() {
        val interpreter = Interpreter.Factory.createDefault()
        val tree1 = AssignationAST(
            DeclarationAST(
                "someString",
                STR,
                true,
            ),
            BinaryOperationAST(
                LiteralAST(StrValue("some")),
                LiteralAST(NumValue(1.0)),
                ADD,
            ),
        )
        val tree2 = AssignationAST(
            DeclarationAST(
                "bool",
                BOOL,
                true,
            ),
            LiteralAST(BoolValue(true)),
        )
        interpreter.interpret(tree1)
        interpreter.interpret(tree2)
        val tree4 = ReAssignationAST(
            "someString",
            LiteralAST(StrValue("newSomeString")),
        )
        val tree5 = ReAssignationAST(
            "bool",
            LiteralAST(BoolValue(false)),
        )
        val ifTree = IfAST(
            VariableAST("bool"),
            listOf(
                tree4,
                tree5,
            ),
        )
        interpreter.interpret(ifTree)
        assert((interpreter.getMemory().getValue("someString") as StrValue).value == "newSomeString")
        assert((interpreter.getMemory().getValue("bool") as BoolValue).value == false)
    }
}
