// import ingsis.printscript.parser.implementations.Parser
// import org.junit.jupiter.api.Test
// import kotlin.test.assertFailsWith
//
// class ValidationTest {
//
//    private val parser = Parser()
//
//    @Test
//    fun emptyListShouldThrowError() {
//        assertFailsWith<Exception>(
//            message = "ingsis.printscript.utilities.types.Token list should be larger",
//            block = { parser.parse(emptyList()) }
//        )
//    }
//
//    @Test
//    fun notLetInListShouldThrowError() {
//        val errorList = listOf(
//            LeafToken.IDENTIFIER("name"),
//            LeafToken.TYPE(Type.StringType),
//            NodeToken.ASSIGNATION,
//            LeafToken.LITERAL(AvailableTypes.String("Fede")),
//            UtilToken.SEMICOLON
//        )
//
//        assertFailsWith<Exception>(
//            message = "ingsis.printscript.utilities.types.Token list should start with let",
//            block = { parser.parse(errorList) }
//        )
//    }
// }
