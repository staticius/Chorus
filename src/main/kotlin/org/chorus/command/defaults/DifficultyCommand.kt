package org.chorus.command.defaults

import org.chorus.Server
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import kotlin.collections.ArrayList
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/11/12
 */
class DifficultyCommand(name: String) :
    VanillaCommand(name, "commands.difficulty.description", "%commands.difficulty.usage") {
    init {
        this.permission = "nukkit.command.difficulty"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("difficulty", CommandParamType.INT)
        )
        commandParameters["byString"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum(
                "difficulty",
                CommandEnum("Difficulty", "peaceful", "p", "easy", "e", "normal", "n", "hard", "h")
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
        val list = result.value
        var difficulty: Int
        when (result.key) {
            "default" -> {
                difficulty = list!!.getResult(0)!!
            }

            "byString" -> {
                val str = list!!.getResult<String>(0)
                difficulty = Server.getDifficultyFromString(str)
            }

            else -> {
                return 0
            }
        }
        if (sender.server.isHardcore) {
            difficulty = 3
        }
        if (difficulty != -1) {
            sender.server.difficulty = difficulty
            val pk: SetDifficultyPacket = SetDifficultyPacket()
            pk.difficulty = sender.server.difficulty
            Server.broadcastPacket(ArrayList(sender.server.onlinePlayers.values), pk)
            log.addSuccess("commands.difficulty.success", difficulty.toString()).output(true)
            return 1
        } else {
            log.addSyntaxErrors(0).output()
            return 0
        }
    }
}
