package cn.nukkit.command.defaults

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.lang.TranslationContainer
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/11
 */
class ListCommand(name: String) : VanillaCommand(name, "commands.list.description") {
    init {
        this.permission = "nukkit.command.list"
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
        var online = StringBuilder()
        var onlineCount = 0
        for (player in sender.server.onlinePlayers.values) {
            if (player.isOnline && (sender !is Player || sender.canSee(player))) {
                online.append(player.displayName).append(", ")
                ++onlineCount
            }
        }
        if (!online.isEmpty()) {
            online = StringBuilder(online.substring(0, online.length - 2))
        }
        sender.sendMessage(
            TranslationContainer(
                "commands.players.list",
                onlineCount.toString(), sender.server.maxPlayers.toString()
            )
        )
        sender.sendMessage(online.toString())
        return 1
    }
}
