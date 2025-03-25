package org.chorus.command.defaults

import org.chorus.Player
import org.chorus.command.CommandSender
import org.chorus.command.data.CommandEnum
import org.chorus.command.data.CommandParamType
import org.chorus.command.data.CommandParameter
import org.chorus.command.tree.ParamList
import org.chorus.command.utils.CommandLogger
import org.chorus.entity.Entity
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.item.enchantment.Enchantment
import kotlin.collections.set

/**
 * @author Pub4Game
 * @since 23.01.2016
 */
class EnchantCommand(name: String) :
    VanillaCommand(name, "commands.enchant.description", "nukkit.command.enchant.usage") {
    init {
        this.permission = "nukkit.command.enchant"
        commandParameters.clear()
        commandParameters["default"] = arrayOf(
            CommandParameter.Companion.newType("player", CommandParamType.TARGET),
            CommandParameter.Companion.newType("enchantmentId", CommandParamType.INT),
            CommandParameter.Companion.newType("level", true, CommandParamType.INT)
        )
        commandParameters["byName"] = arrayOf(
            CommandParameter.Companion.newType("player", CommandParamType.TARGET),
            CommandParameter.Companion.newEnum("enchantmentName", CommandEnum.Companion.ENUM_ENCHANTMENT),
            CommandParameter.Companion.newType("level", true, CommandParamType.INT)
        )
        this.enableParamTree()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String?,
        result: Map.Entry<String, ParamList>,
        log: CommandLogger
    ): Int {
        val list = result.value
        val entities = list.getResult<List<Entity>>(0)!!
        if (entities.isEmpty()) {
            log.addNoTargetMatch().output()
            return 0
        }
        val enchantment: Enchantment?
        var enchantLevel = 1
        when (result.key) {
            "default" -> {
                val enchant = list.getResult<Int>(1)!!
                enchantment = Enchantment.getEnchantment(enchant)
                if (enchantment.originalName == "unknown") {
                    log.addError("commands.enchant.notFound", enchant.toString()).output()
                    return 0
                }
            }

            "byName" -> {
                val str = list.getResult<String>(1)!!
                enchantment = Enchantment.getEnchantment(str) ?: run {
                    log.addError("commands.enchant.notFound", str).output()
                    return 0
                }
            }

            else -> {
                return 0
            }
        }
        if (list.hasResult(2)) {
            enchantLevel = list.getResult(2)!!
            if (enchantLevel < 1) {
                log.addNumTooSmall(2, 1).output()
                return 0
            }
        }
        var success = 0
        for (entity in entities) {
            val player = entity as Player
            enchantment.setLevel(enchantLevel, false)
            val item = player.inventory.itemInHand
            if (item.isNothing) {
                log.addError("commands.enchant.noItem").output()
                continue
            }
            if (item.id !== ItemID.BOOK) {
                item.addEnchantment(enchantment)
                player.inventory.setItemInHand(item)
            } else {
                val enchanted = Item.get(ItemID.ENCHANTED_BOOK, 0, 1, item.compoundTag)
                enchanted.addEnchantment(enchantment)
                val clone = item.clone()
                clone.count--
                val inventory = player.inventory
                inventory.setItemInHand(clone)
                player.giveItem(enchanted)
            }
            log.addSuccess("commands.enchant.success", enchantment.getName()).output(true)
            success++
        }
        return success
    }
}
