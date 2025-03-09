package org.chorus.command.defaults

import cn.nukkit.Player
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.*
import cn.nukkit.command.utils.CommandLogger
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.set

class AbilityCommand(name: String) : VanillaCommand(name, "commands.ability.description", "%commands.ability.usage") {
    init {
        this.permission = "nukkit.command.ability"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("player", false, CommandParamType.TARGET, PlayersNode()),
            CommandParameter.Companion.newEnum(
                "ability",
                false,
                arrayOf<String?>("mayfly", "mute", "worldbuilder")
            ),
            CommandParameter.Companion.newEnum(
                "value",
                true,
                CommandEnum.Companion.ENUM_BOOLEAN,
                BooleanNode()
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
        val players = list!!.getResult<List<Player>>(0)!!
        if (players.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        val ability_str: String
        val type: AdventureSettings.Type? = when (list.getResult<String>(1).also { ability_str = it!! }) {
            "mayfly" -> AdventureSettings.Type.ALLOW_FLIGHT
            "mute" -> AdventureSettings.Type.MUTED
            "worldbuilder" -> AdventureSettings.Type.WORLD_BUILDER
            else -> null
        }

        if (list.hasResult(2)) {
            val value = list.getResult<Boolean>(2)!!
            for (player in players) {
                player.adventureSettings.set(type, value)
                player.adventureSettings.update()
                if (value) log.addSuccess("commands.ability.granted", ability_str)
                else log.addSuccess("commands.ability.revoked", ability_str)
            }
            log.addSuccess("commands.ability.success").successCount(1).output()
        } else {
            if (!sender.isPlayer) {
                return 0
            }
            val value: Boolean = sender.asPlayer()!!.adventureSettings.get(type)
            log.addSuccess("$ability_str = $value").output()
        }
        return 1
    }
}
