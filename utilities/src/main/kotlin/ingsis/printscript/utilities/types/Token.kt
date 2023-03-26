package ingsis.printscript.utilities.types

import ingsis.printscript.utilities.enums.TokenType

data class Token(val type: TokenType, val value: Any? = null)
