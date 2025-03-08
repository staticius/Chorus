package cn.nukkit.inventory.request

import cn.nukkit.Player
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.EntityHumanType.getInventory
import cn.nukkit.event.player.PlayerDropItemEvent
import cn.nukkit.inventory.*
import cn.nukkit.network.protocol.types.itemstack.request.action.DropAction
import cn.nukkit.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import cn.nukkit.network.protocol.types.itemstack.response.ItemStackResponseContainer
import cn.nukkit.network.protocol.types.itemstack.response.ItemStackResponseSlot
import com.google.common.collect.Lists
import lombok.extern.slf4j.Slf4j
import java.util.List

/**
 * Allay Project 2023/9/23
 *
 * @author daoge_cmd
 */
@Slf4j
class DropActionProcessor : ItemStackRequestActionProcessor<DropAction> {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.DROP

    override fun handle(action: DropAction, player: Player, context: ItemStackRequestContext): ActionResponse? {
        val inventory: Inventory = getInventory(player, action.source.container)
        val count = action.count
        val slot = inventory.fromNetworkSlot(action.source.slot)
        var item = inventory.getItem(slot)

        val ev: PlayerDropItemEvent
        player.getServer().getPluginManager().callEvent(PlayerDropItemEvent(player, item).also { ev = it })
        if (ev.isCancelled) {
            return context.error()
        }

        if (validateStackNetworkId(item.netId, action.source.stackNetworkId)) {
            DropActionProcessor.log.warn("mismatch stack network id!")
            return context.error()
        }
        if (item.isNull) {
            DropActionProcessor.log.warn("cannot throw an air!")
            return context.error()
        }
        if (item.getCount() < count) {
            DropActionProcessor.log.warn("cannot throw more items than the current amount!")
            return context.error()
        }
        val drop = item.clone()
        drop.setCount(count)
        player.dropItem(drop)

        val c = item.getCount() - count
        if (c <= 0) {
            inventory.clear(slot, false)
            item = inventory.getItem(slot)
        } else {
            item.setCount(c)
            inventory.setItem(slot, item, false)
        }
        return context.success(
            List.of(
                ItemStackResponseContainer(
                    inventory.getSlotType(slot),
                    Lists.newArrayList(
                        ItemStackResponseSlot(
                            inventory.toNetworkSlot(slot),
                            inventory.toNetworkSlot(slot),
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
}
