package org.chorus.network.protocol.types.itemstack.request.action



/**
 * BeaconPaymentStackRequestActionData is sent by the client when it submits an item to enable effects from a
 * beacon. These items will have been moved into the beacon item slot in advance.
 */

class BeaconPaymentAction : ItemStackRequestAction {
    var primaryEffect: Int = 0
    var secondaryEffect: Int = 0

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.BEACON_PAYMENT
}
