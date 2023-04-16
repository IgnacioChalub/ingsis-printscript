package ingsis.printscript.cli

import ingsis.printscript.interpreter.Interpreter
import ingsis.printscript.lexer.Lexer
import ingsis.printscript.parser.implementations.Parser
import ingsis.printscript.utilities.enums.Version
import kotlinx.cli.* // ktlint-disable no-wildcard-imports
import java.io.File

object App {
    const val appName = "Printscript CLI"
    const val version = "0.0.1"
}

enum class MenuOptions {
    Execution,
    REPL,
}

fun runCLI(operation: MenuOptions, input: String?, version: String, config: String?) {
    return when (operation) {
        MenuOptions.Execution -> executeProgram(input, version)
        MenuOptions.REPL -> executeREPL(version)
    }
}

fun executeProgram(input: String?, version: String) {
    val file = File(input!!)

    val lexer = Lexer()
    val parser = Parser(
        when (version) {
            "1.0" -> Version.VERSION_1_0
            "1.1" -> Version.VERSION_1_1
            else -> throw Exception("Version not found")
        },
    )
    val interpreter = Interpreter.Factory.createDefault()

    val content = file.readText().replace("\n", "")
    val sentences = if (content.last() == ';') { content.split(";").dropLast(1) } else {
        content.split(";")
    }
    val tokenLists = sentences.map { lexer.tokenize("$it;") }
    val asTrees = tokenLists.map { parser.parse(it.map { a -> a.first }) }
    asTrees.forEach { interpreter.interpret(it) }
}

fun executeREPL(version: String) {
    val lexer = Lexer()
    val parser = Parser(
        when (version) {
            "1.0" -> Version.VERSION_1_0
            "1.1" -> Version.VERSION_1_1
            else -> throw Exception("Version not found")
        },
    )
    val interpreter = Interpreter.Factory.createDefault()
    var content = ""
    while (true) {
        print(">> ")
        content = readLine()!!
        if (content == "quit") break
        try {
            interpreter.interpret(parser.parse(lexer.tokenize(content).map { it.first }))
        } catch (e: Throwable) {
            println("ERROR: " + e.message)
        }
    }
}

fun main(args: Array<String>) {
    val parser = ArgParser("printscript")
    val operation by parser.option(
        ArgType.Choice<MenuOptions>(),
        shortName = "o",
        description = "Operation to run",
    ).default(MenuOptions.Execution)
    val input by parser.option(ArgType.String, shortName = "i", description = "Input file")
    val version by parser.option(
        ArgType.Choice(listOf("1.0", "1.1"), { it }),
        shortName = "v",
        description = "Version of program",
    ).default("1.0")
    val config by parser.option(ArgType.String, shortName = "c", description = "Config File")

    parser.parse(args)
    runCLI(operation, input, version, config)
}
