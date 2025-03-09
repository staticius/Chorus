package org.chorus.inventory.request

import org.chorus.network.protocol.types.itemstack.request.action.ItemStackRequestActionType
import org.chorus.network.protocol.types.itemstack.request.action.PlaceAction


/**
 * Allay Project 2023/7/26
 *
 * @author daoge_cmd
 */
class PlaceActionProcessor : TransferItemActionProcessor<PlaceAction?>() {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.PLACE
}
