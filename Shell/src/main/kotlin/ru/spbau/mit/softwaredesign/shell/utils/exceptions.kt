package ru.spbau.mit.softwaredesign.shell.utils

class ArgumentsException(command: String, expected: Int, occurred: Int)
    : Exception("$command: invalid arguments count. Expected $expected, occurred: $occurred")

class VariableUndefinedException(name: String)
    : Exception("Variable $$name undefined")

class ParseException()
    : Exception("Invalid expression")

class NonZeroResultException(command: String, code: Int)
    : Exception("$command: Exit code $code")

class CommandNotFoundException(name: String)
    : Exception("$name: not found")

class UnknownNodeType(type: String)
    : Exception("Unknown node type: $type")

class DirNotFoundException(dir: String)
    : Exception("Dir $dir not found")
