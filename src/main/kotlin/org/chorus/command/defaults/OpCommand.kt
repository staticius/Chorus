package org.chorus.command.defaults

import org.chorus.IPlayer
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.tree.node.IPlayersNode
import org.chorus.command.utils.CommandLogger
import org.chorus.utils.TextFormat
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class OpCommand(name: String) : VanillaCommand(name, "commands.op.description") {
    init {
        this.permission = "nukkit.command.op.give"
        commandParameters.clear()
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
            if (player.isOp) {
                log.addError("commands.op.failed", player.name).output()
            } else {
                player.isOp = true
                if (player.isOnline) {
                    log.outputObjectWhisper(player.player, TextFormat.GRAY.toString() + "%commands.op.message")
                }
                log.addSuccess("commands.op.success", player.name).output(true)
            }
        }
        return IPlayers.size
    }
}
