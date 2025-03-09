package org.chorus.command.defaults

import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.plugin.InternalPlugin
import java.util.*
import kotlin.collections.Map
import kotlin.collections.set

class DebugCommand(name: String) : TestCommand(name, "commands.debug.description"),
    CoreCommand {
    init {
        this.permission = "nukkit.command.debug"
        commandParameters.clear()
        //生物AI debug模式开关
        commandParameters["entity"] =
            arrayOf<CommandParameter?>(
                CommandParameter.Companion.newEnum("entity", arrayOf<String?>("entity")),
                CommandParameter.Companion.newEnum(
                    "option",
                    Arrays.stream<EntityAI.DebugOption>(EntityAI.DebugOption.EntityAI.DebugOption.entries.toTypedArray())
                        .map<String> { option: EntityAI.DebugOption -> option.name.lowercase() }.toList()
                        .toTypedArray<String>()
                ),
                CommandParameter.Companion.newEnum("value", false, CommandEnum.Companion.ENUM_BOOLEAN)
            )
        commandParameters["rendermap"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newEnum("rendermap", arrayOf<String?>("rendermap")),
            CommandParameter.Companion.newType("zoom", CommandParamType.INT)
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
        when (result.key) {
            "entity" -> {
                val str = list!!.getResult<String>(1)
                val option: EntityAI.DebugOption = EntityAI.DebugOption.valueOf(str!!.uppercase())
                val value = list.getResult<Boolean>(2)!!
                EntityAI.setDebugOption(option, value)
                log.addSuccess(
                    "Entity AI framework " + option.name + " debug mode have been set to: " + EntityAI.checkDebugOption(
                        option
                    )
                ).output()
                return 1
            }

            "rendermap" -> {
                if (!sender.isPlayer) return 0
                val zoom = list!!.getResult<Int>(1)!!
                if (zoom < 1) {
                    log.addError("Zoom must bigger than one").output()
                    return 0
                }
                val player = sender.asPlayer()
                if (player!!.inventory.itemInHand is ItemFilledMap) {
                    player.level.scheduler.scheduleAsyncTask(InternalPlugin.INSTANCE, object : AsyncTask() {
                        override fun onRun() {
                            itemFilledMap.renderMap(
                                player.level,
                                player.position.floorX - 64,
                                player.position.floorZ - 64,
                                zoom
                            )
                            player.inventory.setItemInHand(itemFilledMap)
                            itemFilledMap.sendImage(player)
                            player.sendMessage("Successfully rendered the map in your hand")
                        }
                    })
                    log.addSuccess("Start rendering the map in your hand. Zoom: $zoom").output()
                    return 1
                }
                return 0
            }

            else -> {
                return 0
            }
        }
    }
}
