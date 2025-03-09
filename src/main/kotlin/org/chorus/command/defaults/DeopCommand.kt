package org.chorus.command.defaults

import cn.nukkit.IPlayer
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.IPlayersNode
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.utils.TextFormat
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class DeopCommand(name: String) : VanillaCommand(name, "commands.deop.description") {
    init {
        this.permission = "nukkit.command.op.take"
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
        val IPlayers = result.value!!.getResult<List<IPlayer>>(0)!!
        if (IPlayers.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        for (player in IPlayers) {
            if (!player.isOp) {
                log.addError("Privileges cannot be revoked (revoked or with higher privileges)")
                    .output() //no translation in client
                return 0
            }
            player.isOp = false
            if (player.isOnline) {
                log.outputObjectWhisper(player.player, TextFormat.GRAY.toString() + "%commands.deop.message")
            }
            log.addSuccess("commands.deop.success", player.name).output(true)
        }
        return IPlayers.size
    }
}
