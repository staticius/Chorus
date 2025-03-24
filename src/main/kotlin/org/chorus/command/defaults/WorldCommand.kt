package org.chorus.command.defaults

import org.chorus.Server
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.level.Level
import org.chorus.utils.TextFormat
import java.util.function.Consumer
import kotlin.collections.set

class WorldCommand(name: String) : VanillaCommand(name, "nukkit.command.world.description") {
    init {
        this.permission = "nukkit.command.world"
        commandParameters.clear()
        commandParameters["tp"] = arrayOf(
            CommandParameter.Companion.newEnum("tp", arrayOf<String?>("tp")),
            CommandParameter.Companion.newEnum("world", false, WORLD_NAME_ENUM)
        )
        commandParameters["list"] = arrayOf(
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
                Server.instance.levels.values.forEach(Consumer { level: Level ->
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
                var level = Server.instance.getLevelByName(levelName)
                if (level == null) {
                    if (Server.instance.loadLevel(levelName)) {
                        level = Server.instance.getLevelByName(levelName)
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
            Server.instance.levels.values.stream()
                .map { obj: Level -> obj.name }.toList()
        }
    }
}
