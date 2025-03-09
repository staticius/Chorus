package org.chorus.command.defaults

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.PlayersNode
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.utils.TextFormat
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.listOf
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/13
 */
class GamemodeCommand(name: String) : VanillaCommand(
    name, "commands.gamemode.description", "",
    arrayOf<String>("gm")
) {
    init {
        this.permission = "nukkit.command.gamemode.survival;" +
                "nukkit.command.gamemode.creative;" +
                "nukkit.command.gamemode.adventure;" +
                "nukkit.command.gamemode.spectator;" +
                "nukkit.command.gamemode.other"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("gameMode", CommandParamType.INT),
            CommandParameter.Companion.newType("player", true, CommandParamType.TARGET, PlayersNode())
        )
        commandParameters["byString"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("gameMode", CommandEnum.Companion.ENUM_GAMEMODE),
            CommandParameter.Companion.newType("player", true, CommandParamType.TARGET, PlayersNode())
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
        var gameMode = -1
        when (result.key) {
            "default" -> gameMode = list!!.getResult(0)!!
            "byString" -> {
                val str = list!!.getResult<String>(0)
                gameMode = Server.getGamemodeFromString(str)
            }
        }
        if (gameMode < 0 || gameMode > 3) {
            log.addError("commands.gamemode.fail.invalid", gameMode.toString()).output()
            return 0
        }
        val players = if (list!!.hasResult(1)) {
            if (sender.hasPermission("nukkit.command.gamemode.other")) {
                list.getResult<List<Player>>(1)!!
            } else {
                log.addMessage(TextFormat.RED.toString() + "%nukkit.command.generic.permission")
                    .output()
                return 0
            }
        } else {
            if (sender.isPlayer) java.util.List.of(sender.asPlayer()) else listOf()
        }

        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }

        if ((gameMode == 0 && !sender.hasPermission("nukkit.command.gamemode.survival")) ||
            (gameMode == 1 && !sender.hasPermission("nukkit.command.gamemode.creative")) ||
            (gameMode == 2 && !sender.hasPermission("nukkit.command.gamemode.adventure")) ||
            (gameMode == 3 && !sender.hasPermission("nukkit.command.gamemode.spectator"))
        ) {
            log.addMessage(TextFormat.RED.toString() + "%nukkit.command.generic.permission").output()
            return 0
        }

        for (target in players) {
            target.setGamemode(gameMode)
            if (target == sender.asPlayer()) {
                log.addSuccess("commands.gamemode.success.self", Server.getGamemodeString(gameMode))
            } else {
                log.outputObjectWhisper(target, "gameMode.changed", Server.getGamemodeString(gameMode))
                log.addSuccess("commands.gamemode.success.other", Server.getGamemodeString(gameMode), target.name)
            }
        }
        log.successCount(players.size).output(true)
        return players.size
    }
}
