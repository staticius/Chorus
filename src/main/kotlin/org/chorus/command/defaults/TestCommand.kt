package org.chorus.command.defaults

import org.chorus.command.Command
import org.chorus.command.data.CommandData

abstract class TestCommand @JvmOverloads constructor(
    name: String,
    description: String = "",
    usageMessage: String? = "",
    aliases: Array<String> = arrayOf()
) :
    Command(name, description, usageMessage, aliases) {
    init {
        // Mark as test command, client displays in blue
        defaultCommandData.flags.add(CommandData.Flag.TEST_USAGE)
    }
}
