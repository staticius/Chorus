package org.chorus_oss.chorus.inventory.request

import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus_oss.chorus.network.protocol.types.itemstack.request.action.PlaceAction

class PlaceActionProcessor : TransferItemActionProcessor<PlaceAction?>() {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.PLACE
}
