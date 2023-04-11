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
//            message = "ingsis.printscript.utilities.types.ingsis.printscript.utilities.enums.Token list should be larger",
//            block = { parser.parse(emptyList()) }
//        )
//    }
//
//    @Test
//    fun notLetInListShouldThrowError() {
//        val errorList = listOf(
//            LeafToken.ingsis.printscript.utilities.enums.IDENTIFIER("name"),
//            LeafToken.TYPE(ingsis.printscript.utilities.enums.Type.StringType),
//            NodeToken.ingsis.printscript.utilities.enums.ASSIGNATION,
//            LeafToken.LITERAL(AvailableTypes.String("Fede")),
//            ingsis.printscript.utilities.enums.UtilToken.ingsis.printscript.utilities.enums.SEMICOLON
//        )
//
//        assertFailsWith<Exception>(
//            message = "ingsis.printscript.utilities.types.ingsis.printscript.utilities.enums.Token list should start with let",
//            block = { parser.parse(errorList) }
//        )
//    }
// }
