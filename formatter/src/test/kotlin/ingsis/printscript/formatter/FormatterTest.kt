// package ingsis.printscript.formatter
//
// import ingsis.printscript.lexer.Lexer
// import ingsis.printscript.parser.implementations.Parser
// import ingsis.printscript.utilities.enums.*
// import ingsis.printscript.utilities.visitor.*
// import org.junit.jupiter.api.Assertions.assertEquals
// import org.junit.jupiter.api.Test
//
// class FormatterTest {
//    val lexer = Lexer()
//    val parser = Parser(Version.VERSION_1_1)
//
//    @Test
//    fun `test IfAST formatting`() {
//        val formatterVisitor = FormatterVisitor()
//        val ifAST = IfAST(
//            LiteralAST(BoolValue(true)),
//            listOf(
//                AssignationAST(
//                    DeclarationAST("x", STR, true),
//                    LiteralAST(NumValue(1.0)),
//                ),
//            ),
//        )
//        ifAST.accept(formatterVisitor)
//        val result = formatterVisitor.toString()
//        assertEquals("    if (true) {    var x: STR = 1.0;\n}", result)
//    }
//
//    @Test
//    fun `test IfElseAST formatting`() {
//        val formatterVisitor = FormatterVisitor()
//        val ifElseAST = IfElseAST(
//            LiteralAST(BoolValue(false)),
//            listOf(
//                AssignationAST(
//                    DeclarationAST("x", NUM, true),
//                    LiteralAST(NumValue(1.0)),
//                ),
//            ),
//            listOf(
//                ReAssignationAST(
//                    "x",
//                    LiteralAST(NumValue(0.0)),
//                ),
//            ),
//        )
//
//        ifElseAST.accept(formatterVisitor)
//        val result = formatterVisitor.toString()
//
//        assertEquals("    if (false) {    var x: NUM = 1.0;\n} else {    x = 0.0;\n}", result)
//    }
//
//    @Test
//    fun `test BinaryOperationAST formatting`() {
//        val formatterVisitor = FormatterVisitor()
//        val binaryOperationAST = BinaryOperationAST(
//            LiteralAST(NumValue(2.0)),
//            LiteralAST(NumValue(3.0)),
//            ingsis.printscript.utilities.enums.ADD,
//        )
//
//        binaryOperationAST.accept(formatterVisitor)
//        val result = formatterVisitor.toString()
//
//        assertEquals("2.0 + 3.0", result)
//    }
//
//    @Test
//    fun `test UnaryOperationAST formatting`() {
//        val formatterVisitor = FormatterVisitor()
//        val unaryOperationAST = UnaryOperationAST(
//            PRINT,
//            LiteralAST(StrValue("Hello, world!")),
//        )
//
//        unaryOperationAST.accept(formatterVisitor)
//        val result = formatterVisitor.toString()
//
//        assertEquals("PRINT(Hello, world!)", result)
//    }
//
//    @Test
//    fun `test AssignationAST formatting`() {
//        val formatterVisitor = FormatterVisitor()
//        val expected = parser.parse(lexer.tokenize("           let x: Number =      10.0;"))
//        expected.accept(formatterVisitor)
//
//        assertEquals("let x: Number = 10.0;\n", formatterVisitor.toString())
//    }
//
//    @Test
//    fun `test ReAssignationAST formatting`() {
//        val formatterVisitor = FormatterVisitor()
//        val reAssignationAST = ReAssignationAST(
//            "x",
//            LiteralAST(NumValue(42.0)),
//        )
//
//        reAssignationAST.accept(formatterVisitor)
//        val result = formatterVisitor.toString()
//
//        assertEquals("    x = 42.0;\n", result)
//    }
//
//    @Test
//    fun `test DeclarationAST formatting`() {
//        val formatterVisitor = FormatterVisitor()
//        val declarationAST = DeclarationAST("x", STR, true)
//
//        declarationAST.accept(formatterVisitor)
//        val result = formatterVisitor.toString()
//
//        assertEquals("var x: STR", result)
//    }
//
//    @Test
//    fun `test LiteralAST formatting`() {
//        val formatterVisitor = FormatterVisitor()
//        val literalAST = LiteralAST(NumValue(42.0))
//
//        literalAST.accept(formatterVisitor)
//        val result = formatterVisitor.toString()
//
//        assertEquals("42.0", result)
//    }
//
//    @Test
//    fun `test VariableAST formatting`() {
//        val formatterVisitor = FormatterVisitor()
//        val variableAST = VariableAST("x")
//
//        variableAST.accept(formatterVisitor)
//        val result = formatterVisitor.toString()
//
//        assertEquals("x", result)
//    }
//
//    /*    @Test
//        fun visitLiteralAST() {
//            val literal = LiteralAST(NumValue(42.0))
//            val formatter = FormatterVisitor()
//            val formattedLiteral = formatter.visitLiteralAST(literal)
//            assertEquals("42.0", formattedLiteral.toString())
//        }
//
//        @Test
//        fun visitVariableAST() {
//            val variable = VariableAST("x")
//            val formatter = FormatterVisitor()
//            val formattedVariable = formatter.visitVariableAST(variable)
//            assertEquals("x", formattedVariable.toString())
//        }*/
// }
