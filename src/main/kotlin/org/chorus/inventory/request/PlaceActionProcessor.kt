package org.chorus.inventory.request

import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.network.protocol.types.itemstack.request.action.PlaceAction

class PlaceActionProcessor : TransferItemActionProcessor<PlaceAction?>() {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.PLACE
}
