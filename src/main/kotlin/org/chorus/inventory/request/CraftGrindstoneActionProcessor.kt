package org.chorus.inventory.request

import it.unimi.dsi.fastutil.Pair
import it.unimi.dsi.fastutil.objects.ObjectIntMutablePair
import org.chorus.Player
import org.chorus.Server
import org.chorus.event.inventory.GrindstoneEvent
import org.chorus.inventory.GrindstoneInventory
import org.chorus.item.Item
import org.chorus.item.ItemID
import org.chorus.item.enchantment.Enchantment
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.types.itemstack.request.action.CraftGrindstoneAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.utils.Loggable
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.Stream
import kotlin.math.ceil
import kotlin.math.max

class CraftGrindstoneActionProcessor : ItemStackRequestActionProcessor<CraftGrindstoneAction?> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_REPAIR_AND_DISENCHANT

    override fun handle(
        action: CraftGrindstoneAction?,
        player: Player,
        context: ItemStackRequestContext
    ): ActionResponse? {
        val topWindow = player.topWindow
        if (topWindow.isEmpty) {
            CraftGrindstoneActionProcessor.log.error("the player's inventory is empty!")
            return context.error()
        }
        val inventory = topWindow.get() as GrindstoneInventory
        val firstItem = inventory.firstItem
        val secondItem = inventory.secondItem
        if ((firstItem.isNothing) && (secondItem.isNothing)) {
            return context.error()
        }
        val pair = updateGrindstoneResult(player, inventory) ?: return context.error()
        val exp = pair.right()
        val event = GrindstoneEvent(
            inventory,
            firstItem, pair.left(), secondItem, exp, player
        )
        Server.instance.pluginManager.callEvent(event)
        if (event.isCancelled) {
            player.removeAllWindows(false)
            player.sendAllInventories()
            return context.error()
        }
        player.addExperience(event.experienceDropped)
        player.creativeOutputInventory.setItem(event.resultItem)
        return null
    }

    fun updateGrindstoneResult(player: Player?, inventory: GrindstoneInventory): Pair<Item, Int>? {
        var firstItem = inventory.firstItem
        var secondItem = inventory.secondItem
        val resultPair: Pair<Item, Int> = ObjectIntMutablePair.of(Item.AIR, 0)
        if (!firstItem.isNothing && !secondItem.isNothing && (firstItem.id != secondItem.id)) {
            return null
        }

        if (firstItem.isNothing) {
            val air = firstItem
            firstItem = secondItem
            secondItem = air
        }

        if (firstItem.isNothing) {
            return null
        }

        if (firstItem.id == ItemID.ENCHANTED_BOOK) {
            if (secondItem.isNothing) {
                resultPair.left(Item.get(ItemID.BOOK, 0, firstItem.getCount()))
                resultPair.right(recalculateResultExperience(inventory))
            } else {
                return null
            }
            return resultPair
        }
        val resultExperience = recalculateResultExperience(inventory)
        val result = firstItem.clone()
        var tag = result.namedTag
        if (tag == null) tag = CompoundTag()
        tag.remove("ench")
        tag.remove("custom_ench")
        result.setCompoundTag(tag)

        if (!secondItem.isNothing && firstItem.maxDurability > 0) {
            val first = firstItem.maxDurability - firstItem.damage
            val second = secondItem.maxDurability - secondItem.damage
            val reduction = first + second + firstItem.maxDurability * 5 / 100
            val resultingDamage = max((firstItem.maxDurability - reduction + 1).toDouble(), 0.0).toInt()
            result.damage = resultingDamage
        }
        resultPair.left(result)
        resultPair.right(resultExperience)
        return resultPair
    }

    fun recalculateResultExperience(inventory: GrindstoneInventory): Int {
        val firstItem = inventory.firstItem
        val secondItem = inventory.secondItem
        if (!firstItem.hasEnchantments() && !secondItem.hasEnchantments()) {
            return 0
        }

        var resultExperience = Stream.of(firstItem, secondItem)
            .flatMap { item: Item ->
                // Support stacks of enchanted items and skips invalid stacks (e.g. negative stacks, enchanted air)
                if (item.isNothing) {
                    return@flatMap Stream.empty<Enchantment>()
                } else if (item.getCount() == 1) {
                    return@flatMap item.enchantments.toList().stream()
                } else {
                    val enchantments: Array<Array<Enchantment>> = Array(item.getCount()) {
                        item.enchantments
                    }
                    return@flatMap enchantments.toList().stream().flatMap { it.toList().stream() }
                }
            }
            .mapToInt { enchantment: Enchantment -> enchantment.getMinEnchantAbility(enchantment.level) }
            .sum()

        resultExperience = ThreadLocalRandom.current().nextInt(
            ceil(resultExperience.toDouble() / 2).toInt(),
            resultExperience + 1
        )
        return resultExperience
    }

    companion object : Loggable
}
