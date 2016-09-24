package ru.spbau.mit.softwaredesign.shell.pipeline

import ru.spbau.mit.softwaredesign.shell.grammar.ShellBaseListener
import ru.spbau.mit.softwaredesign.shell.grammar.ShellParser
import ru.spbau.mit.softwaredesign.shell.utils.ParseException
import ru.spbau.mit.softwaredesign.shell.utils.listenShellString

private class PipelineBuilder() : ShellBaseListener() {
    val nodes = mutableListOf<ExecutableNode>()

    override fun enterAssignment(ctx: ShellParser.AssignmentContext?) {
        super.enterAssignment(ctx)
        ctx ?: throw ParseException()

        val node = AssignmentNode(
                ctx.lhs().variable().name().text,
                ctx.rhs().text)

        nodes.add(node)
    }

    override fun enterCommand(ctx: ShellParser.CommandContext?) {
        super.enterCommand(ctx)
        ctx ?: throw ParseException()

        val args = ctx.argument().map { escapeArgument(it) }.toTypedArray()
        val node = CommandNode(
                ctx.identifier().text,
                args)

        nodes.add(node)
    }

    override fun enterArgument(ctx: ShellParser.ArgumentContext?) {
        super.enterArgument(ctx)
    }

    private fun escapeArgument(argument: ShellParser.ArgumentContext): String {
        val string = argument.string() ?: return argument.text
        return string.weak_string()?.content(0)?.text ?: string.strong_string()?.content(0)?.text ?: throw ParseException()
    }

    fun getPipeline() = Pipeline(nodes)
}


/**
 * Build annotated execution pipeline from string with expanded macroses
 * @param line input line
 * @return pipeline
 */
fun buildPipeline(line: String): Pipeline {
    val builder = PipelineBuilder()
    listenShellString(line, builder)

    return builder.getPipeline()
}
