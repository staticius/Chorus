package org.chorus.inventory.request

import cn.nukkit.Player
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.network.protocol.types.itemstack.request.action.CraftCreativeAction
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import cn.nukkit.registry.Registries
import lombok.extern.slf4j.Slf4j

/**
 * Allay Project 2023/7/26
 *
 * @author daoge_cmd | CoolLoong
 */
@Slf4j
class CraftCreativeActionProcessor : ItemStackRequestActionProcessor<CraftCreativeAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_CREATIVE

    override fun handle(
        action: CraftCreativeAction,
        player: Player,
        context: ItemStackRequestContext
    ): ActionResponse? {
        var item = Registries.CREATIVE[action.creativeItemNetworkId]
        if (!player.isCreative) {
            CraftCreativeActionProcessor.log.warn(
                "This player {} is get createitems in non-creative mode, which may be a hacker!",
                player.getName()
            )
            return context.error()
        }
        if (item == null) {
            CraftCreativeActionProcessor.log.warn("Unknown creative item network id: {}", action.creativeItemNetworkId)
            return context.error()
        }
        item = item.clone().autoAssignStackNetworkId()
        item.setCount(item.maxStackSize)
        player.creativeOutputInventory.setItem(item)
        //Picking up something from the creation inventory does not require a response
        context.put(CRAFT_CREATIVE_KEY, true)
        return null
    }

    companion object {
        const val CRAFT_CREATIVE_KEY: String = "craft_creative_key"
    }
}
