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

class OpCommand(name: String) : VanillaCommand(name, "commands.op.description") {
    init {
        this.permission = "chorus.command.op.give"
        commandParameters.clear()
        commandParameters["default"] = arrayOf(
            CommandParameter.newType("player", CommandParamType.TARGET, IPlayersNode())
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        val players = result.value.getResult<List<IPlayer>>(0)!!
        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }

        for (player in players) {
            if (player.isOp) {
                log.addError("commands.op.failed", player.name!!).output()
            } else {
                player.isOp = true
                if (player.isOnline) {
                    log.outputObjectWhisper(player.player!!, TextFormat.GRAY.toString() + "%commands.op.message")
                }
                log.addSuccess("commands.op.success", player.name!!).output(true)
            }
        }
        return players.size
    }
}
