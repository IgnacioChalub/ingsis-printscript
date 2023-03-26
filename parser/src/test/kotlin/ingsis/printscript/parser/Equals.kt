import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EqualsTest {

    @Test
    fun leafWithIdentifierAndSameIdentifierShouldReturnTrue() {
        assertTrue { Leaf(LeafToken.IDENTIFIER("test")).equalsTree(Leaf(LeafToken.IDENTIFIER("test"))) }
    }

    @Test
    fun leafWithIdentifierAndDifferentIdentifierShouldReturnFalse() {
        assertFalse { Leaf(LeafToken.IDENTIFIER("test")).equalsTree(Leaf(LeafToken.IDENTIFIER("hello"))) }
    }

    @Test
    fun leafWithIdentifierAndDifferentTypeShouldReturnFalse() {
        assertFalse { Leaf(LeafToken.IDENTIFIER("test")).equalsTree(Leaf(LeafToken.TYPE(Type.StringType))) }
    }

    @Test
    fun astsWithSameValuesShouldReturnTrue() {
        val left = AST(
            NodeToken.ASSIGNATION,
            Leaf(LeafToken.IDENTIFIER("name")),
            Leaf(
                LeafToken.LITERAL(AvailableTypes.String("test")),
            ),
        )
        val right = AST(
            NodeToken.ASSIGNATION,
            Leaf(LeafToken.IDENTIFIER("name")),
            Leaf(
                LeafToken.LITERAL(AvailableTypes.String("test")),
            ),
        )

        assertTrue { left.equalsTree(right) }
    }

    @Test
    fun astsWithDifferentNodesShouldReturnFalse() {
        val left = AST(
            NodeToken.ASSIGNATION,
            Leaf(LeafToken.IDENTIFIER("name")),
            Leaf(
                LeafToken.LITERAL(AvailableTypes.String("test")),
            ),
        )
        val right = AST(
            NodeToken.ASSIGNATION,
            Leaf(LeafToken.IDENTIFIER("name")),
            Leaf(
                LeafToken.LITERAL(AvailableTypes.String("test")),
            ),
        )

        assertFalse { left.equalsTree(right) }
    }

    @Test
    fun astsWithDifferentLeafShouldReturnFalse() {
        val left = AST(
            NodeToken.ASSIGNATION,
            Leaf(LeafToken.IDENTIFIER("name")),
            Leaf(
                LeafToken.LITERAL(AvailableTypes.String("test")),
            ),
        )
        val right = AST(
            NodeToken.ASSIGNATION,
            Leaf(LeafToken.IDENTIFIER("hello")),
            Leaf(
                LeafToken.LITERAL(AvailableTypes.String("test")),
            ),
        )

        assertFalse { left.equalsTree(right) }
    }

    @Test
    fun astsWithALeafAndAnASTShouldReturnFalse() {
        val left = AST(
            NodeToken.ASSIGNATION,
            Leaf(LeafToken.IDENTIFIER("name")),
            Leaf(
                LeafToken.LITERAL(AvailableTypes.String("test")),
            ),
        )
        val right = AST(
            NodeToken.ASSIGNATION,
            AST(
                NodeToken.ASSIGNATION,
                Leaf(LeafToken.IDENTIFIER("name")),
                Leaf(
                    LeafToken.LITERAL(AvailableTypes.String("test")),
                ),
            ),
            Leaf(
                LeafToken.LITERAL(AvailableTypes.String("test")),
            ),
        )

        assertFalse { left.equalsTree(right) }
    }
}
