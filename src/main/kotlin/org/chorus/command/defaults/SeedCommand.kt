package org.chorus.command.defaults

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author MagicDroidX (Nukkit Project)
 */
class SeedCommand(name: String) : VanillaCommand(name, "Show the level's seed") {
    init {
        this.permission = "nukkit.command.seed"
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
        val seed = if (sender is Player) {
            sender.level.seed
        } else {
            sender.server.defaultLevel.seed
        }
        log.addSuccess("commands.seed.success", seed.toString()).output()
        return 1
    }
}
