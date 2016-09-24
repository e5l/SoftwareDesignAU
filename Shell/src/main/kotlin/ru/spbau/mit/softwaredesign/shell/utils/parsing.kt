package ru.spbau.mit.softwaredesign.shell.utils

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.tree.ParseTreeWalker
import ru.spbau.mit.softwaredesign.shell.grammar.ShellLexer
import ru.spbau.mit.softwaredesign.shell.grammar.ShellParser

/**
 * Run lexer and parser over listener
 * Helper function
 * @param line input
 * @param: listener
 */
fun <T : ParseTreeListener> listenShellString(line: String, listener: T) {
    val lexer = ShellLexer(ANTLRInputStream(line.byteInputStream()))
    val parser = ShellParser(CommonTokenStream(lexer))
    ParseTreeWalker().walk(listener, parser.line())
}
