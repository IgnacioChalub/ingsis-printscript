package ingsis.printscript.formatter

class Formatter(private val indentationSpaces: Int = 4) {

    fun format(code: String): String {
        val lines = code.lines()
        val formattedLines = mutableListOf<String>()
        var currentIndentation = 0

        for (line in lines) {
            val trimmedLine = line.trim()
            if (trimmedLine.startsWith("if")) {
                formattedLines.add(" ".repeat(currentIndentation) + trimmedLine.replace(" {", "{").replace("{", " {"))
                currentIndentation += indentationSpaces
            } else if (trimmedLine.startsWith("}")) {
                currentIndentation -= indentationSpaces
                formattedLines.add(" ".repeat(currentIndentation) + trimmedLine)
            } else {
                formattedLines.add(" ".repeat(currentIndentation) + trimmedLine)
            }
        }

        return formattedLines.joinToString(separator = "\n")
    }
}
