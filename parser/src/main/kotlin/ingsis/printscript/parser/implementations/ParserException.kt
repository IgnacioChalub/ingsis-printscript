package ingsis.printscript.parser.implementations

class ParserException : Exception {
    constructor(token: String, line: Int) : super(Exception("Error: Could not parse due to missing $token in line $line"))
    constructor(token: String) : super("Error: Could not find $token")
}
