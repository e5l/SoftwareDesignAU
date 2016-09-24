package ru.spbau.mit.softwaredesign.shell

import ru.spbau.mit.softwaredesign.shell.utils.DirNotFoundException
import ru.spbau.mit.softwaredesign.shell.utils.VariableUndefinedException
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.file.Paths

/**
 * Shell environment.
 * Store for local vars and path.
 */
object Environment {
    fun path() = System.getProperty("user.dir")

    private val vars = hashMapOf<String, String>()

    /**
     * Change user's working directory
     * @param dir directory name
     */
    fun setUserDir(dir: String) {
        val path = Paths.get(path()).resolve(dir).normalize().toAbsolutePath()

        if (File(path.toString()).isDirectory) {
            System.setProperty("user.dir", path.toString())
        }
        else {
            throw DirNotFoundException(dir)
        }
    }

    /**
     * Read variable from context
     * @param name: variable name
     * @return variable content
     */
    operator fun get(name: String): String {
        return vars[name] ?: throw VariableUndefinedException(name)
    }

    /**
     * Write variable to context
     * @param name: variable name
     * @return variable content
     */
    operator fun set(name: String, content: String) {
        vars[name] = content
    }

    /**
     * Read file from current path
     * @param file: related name
     * @return file content
     */
    fun readFile(file: String): String {
        val file = File("${path()}/$file")
        if (file.exists().not()) {
            throw FileNotFoundException()
        }

        return InputStreamReader(file.inputStream()).readText()
    }

    fun listFilesInDir(dir: String): List<String> {
        return File(dir).listFiles { f -> !f.isDirectory }.map { f -> f.name }
    }

    fun listDirsInDir(dir: String): List<String> {
        return File(dir).listFiles { f -> f.isDirectory }.map { f -> f.name }
    }
}
