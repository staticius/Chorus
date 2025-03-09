package org.chorus.command.defaults

import cn.nukkit.Server
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.level.Level
import cn.nukkit.utils.TextFormat
import java.util.function.Consumer
import kotlin.collections.Map
import kotlin.collections.set

class WorldCommand(name: String) : VanillaCommand(name, "nukkit.command.world.description") {
    init {
        this.permission = "nukkit.command.world"
        commandParameters.clear()
        commandParameters["tp"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("tp", arrayOf<String?>("tp")),
            CommandParameter.Companion.newEnum("world", false, WORLD_NAME_ENUM)
        )
        commandParameters["list"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("list", arrayOf<String?>("list"))
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList?>,
        log: CommandLogger
    ): Int {
        when (result.key) {
            "list" -> {
                val strBuilder = StringBuilder()
                Server.getInstance().levels.values.forEach(Consumer { level: Level ->
                    strBuilder.append(level.name)
                    strBuilder.append(", ")
                })
                log.addMessage(
                    TextFormat.WHITE.toString() + "%nukkit.command.world.availableLevels",
                    strBuilder.toString()
                ).output()
                return 1
            }

            "tp" -> {
                val levelName = result.value!!.getResult<String>(1)
                var level = Server.getInstance().getLevelByName(levelName)
                if (level == null) {
                    if (Server.getInstance().loadLevel(levelName)) {
                        level = Server.getInstance().getLevelByName(levelName)
                    } else {
                        log.addMessage("nukkit.command.world.levelNotFound", levelName).output()
                        return 0
                    }
                }
                sender.asEntity()!!.teleport(level!!.safeSpawn)
                log.addMessage(TextFormat.WHITE.toString() + "%nukkit.command.world.successTp", levelName).output()
                return 1
            }

            else -> {
                return 0
            }
        }
    }

    companion object {
        @JvmField
        val WORLD_NAME_ENUM: CommandEnum = CommandEnum(
            "world"
        ) {
            Server.getInstance().levels.values.stream()
                .map { obj: Level -> obj.name }.toList()
        }
    }
}
