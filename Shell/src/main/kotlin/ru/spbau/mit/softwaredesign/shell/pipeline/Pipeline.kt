package ru.spbau.mit.softwaredesign.shell.pipeline

/**
 * Simple block of execution pipeline
 */
interface ExecutableNode {}

/**
 * Assign command
 */
class AssignmentNode(val variable: String, val content: String) : ExecutableNode {}

/**
 * Execution command
 */
class CommandNode(val name: String, val args: Array<String>) : ExecutableNode {}

/**
 * One command line connected with pipes
 */
data class Pipeline(val commands: List<ExecutableNode>)
