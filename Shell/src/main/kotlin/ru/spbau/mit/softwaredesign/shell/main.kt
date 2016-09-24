package ru.spbau.mit.softwaredesign.shell

import ru.spbau.mit.softwaredesign.shell.parser.expandMacroses
import ru.spbau.mit.softwaredesign.shell.pipeline.buildPipeline
import ru.spbau.mit.softwaredesign.shell.std.STD_COMMANDS

fun main(args: Array<String>) {
    STD_COMMANDS.forEach { name, command -> Executor.register(name, command) }

    while (true) {
        try {
            val pipeline = buildPipeline(expandMacroses(readLine()!!, { Environment[it] }))
            val result = Executor.executePipeline(pipeline)

            if (result.second.length > 0) {
                println(result.second)
            }

            if (result.first != 0) {
                return
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }
    }
}
