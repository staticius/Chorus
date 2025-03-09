package org.chorus.command.defaults

import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import java.net.UnknownHostException
import java.util.regex.Pattern
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author MagicDroidX (Nukkit Project)
 */
class PardonIpCommand(name: String) : VanillaCommand(name, "unban an IP") {
    init {
        this.permission = "nukkit.command.unban.ip"
        this.aliases = arrayOf<String?>("unbanip", "unban-ip", "pardonip")
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("ip", CommandParamType.STRING)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val value = result.value!!.getResult<String>(0)
        if (Pattern.matches(
                "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$",
                value
            )
        ) {
            sender.server.ipBans.remove(value)
            try {
                sender.server.network.unblockAddress(InetAddress.getByName(value))
            } catch (e: UnknownHostException) {
                log.addError("commands.unbanip.invalid").output()
                return 0
            }
            log.addSuccess("commands.unbanip.success", value).output(true)
            return 1
        } else {
            log.addError("commands.unbanip.invalid").output()
        }
        return 0
    }
}
