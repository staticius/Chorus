package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/13
 */
class SaveCommand(name: String) : VanillaCommand(name, "Save the server (levels and players)") {
    init {
        this.permission = "nukkit.command.save.perform"
        commandParameters.clear()
        commandParameters["default"] = CommandParameter.Companion.EMPTY_ARRAY
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        log.addSuccess("commands.save.start").output(true)
        for (player in Server.instance.onlinePlayers.values) {
            player.save()
        }
        for (level in Server.instance.levels.values) {
            level.save(true)
        }
        log.addSuccess("commands.save.success").output(true)
        return 1
    }
}
