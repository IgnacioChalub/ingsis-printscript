package ingsis.printscript.cli

import ingsis.printscript.interpreter.Interpreter
import ingsis.printscript.lexer.Lexer
import ingsis.printscript.parser.implementations.Parser
import kotlinx.cli.* // ktlint-disable no-wildcard-imports
import java.io.File

object App {
    const val appName = "Printscript CLI"
    const val version = "0.0.1"
}

enum class MenuOptions {
    Execution,
}

fun runCLI(operation: MenuOptions, input: String, version: String, config: String?) {
    return when (operation) {
        MenuOptions.Execution -> executeProgram(input, version)
    }
}

fun executeProgram(input: String, version: String) {
    val file = File(input)
    val tokenList = Lexer(file.readText()).tokenize()
    val parser = Parser()
    val interpreter = Interpreter.Factory.createDefault()
    val asTree = parser.parse(tokenList)
    interpreter.interpret(asTree)
}

fun main(args: Array<String>) {
    val parser = ArgParser("printscript")
    val operation by parser.option(
        ArgType.Choice<MenuOptions>(),
        shortName = "o",
        description = "Operation to run",
    ).default(MenuOptions.Execution)
    val input by parser.option(ArgType.String, shortName = "i", description = "Input file").required()
    val version by parser.option(
        ArgType.Choice(listOf("1.0"), { it }),
        shortName = "v",
        description = "Version of program",
    ).default("1.0")
    val config by parser.option(ArgType.String, shortName = "c", description = "Config File")

    parser.parse(args)
    runCLI(operation, input, version, config)
}
