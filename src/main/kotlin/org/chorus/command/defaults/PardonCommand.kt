package org.chorus.command.defaults

import cn.nukkit.IPlayer
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.IPlayersNode
import cn.nukkit.command.utils.CommandLogger
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author MagicDroidX (Nukkit Project)
 */
class PardonCommand(name: String) : VanillaCommand(name, "unban a player") {
    init {
        this.permission = "nukkit.command.unban.player"
        this.aliases = arrayOf<String?>("unban")
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("player", CommandParamType.TARGET, IPlayersNode())
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val players = result.value!!.getResult<List<IPlayer>>(0)!!
        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        for (player in players) {
            sender.server.nameBans.remove(player.name)
            log.addSuccess("commands.unban.success", player.name)
        }
        log.successCount(players.size).output(true)
        return players.size
    }
}
