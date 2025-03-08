package cn.nukkit.command.defaults

import cn.nukkit.Server
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger

class SetMaxPlayersCommand(name: String) : VanillaCommand(name, "commands.setmaxplayers.description") {
    init {
        this.permission = "nukkit.command.setmaxplayers"
        getCommandParameters().clear()
        this.addCommandParameters(
            "default", arrayOf<CommandParameter?>(
                CommandParameter.Companion.newType("maxPlayers", false, CommandParamType.INT)
            )
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        var maxPlayers = result.value!!.getResult<Int>(0)!!
        var lowerBound = false

        if (maxPlayers < Server.getInstance().onlinePlayers.size) {
            maxPlayers = Server.getInstance().onlinePlayers.size
            lowerBound = true
        }

        sender.server.maxPlayers = maxPlayers
        log.addSuccess("commands.setmaxplayers.success", maxPlayers.toString())
        if (lowerBound) {
            log.addSuccess("commands.setmaxplayers.success.lowerbound")
        }
        log.output()
        return 1
    }
}
