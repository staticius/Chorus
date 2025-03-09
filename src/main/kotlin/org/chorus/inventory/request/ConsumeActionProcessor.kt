package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.entity.EntityHumanType.getInventory
import org.chorus.inventory.*
import org.chorus.network.protocol.types.itemstack.ContainerSlotType
import org.chorus.network.protocol.types.itemstack.request.action.ConsumeAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseContainer
import org.chorus.network.protocol.types.itemstack.response.ItemStackResponseSlot
import com.google.common.collect.Lists
import lombok.extern.slf4j.Slf4j
import java.util.List

/**
 * Allay Project 2023/12/1
 *
 * @author daoge_cmd
 */
@Slf4j
class ConsumeActionProcessor : ItemStackRequestActionProcessor<ConsumeAction> {
    override fun handle(action: ConsumeAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        // We have validated the recipe in CraftRecipeActionProcessor, so here we can believe the client directly
        val count = action.count
        if (count == 0) {
            ConsumeActionProcessor.log.warn("cannot consume 0 items!")

            return context.error()
        }

        val sourceContainer: Inventory = getInventory(player, action.source.container)
        val slot = sourceContainer.fromNetworkSlot(action.source.slot)
        var item = sourceContainer.getItem(slot)
        if (validateStackNetworkId(item.netId, action.source.stackNetworkId)) {
            ConsumeActionProcessor.log.warn("mismatch stack network id!")

            return context.error()
        }

        if (item.isNull) {
            ConsumeActionProcessor.log.warn("cannot consume an air!")

            return context.error()
        }

        if (item.getCount() < count) {
            ConsumeActionProcessor.log.warn("cannot consume more items than the current amount!")

            return context.error()
        }

        if (item.getCount() > count) {
            item.setCount(item.getCount() - count)
            sourceContainer.setItem(slot, item, false)
        } else {
            sourceContainer.clear(slot, false)
            item = sourceContainer.getItem(slot)
        }

        val isEnchRecipe = context.get<Boolean>(CraftRecipeActionProcessor.Companion.ENCH_RECIPE_KEY)
        if (isEnchRecipe != null && isEnchRecipe && action.source.container == ContainerSlotType.ENCHANTING_INPUT) {
            return null
        }

        val containerSlotType = sourceContainer.getSlotType(slot)
        checkNotNull(containerSlotType) { "Unknown slot type for slot " + slot + " in inventory " + sourceContainer.javaClass.simpleName }

        return context.success(
            List.of(
                ItemStackResponseContainer(
                    containerSlotType,
                    Lists.newArrayList(
                        ItemStackResponseSlot(
                            sourceContainer.toNetworkSlot(slot),
                            sourceContainer.toNetworkSlot(slot),
                            item.getCount(),
                            item.netId,
                            item.customName,
                            item.damage
                        )
                    ),
                    action.source.containerName
                )
            )
        )
    }

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CONSUME
}
