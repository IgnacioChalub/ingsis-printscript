import implementation.Parser // ktlint-disable filename
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class MainTest {

    private val parser = Parser()

    @Test
    fun normalExpressionShouldReturnTree() {
        val tokenList = listOf(
            UtilToken.LET_KEY_WORD,
            LeafToken.IDENTIFIER("name"),
            LeafToken.TYPE(Type.StringType),
            NodeToken.ASSIGNATION,
            LeafToken.LITERAL(AvailableTypes.String("Fede")),
            UtilToken.SEMICOLON
        )

        val expectedTree = AST(
            NodeToken.ASSIGNATION,
            AST(
                NodeToken.COLON,
                Leaf(LeafToken.IDENTIFIER("name")),
                Leaf(LeafToken.TYPE(Type.StringType))
            ),
            Leaf(
                LeafToken.LITERAL(AvailableTypes.String("Fede"))
            )
        )

        assertTrue { expectedTree.equalsTree(parser.parse(tokenList)) }
    }
}
