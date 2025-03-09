package org.chorus.network.protocol.types.itemstack.request.action

import lombok.Value

/**
 * CraftCreativeStackRequestActionData is sent by the client when it takes an item out of the creative inventory.
 * The item is thus not really crafted, but instantly created.
 */
@Value
class CraftCreativeAction : ItemStackRequestAction {
    /**
     * creativeItemNetworkId is the network ID of the creative item that is being created. This is one of the
     * creative item network IDs sent in the CreativeContent packet.
     */
    var creativeItemNetworkId: Int = 0

    var numberOfRequestedCrafts: Int = 0

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_CREATIVE
}
