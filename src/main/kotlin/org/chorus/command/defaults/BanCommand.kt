package org.chorus.command.defaults

import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.event.player.PlayerKickEvent
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author MagicDroidX (Nukkit Project)
 */
class BanCommand(name: String) : VanillaCommand(name, "commands.ban.description", "%commands.ban.usage") {
    init {
        this.permission = "nukkit.command.ban.player"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("player", CommandParamType.STRING),
            CommandParameter.Companion.newType("reason", true, CommandParamType.STRING)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val list = result.value
        val name = list!!.getResult<String>(0)
        val reason = list.getResult<String>(1)
        sender.server.nameBans.addBan(name, reason, null, sender.name)

        val player = sender.server.getPlayerExact(name)
        player?.kick(
            PlayerKickEvent.Reason.NAME_BANNED,
            if (reason!!.length > 0) "Banned by admin. Reason: $reason" else "Banned by admin"
        )
        log.addSuccess("commands.ban.success", player?.name ?: name).output(true)
        return 1
    }
}
