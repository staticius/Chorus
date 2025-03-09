package org.chorus.command.defaults

import cn.nukkit.Player
import cn.nukkit.camera.instruction.impl.ClearInstruction.get
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.tree.node.PlayersNode
import cn.nukkit.command.tree.node.RemainStringNode
import cn.nukkit.command.utils.CommandLogger
import cn.nukkit.item.Item
import java.util.stream.Collectors
import kotlin.math.min

/**
 * @author xtypr
 * @since 2015/12/9
 */
class GiveCommand(name: String) : VanillaCommand(name, "commands.give.description") {
    init {
        this.permission = "nukkit.command.give"
        commandParameters.clear()
        commandParameters["default"] = arrayOf<CommandParameter?>(
            CommandParameter.Companion.newType("player", CommandParamType.TARGET, PlayersNode()),
            GenericParameter.Companion.ITEM_NAME.get(false),
            CommandParameter.Companion.newType("amount", true, CommandParamType.INT),
            CommandParameter.Companion.newType("data", true, CommandParamType.INT),
            CommandParameter.Companion.newType("components", true, CommandParamType.JSON, RemainStringNode())
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

        val item = list.getResult<Item>(1)
        if (item!!.isNull) {
            log.addError("commands.give.item.notFound", item.displayName).output()
            return 0
        }
        if (item is ItemBlock && item.block is BlockUnknown) {
            log.addError("commands.give.block.notFound", item.displayName).output()
            return 0
        }
        val count: Int
        if (list.hasResult(2)) {
            count = list.getResult(2)!!
            if (count <= 0) {
                log.addNumTooSmall(2, 1).output()
                return 0
            }
            item.setCount(count)
        }
        if (list.hasResult(3)) {
            val damage = list.getResult<Int>(3)!!
            item.damage = damage
        }
        if (list.hasResult(4)) {
            val json = list.getResult<String>(4)
            val components: ItemJsonComponents = Item.ItemJsonComponents.fromJson(json)
            item.readItemJsonComponents(components)
        }

        for (player in players) {
            val returns = player.inventory.addItem(item.clone())
            val drops: MutableList<Item> = ArrayList()
            for (returned in returns) {
                val maxStackSize = returned.maxStackSize
                if (returned.getCount() <= maxStackSize) {
                    drops.add(returned)
                } else {
                    while (returned.getCount() > maxStackSize) {
                        val drop = returned.clone()
                        val toDrop = min(returned.getCount().toDouble(), maxStackSize.toDouble()).toInt()
                        drop.setCount(toDrop)
                        returned.setCount(returned.getCount() - toDrop)
                        drops.add(drop)
                    }
                    if (!returned.isNull) {
                        drops.add(returned)
                    }
                }
            }

            for (drop in drops) {
                player.dropItem(drop)
            }
            log.outputObjectWhisper(
                player,
                "commands.give.successRecipient",
                item.displayName + " (" + item.id + (if (item.damage != 0) ":" + item.damage else "") + ")",
                item.getCount().toString()
            )
        }
        log.addSuccess(
            "commands.give.success",
            item.displayName + " (" + item.id + (if (item.damage != 0) ":" + item.damage else "") + ")",
            item.getCount().toString(),
            players.stream().map { obj: Player -> obj.name }.collect(Collectors.joining(","))
        )
            .successCount(players.size).output(true)
        return players.size
    }
}
