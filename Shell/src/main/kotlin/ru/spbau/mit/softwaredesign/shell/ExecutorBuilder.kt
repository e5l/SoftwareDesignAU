package ru.spbau.mit.softwaredesign.shell

/**
 * Custom shell builder
 */
class ExecutorBuilder(val executor: Executor = Executor()) {
    fun add(name: String, command: (args: Array<String>, input: String) -> Pair<Int, String>): ExecutorBuilder {
        executor.register(name, command)
        return this
    }

    fun build(): Executor = executor
}