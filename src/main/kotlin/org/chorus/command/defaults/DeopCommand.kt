package org.chorus.command.defaults

import org.chorus.IPlayer
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.IPlayersNode
import org.chorus.command.utils.CommandLogger
import org.chorus.utils.TextFormat
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class DeopCommand(name: String) : VanillaCommand(name, "commands.deop.description") {
    init {
        this.permission = "nukkit.command.op.take"
        commandParameters["default"] = arrayOf(
            CommandParameter.newType("player", CommandParamType.TARGET, IPlayersNode())
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
