package ru.spbau.mit.softwaredesign.shell.std

import com.jshmrsn.karg.parseArguments
import ru.spbau.mit.softwaredesign.shell.Environment
import ru.spbau.mit.softwaredesign.shell.utils.ArgumentsException
import java.io.FileNotFoundException

/**
 * Cat file
 * @param args array of arguments
 * @param input stdin
 * @return pair of exit code and stdout
 */
fun cat(args: Array<String>, input: String): Pair<Int, String> {
    if (args.size != 1) {
        throw ArgumentsException("cat", 1, args.size)
    }

    val file = Environment.readFile(args[0]) ?: throw FileNotFoundException(args[0])
    return Pair(0, file)
}

/**
 * Word count
 * @param args array of arguments
 * @param input stdin
 * @return pair of exit code and stdout
 */
fun wc(args: Array<String>, input: String): Pair<Int, String> {
    val wordCount = (when (args.size) {
        0 -> input.split(" ").size
        1 -> Environment.readFile(args[1])?.split(" ")?.size ?: throw FileNotFoundException(args[1])
        else -> throw ArgumentsException("wc", 0, args.size)
    }).toString()

    return Pair(0, wordCount)
}

/**
 * Current path
 * @param args array of arguments
 * @param input stdin
 * @return pair of exit code and stdout
 */
fun pwd(args: Array<String>, input: String): Pair<Int, String> {
    if (args.isNotEmpty()) {
        throw ArgumentsException("pwd", 0, args.size)
    }

    val path = Environment.path.absolutePath
    return Pair(0, path)
}

/**
 * @param args array of arguments
 * @param input stdin
 * @return pair of exit code and stdout
 */
fun echo(args: Array<String>, input: String): Pair<Int, String> {
    return Pair(0, args.joinToString(" "))
}

/**
 * @param args array of arguments
 * @param input stdin
 * @return pair of exit code and stdout
 */
fun exit(args: Array<String>, input: String): Pair<Int, String> {
    if (args.size != 0) {
        throw ArgumentsException("exit", 0, args.size)
    }

    return Pair(1, "")
}

/**
 * Grep
 * @param args array of arguments
 * @param input stdin
 * @return pair of exit code and stdout
 */
fun grep(args: Array<String>, input: String): Pair<Int, String> {
    val arguments = parseArguments(args, ::GrepArgumentParser)
    val options = mutableSetOf<RegexOption>()
    if (arguments.caseInsensitive) {
        options.add(RegexOption.IGNORE_CASE)
    }

    val pattern = Regex(arguments.pattern[0], options)
    val source = if (arguments.pattern.size == 2) {
        Environment.readFile(arguments.pattern[1]) ?: arguments.pattern[1]
    } else input

    val lines = source.split("\n").toTypedArray()
    val linesCount = arguments.printAfter?.toInt() ?: 1

    val builder = StringBuilder()
    val printer = fun(lineNumber: Int, line: String) {
        if (arguments.word) {
            builder.appendln(pattern.find(line)!!.value)
            return
        }

        for (i in lineNumber..Math.min(lineNumber + linesCount - 1, lines.size - 1)) {
            builder.appendln(lines[i])
        }
    }

    lines.forEachIndexed { i, s ->
        if (pattern.containsMatchIn(s)) {
            printer(i, s)
        }
    }

    return Pair(0, builder.toString())
}
