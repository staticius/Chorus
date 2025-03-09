package org.chorus.inventory.request

import org.chorus.Player
import org.chorus.blockentity.BlockEntityBeacon
import org.chorus.inventory.BeaconInventory
import org.chorus.network.protocol.types.itemstack.request.action.BeaconPaymentAction
import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import lombok.extern.slf4j.Slf4j

@Slf4j
class BeaconPaymentActionProcessor : ItemStackRequestActionProcessor<BeaconPaymentAction> {
    override fun handle(
        action: BeaconPaymentAction,
        player: Player,
        context: ItemStackRequestContext
    ): ActionResponse? {
        val topWindow = player.topWindow
        if (topWindow.isEmpty) {
            BeaconPaymentActionProcessor.log.error("the player's haven't open any inventory!")
            return context.error()
        }
        if (topWindow.get() !is BeaconInventory) {
            BeaconPaymentActionProcessor.log.error("the player's haven't open beacon inventory!")
            return context.error()
        }
        val holder: BlockEntityBeacon = beaconInventory.getHolder()
        holder.primaryPower = action.primaryEffect
        holder.secondaryPower = action.secondaryEffect
        return null
    }

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.BEACON_PAYMENT
}
