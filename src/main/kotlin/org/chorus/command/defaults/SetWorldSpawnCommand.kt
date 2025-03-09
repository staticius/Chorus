package org.chorus.command.defaults

import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.level.Level
import cn.nukkit.math.*
import java.text.DecimalFormat
import kotlin.collections.Map
import kotlin.collections.set

/**
 * @author xtypr
 * @since 2015/12/13
 */
class SetWorldSpawnCommand(name: String) : VanillaCommand(name, "commands.setworldspawn.description") {
    init {
        this.permission = "nukkit.command.setworldspawn"
        commandParameters.clear()
        commandParameters["default"] = CommandParameter.Companion.EMPTY_ARRAY
        commandParameters["spawnPoint"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("spawnPoint", true, CommandParamType.POSITION)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        val level: Level
        val pos: Vector3?
        if (!result.value!!.hasResult(0)) {
            level = sender.locator.level
            pos = sender.locator.position.round()
        } else {
            level = sender.server.defaultLevel
            pos = result.value!!.getResult(0)
        }
        level.setSpawnLocation(pos)
        val round2 = DecimalFormat("##0.00")
        log.addSuccess(
            "commands.setworldspawn.success", round2.format(pos!!.x),
            round2.format(pos.y),
            round2.format(pos.z)
        ).output(true)
        return 1
    }
}
