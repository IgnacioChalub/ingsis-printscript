package `interface`

import IAST
import Token

interface IParser {
    fun parse(tokenList: List<Token>): IAST
}
