package ingsis.printscript.cli

import ingsis.printscript.analyser.Analyser
import ingsis.printscript.interpreter.Interpreter
import ingsis.printscript.interpreter.InterpreterVisitor
import ingsis.printscript.interpreter.PrintFunction
import ingsis.printscript.interpreter.ReadInputFunction
import ingsis.printscript.lexer.Lexer
import ingsis.printscript.parser.implementations.Parser
import ingsis.printscript.utilities.enums.Value
import ingsis.printscript.utilities.enums.Version
import kotlinx.cli.*
import java.io.File
import java.io.FileInputStream

enum class MenuOptions {
    Validation,
    Execution,
    REPL,
    Formatting,
    Analyzing,
}

fun runCLI(operation: MenuOptions, input: String?, version: String) {
    return when (operation) {
        MenuOptions.Execution -> executeProgram(input, version)
        MenuOptions.REPL -> executeREPL(version)
        MenuOptions.Analyzing -> executeAnalyzer(input, version)
        MenuOptions.Formatting -> executeFormatter(input)
        MenuOptions.Validation -> executeValidation(input, version)
    }
}

private fun readStatements(fileInputStream: FileInputStream): List<String> {
    var statement = ""
    val nextStatement = mutableListOf<String>()
    var depth = 0
    while (true) {
        val byte = fileInputStream.read()
        if (byte == -1) break // end of file
        val char = byte.toChar()
        if (char == '\n') { // ignore new lines
            continue
        } else if (char == ';' && depth == 0) {
            nextStatement.add("$statement;")
            statement = ""
            continue
        } else if (char == '{') {
            depth++
        } else if (char == '}') depth--
        statement += char // add char to statement
    }
    return nextStatement
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

    try{
        val sentences = readStatements(FileInputStream(file))
        sentences.forEach { interpreter.interpret(parser.parse(lexer.tokenize(it))) }
    }catch (e: Throwable){
        println(e.message)
    }
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
            val tokenList = lexer.tokenize(content)
            interpreter.interpret(parser.parse(tokenList))
        } catch (e: Throwable) {
            println("ERROR: " + e.message)
        }
    }
}

fun executeValidation(input: String?, version: String) {
    val file = File(input!!)

    val lexer = Lexer()
    val parser = Parser(
        when (version) {
            "1.0" -> Version.VERSION_1_0
            "1.1" -> Version.VERSION_1_1
            else -> throw Exception("Version not found")
        },
    )
    val inter = Interpreter.Factory.createDefault()
    val interpreter = Interpreter(
        InterpreterVisitor(
            inter.getMemory(), object : PrintFunction {
                override fun print(value: Value) {
                    return
                }
            },
            object : ReadInputFunction {
                override fun read(message: String): String {
                    return ""
                }
            }))
    try{
        val sentences = readStatements(FileInputStream(file))
        sentences.forEach { interpreter.interpret(parser.parse(lexer.tokenize(it))) }
    }catch (e: Throwable){
        println(e.message)
    }
    println("Is Valid!")
}

fun executeAnalyzer(input: String?, version: String) {
    val file = File(input!!)
    val analyzer = Analyser.Factory.getDefault(emptyList())
    val lexer = Lexer()
    val parser = Parser(when (version) {
        "1.0" -> Version.VERSION_1_0
        "1.1" -> Version.VERSION_1_1
        else -> throw Exception("Version not found")
    },)
    try {
        val sentences = readStatements(FileInputStream(file))
        sentences.forEach { analyzer.analyse(parser.parse(lexer.tokenize(it)))}
    }catch (e: Throwable) {
        println(e.message)
    }
}

fun executeFormatter(input: String?) {
    // TODO add formatter
    println("Formatter")
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

    parser.parse(args)
    runCLI(operation, input, version)
}
