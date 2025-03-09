package org.chorus.command.function

import org.chorus.Server
import org.chorus.command.CommandSender
import lombok.Getter
import java.nio.file.Files
import java.nio.file.Path

@Getter
class Function private constructor(private val fullPath: Path) {
    private var commands: List<String>? = null

    init {
        try {
            commands = Files.readAllLines(fullPath)
            commands = commands.stream().filter { s: String -> !s.isBlank() }
                .map { s: String -> s.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] }
                .filter { s: String -> !s.isEmpty() }.toList()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun dispatch(sender: CommandSender?): Boolean {
        var success = true
        for (command in commands!!) {
            if (Server.getInstance().executeCommand(sender, command) <= 0) success = false
        }
        return success
    }

    companion object {
        fun fromPath(path: Path): Function {
            return Function(path)
        }
    }
}
