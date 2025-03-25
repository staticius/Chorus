package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.Server
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import kotlin.collections.set


class SeedCommand(name: String) : VanillaCommand(name, "Show the level's seed") {
    init {
        this.permission = "chorus.command.seed"
        commandParameters.clear()
        commandParameters["default"] = CommandParameter.EMPTY_ARRAY
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        val seed = if (sender is Player) {
            sender.level!!.seed
        } else {
            Server.instance.defaultLevel!!.seed
        }
        log.addSuccess("commands.seed.success", seed.toString()).output()
        return 1
    }
}
