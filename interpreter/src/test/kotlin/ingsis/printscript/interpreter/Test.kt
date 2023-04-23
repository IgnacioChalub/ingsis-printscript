package ingsis.printscript.interpreter

import ingsis.printscript.utilities.enums.*
import ingsis.printscript.utilities.visitor.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Test {

    @Test
    fun shouldAssignExpressionToVariable() {
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
    fun shouldAssignStringConcatenationToVariable() {
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
    fun shouldAssignStringConcatenationOfVariablesToNewVariable() {
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
    fun shouldPrintVariable() {
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
        val interpreter = Interpreter.Factory.createMock(printFunctionMock, ReadInputFunctionImpl)
        interpreter.interpret(tree1)
        interpreter.interpret(tree2)
        assert(printFunctionMock.printedValue == "some1.0")
    }

    @Test
    fun shouldEnterIfStatement() {
        val tree = IfAST(
            LiteralAST(BoolValue(true)),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("First if")),
                ),
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(NumValue(1.0)),
                ),
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(BoolValue(true)),
                ),
            ),
        )
        val printFunctionMock = PrintManyFunctionMock(mutableListOf())
        val interpreter = Interpreter.Factory.createMock(printFunctionMock, ReadInputFunctionImpl)
        interpreter.interpret(tree)
        assert(printFunctionMock.printedValues[0] == "First if")
        assert(printFunctionMock.printedValues[1] == "1.0")
        assert(printFunctionMock.printedValues[2] == "true")
    }

    @Test
    fun shouldReAssignInIf() {
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

    @Test
    fun shouldReadStringInput() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadStringFunctionMock)
        val tree = AssignationAST(
            DeclarationAST(
                "someString",
                STR,
                true,
            ),
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some input: ")),
            ),
        )
        interpreter.interpret(tree)
        assert((interpreter.getMemory().getValue("someString") as StrValue).value == "Some string")
    }

    @Test
    fun shouldReadNumInput() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadNumFunctionMock)
        val tree = AssignationAST(
            DeclarationAST(
                "someNum",
                NUM,
                true,
            ),
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some input: ")),
            ),
        )
        interpreter.interpret(tree)
        assert((interpreter.getMemory().getValue("someNum") as NumValue).value == 1.0)
    }

    @Test
    fun shouldReadBoolInput() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadBoolFunctionMock)
        val tree = AssignationAST(
            DeclarationAST(
                "someBool",
                BOOL,
                true,
            ),
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some input: ")),
            ),
        )
        interpreter.interpret(tree)
        assert((interpreter.getMemory().getValue("someBool") as BoolValue).value == true)
    }

    @Test
    fun shouldThrowErrorIfValueProvidedIsNotBoolean() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadStringFunctionMock)
        val tree = AssignationAST(
            DeclarationAST(
                "someBool",
                BOOL,
                true,
            ),
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some input: ")),
            ),
        )
        try {
            interpreter.interpret(tree)
        } catch (e: Error) {
            assert(e.message == "Value provided is not a boolean")
            return
        }
        assert(false)
    }

    @Test
    fun shouldThrowErrorIfValueProvidedIsNotNum() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadStringFunctionMock)
        val tree = AssignationAST(
            DeclarationAST(
                "someNum",
                NUM,
                true,
            ),
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some input: ")),
            ),
        )
        try {
            interpreter.interpret(tree)
        } catch (e: Error) {
            assert(e.message == "Value provided is not a number")
            return
        }
        assert(false)
    }

    @Test
    fun shouldGoToElseIfBooleanIsFalse() {
        val ifTree = IfElseAST(
            LiteralAST(BoolValue(false)),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("yes")),
                ),
            ),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("no")),
                ),
            ),
        )
        val printFunctionMock = PrintFunctionMock("")
        val interpreter = Interpreter.Factory.createMock(printFunctionMock, ReadInputFunctionImpl)
        interpreter.interpret(ifTree)
        assert(printFunctionMock.printedValue === "no")
    }

    @Test()
    fun shouldPrintBoolean() {
        // print(true);
        val tree = UnaryOperationAST(
            PRINT,
            LiteralAST(BoolValue(true)),
        )
        val printFunctionMock = PrintFunctionMock("")
        val interpreter = Interpreter.Factory.createMock(printFunctionMock, ReadInputFunctionImpl)
        interpreter.interpret(tree)
        assert(printFunctionMock.printedValue == "true")
    }

    @Test()
    fun shouldPrintNum() {
        // print(1);
        val tree = UnaryOperationAST(
            PRINT,
            LiteralAST(NumValue(1.0)),
        )
        val printFunctionMock = PrintFunctionMock("")
        val interpreter = Interpreter.Factory.createMock(printFunctionMock, ReadInputFunctionImpl)
        interpreter.interpret(tree)
        assert(printFunctionMock.printedValue == "1.0")
    }

    @Test
    fun shouldReadLines() {
        val tree1 = UnaryOperationAST(
            PRINT,
            LiteralAST(NumValue(1.0)),
        )
        val tree2 = UnaryOperationAST(
            PRINT,
            LiteralAST(BoolValue(false)),
        )
        val tree3 = UnaryOperationAST(
            PRINT,
            LiteralAST(StrValue("some")),
        )
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadInputFunctionImpl)
        interpreter.interpret(tree1)
        interpreter.interpret(tree2)
        interpreter.interpret(tree3)
    }

    @Test
    fun shouldReAssignString() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadStringFunctionMock)
        val tree1 = AssignationAST(
            DeclarationAST(
                "someString",
                STR,
                true,
            ),
            LiteralAST(StrValue("some value")),
        )
        val tree2 = ReAssignationAST(
            "someString",
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some input: ")),
            ),
        )
        interpreter.interpret(tree1)
        interpreter.interpret(tree2)
        assert((interpreter.getMemory().getValue("someString") as StrValue).value === "Some string")
    }

    @Test()
    fun shouldReAssignBool() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadBoolFunctionMock)
        val tree1 = AssignationAST(
            DeclarationAST(
                "someBool",
                BOOL,
                true,
            ),
            LiteralAST(BoolValue(false)),
        )
        val tree2 = ReAssignationAST(
            "someBool",
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some message")),
            ),
        )
        interpreter.interpret(tree1)
        interpreter.interpret(tree2)
        assert((interpreter.getMemory().getValue("someBool") as BoolValue).value === true)
    }

    @Test()
    fun shouldReassignNum() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadNumFunctionMock)
        val tree1 = AssignationAST(
            DeclarationAST(
                "someNum",
                NUM,
                true,
            ),
            LiteralAST(NumValue(2.0)),
        )
        val tree2 = ReAssignationAST(
            "someNum",
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some message")),
            ),
        )
        interpreter.interpret(tree1)
        interpreter.interpret(tree2)
        assert((interpreter.getMemory().getValue("someNum") as NumValue).value === 1.0)
    }

    @Test
    fun shouldDivideNumbers() {
        // let someNumber: Number = 10/2;
        val tree = AssignationAST(
            DeclarationAST(
                "someNumber",
                NUM,
                false,
            ),
            BinaryOperationAST(
                LiteralAST(NumValue(10.0)),
                LiteralAST(NumValue(2.0)),
                DIV,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        interpreter.interpret(tree)
        assert((interpreter.getMemory().getValue("someNumber") as NumValue).value == 5.0)
    }

    @Test
    fun shouldSubNumbers() {
        // let someNumber: Number = 10-2;
        val tree = AssignationAST(
            DeclarationAST(
                "someNumber",
                NUM,
                false,
            ),
            BinaryOperationAST(
                LiteralAST(NumValue(10.0)),
                LiteralAST(NumValue(2.0)),
                SUB,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        interpreter.interpret(tree)
        assert((interpreter.getMemory().getValue("someNumber") as NumValue).value == 8.0)
    }

    @Test
    fun shouldThrowErrorIfAssignStrToNum() {
        val tree = AssignationAST(
            DeclarationAST(
                "someNumber",
                NUM,
                false,
            ),
            LiteralAST(StrValue("some value")),
        )
        val interpreter = Interpreter.Factory.createDefault()
        val err = assertThrows<Error> {
            interpreter.interpret(tree)
        }
        assert(err.message === "Invalid type assignation")
    }

    @Test()
    fun shouldThrowErrorIfReAssignBoolToNum() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadBoolFunctionMock)
        val tree1 = AssignationAST(
            DeclarationAST(
                "someNum",
                NUM,
                true,
            ),
            LiteralAST(NumValue(2.0)),
        )
        val tree2 = ReAssignationAST(
            "someNum",
            LiteralAST(BoolValue(true)),
        )
        interpreter.interpret(tree1)
        val err = assertThrows<Error> {
            interpreter.interpret(tree2)
        }
        assert(err.message === "New value should have the same type")
    }

    @Test()
    fun shouldEnterElseInIf() {
        val ifTree = IfElseAST(
            LiteralAST(BoolValue(true)),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("yes")),
                ),
            ),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("no")),
                ),
            ),
        )
        val printFunctionMock = PrintFunctionMock("")
        val interpreter = Interpreter.Factory.createMock(printFunctionMock, ReadInputFunctionImpl)
        interpreter.interpret(ifTree)
        assert(printFunctionMock.printedValue === "yes")
    }

    @Test()
    fun shouldThrowErrorIfValueInIfStatmentISNotBool() {
        val ifTree = IfElseAST(
            LiteralAST(StrValue("some ")),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("yes")),
                ),
            ),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("no")),
                ),
            ),
        )
        val printFunctionMock = PrintFunctionMock("")
        val interpreter = Interpreter.Factory.createMock(printFunctionMock, ReadInputFunctionImpl)
        val err = assertThrows<Error> {
            interpreter.interpret(ifTree)
        }
        assert(err.message === "Invalid if statement condition")
    }

    @Test()
    fun shouldEnterIfTruthBlockIsVariableIsTrue() {
        val tree1 = AssignationAST(
            DeclarationAST(
                "bool",
                BOOL,
                true,
            ),
            LiteralAST(BoolValue(true)),
        )
        val ifTree = IfElseAST(
            VariableAST("bool"),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("yes")),
                ),
            ),
            listOf(
                UnaryOperationAST(
                    PRINT,
                    LiteralAST(StrValue("no")),
                ),
            ),
        )
        val printFunctionMock = PrintFunctionMock("")
        val interpreter = Interpreter.Factory.createMock(printFunctionMock, ReadInputFunctionImpl)
        interpreter.interpret(tree1)
        interpreter.interpret(ifTree)
        assert(printFunctionMock.printedValue === "yes")
    }

    @Test
    fun shouldAddString() {
        val tree = AssignationAST(
            DeclarationAST(
                "some",
                STR,
                false,
            ),
            BinaryOperationAST(
                LiteralAST(StrValue("some ")),
                LiteralAST(StrValue("value")),
                ADD,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        interpreter.interpret(tree)
        assert((interpreter.getMemory().getValue("some") as StrValue).value == "some value")
    }

    @Test
    fun shouldAddStringToNum() {
        val tree = AssignationAST(
            DeclarationAST(
                "some",
                STR,
                false,
            ),
            BinaryOperationAST(
                LiteralAST(StrValue("some ")),
                LiteralAST(NumValue(1.0)),
                ADD,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        interpreter.interpret(tree)
        assert((interpreter.getMemory().getValue("some") as StrValue).value == "some 1.0")
    }

    @Test
    fun shouldAddNumToString() {
        val tree = AssignationAST(
            DeclarationAST(
                "some",
                STR,
                false,
            ),
            BinaryOperationAST(
                LiteralAST(NumValue(1.0)),
                LiteralAST(StrValue("some ")),
                ADD,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        interpreter.interpret(tree)
        assert((interpreter.getMemory().getValue("some") as StrValue).value == "1.0some ")
    }

    @Test
    fun shouldThrowErrorIfDivideNumAndStr() {
        val tree = AssignationAST(
            DeclarationAST(
                "some",
                STR,
                false,
            ),
            BinaryOperationAST(
                LiteralAST(NumValue(1.0)),
                LiteralAST(StrValue("some ")),
                DIV,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        val err = assertThrows<Error> {
            interpreter.interpret(tree)
        }
        assert(err.message === "Can not divide values")
    }

    @Test
    fun shouldThrowErrorIfMulNumAndStr() {
        val tree = AssignationAST(
            DeclarationAST(
                "some",
                STR,
                false,
            ),
            BinaryOperationAST(
                LiteralAST(NumValue(1.0)),
                LiteralAST(StrValue("some ")),
                MUL,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        val err = assertThrows<Error> {
            interpreter.interpret(tree)
        }
        assert(err.message === "Can not multiply values")
    }

    @Test
    fun shouldThrowErrorIfSubtractNumAndStr() {
        val tree = AssignationAST(
            DeclarationAST(
                "some",
                STR,
                false,
            ),
            BinaryOperationAST(
                LiteralAST(NumValue(1.0)),
                LiteralAST(StrValue("some ")),
                SUB,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        val err = assertThrows<Error> {
            interpreter.interpret(tree)
        }
        assert(err.message === "Can not subtract values")
    }

    @Test
    fun shouldThrowErrorIfSumBoolAndStr() {
        val tree = AssignationAST(
            DeclarationAST(
                "some",
                STR,
                false,
            ),
            BinaryOperationAST(
                LiteralAST(BoolValue(true)),
                LiteralAST(StrValue("some ")),
                ADD,
            ),
        )
        val interpreter = Interpreter.Factory.createDefault()
        val err = assertThrows<Error> {
            interpreter.interpret(tree)
        }
        assert(err.message === "Can not sum values")
    }

    @Test
    fun shouldThrowErrorIfReadMessageIsNum() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadStringFunctionMock)
        val tree = AssignationAST(
            DeclarationAST(
                "someString",
                STR,
                true,
            ),
            UnaryOperationAST(
                READINPUT,
                LiteralAST(NumValue(1.0)),
            ),
        )
        val err = assertThrows<Error> {
            interpreter.interpret(tree)
        }
        assert(err.message === "Read input message should be a string")
    }

    @Test
    fun shouldThrowErrorIfReadMessageIsFunction() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadStringFunctionMock)
        val tree = AssignationAST(
            DeclarationAST(
                "someString",
                STR,
                true,
            ),
            UnaryOperationAST(
                READINPUT,
                UnaryOperationAST(
                    READINPUT,
                    LiteralAST(StrValue("some")),
                ),
            ),
        )
        val err = assertThrows<Error> {
            interpreter.interpret(tree)
        }
        assert(err.message === "Invalid message for read input")
    }

    @Test()
    fun shouldThrowErrorIfReAssignStrToNum() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadStringFunctionMock)
        val tree1 = AssignationAST(
            DeclarationAST(
                "someNum",
                NUM,
                true,
            ),
            LiteralAST(NumValue(2.0)),
        )
        val tree2 = ReAssignationAST(
            "someNum",
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some message")),
            ),
        )
        interpreter.interpret(tree1)
        val err = assertThrows<Error> {
            interpreter.interpret(tree2)
        }
        assert(err.message === "Value provided is not a number")
    }

    @Test()
    fun shouldThrowErrorIfReAssignStrToBool() {
        val interpreter = Interpreter.Factory.createMock(PrintFunctionImpl, ReadStringFunctionMock)
        val tree1 = AssignationAST(
            DeclarationAST(
                "someBool",
                BOOL,
                true,
            ),
            LiteralAST(BoolValue(true)),
        )
        val tree2 = ReAssignationAST(
            "someBool",
            UnaryOperationAST(
                READINPUT,
                LiteralAST(StrValue("Some message")),
            ),
        )
        interpreter.interpret(tree1)
        val err = assertThrows<Error> {
            interpreter.interpret(tree2)
        }
        assert(err.message === "Value provided is not a boolean")
    }
}
