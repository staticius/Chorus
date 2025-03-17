package org.chorus.command.defaults

import org.chorus.command.CommandSender
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.level.GameRule
import org.chorus.level.Level

class DayLockCommand(name: String) :
    VanillaCommand(name, "commands.daylock.description", "", arrayOf<String>("alwaysday")) {
    init {
        this.permission = "nukkit.command.daylock"
        getCommandParameters().clear()
        this.addCommandParameters(
            "default", arrayOf<CommandParameter?>(
                CommandParameter.Companion.newEnum("lock", true, CommandEnum.Companion.ENUM_BOOLEAN)
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
        var lock = true

        if (list!!.hasResult(0)) lock = list.getResult(0)!!

        var level: Level? = sender.getLocator().level
        level = level ?: Server.instance.defaultLevel
        val rules: GameRules = level.gameRules

        if (lock) {
            rules.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            level.stopTime()
            level.setTime(5000)
            log.addSuccess("commands.always.day.locked").output()
        } else {
            rules.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true)
            level.startTime()
            log.addSuccess("commands.always.day.unlocked").output()
        }
        return 1
    }
}
