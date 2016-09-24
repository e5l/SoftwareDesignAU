package ru.spbau.mit.softwaredesign.shell

import ru.spbau.mit.softwaredesign.shell.pipeline.AssignmentNode
import ru.spbau.mit.softwaredesign.shell.pipeline.CommandNode
import ru.spbau.mit.softwaredesign.shell.pipeline.ExecutableNode
import ru.spbau.mit.softwaredesign.shell.pipeline.Pipeline
import ru.spbau.mit.softwaredesign.shell.utils.NonZeroResultException
import ru.spbau.mit.softwaredesign.shell.utils.UnknownNodeType

/**
 * Command line executor.
 * Store shell state and embed commands, perform shell actions.
 */
object Executor {
    private val commands = mutableMapOf<String, (Array<String>, String) -> Pair<Int, String>>()

    /**
     * register embed command in shell
     * @param name command name
     * @param command command handler
     */
    fun register(name: String, command: (Array<String>, String) -> Pair<Int, String>) {
        commands[name] = command
    }

    /**
     * Execute parsed pipeline
     * @param pipeline pared pipeline
     * @return pair of return code and stdout of last command in pipeline
     */
    fun executePipeline(pipeline: Pipeline): Pair<Int, String> {
        var result = Pair(0, "")

        try {
            result = runExecutionLoop(pipeline)
        } catch (e: UnknownNodeType) {
            throw e
        } catch (e: Exception) {
            println(e.message!!)
        }

        return result
    }

    private fun runExecutionLoop(pipeline: Pipeline): Pair<Int, String> {
        var result = Pair(0, "")

        for (command in pipeline.commands) {
             result = execute(command, result.second)

            if (result.first != 0) {
                if (result.first < 0) {
                    throw NonZeroResultException(command.toString(), result.first)
                }

                return result
            }
        }

        return result
    }

    private fun execute(command: ExecutableNode, input: String): Pair<Int, String> = when (command) {
            is AssignmentNode -> {
                Environment[command.variable] = command.content
                Pair(0, input)
            }
            is CommandNode -> {
                val call = commands[command.name]?.invoke(command.args, input)
                if (call != null) {
                    call
                } else {
                    val task = "${command.name} ${command.args.joinToString(" ")}"
                    val process = Runtime.getRuntime().exec(task)
                    val code = process.waitFor()
                    Pair(code, process.inputStream.reader().readText())
                }
            }
            else -> throw UnknownNodeType(command.toString())
        }
}

