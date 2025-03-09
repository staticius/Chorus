package org.chorus.command.defaults

import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.utils.TextFormat
import kotlin.collections.Iterator
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/11
 */
class BanListCommand(name: String) : VanillaCommand(name, "list all the banned players or IPs") {
    init {
        this.permission = "nukkit.command.ban.list"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("type", true, CommandEnum("BanListType", "ips", "players"))
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val paramList = result.value
        val list: BanList
        var ips = false

        if (paramList!!.hasResult(0)) {
            val type = paramList.getResult<String>(0)
            when (type!!.lowercase()) {
                "ips" -> {
                    list = sender.server.ipBans
                    ips = true
                }

                "players" -> list = sender.server.nameBans
                else -> {
                    log.addSyntaxErrors(0).output()
                    return 0
                }
            }
        } else {
            list = sender.server.nameBans
        }

        val builder = StringBuilder()
        val itr: Iterator<BanEntry> = list.getEntires().values.iterator()
        while (itr.hasNext()) {
            builder.append(itr.next().getName())
            if (itr.hasNext()) {
                builder.append(", ")
            }
        }
        val size: Int = list.getEntires().size
        if (ips) {
            log.addSuccess("commands.banlist.ips", size.toString())
        } else {
            log.addSuccess("commands.banlist.players", size.toString())
        }
        log.addSuccess(TextFormat.GREEN.toString() + builder.toString()).successCount(size).output()
        return size
    }
}
