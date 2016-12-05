package ru.spbau.mit.softwaredesign.shell

import ru.spbau.mit.softwaredesign.shell.parser.expandMacroses
import ru.spbau.mit.softwaredesign.shell.pipeline.buildPipeline
import ru.spbau.mit.softwaredesign.shell.std.*

fun main(args: Array<String>) {
    val executor = ExecutorBuilder()
            .add("wc", ::wc)
            .add("cat", ::cat)
            .add("exit", ::exit)
            .add("pwd", ::pwd)
            .add("grep", ::grep)
            .add("version", { args, input -> Pair(0, "SDShell 2.0") })
            .build()

    while (true) {
        try {
            val pipeline = buildPipeline(expandMacroses(readLine()!!, { Environment[it] }))
            val result = executor.executePipeline(pipeline)

            if (result.second.isNotEmpty()) {
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

