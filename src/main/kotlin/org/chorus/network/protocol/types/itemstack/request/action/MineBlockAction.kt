package org.chorus.network.protocol.types.itemstack.request.action

/**
 * MineBlockStackRequestActionData is sent by the client when it breaks a block.
 */
class MineBlockAction(
    var hotbarSlot: Int,
    var predictedDurability: Int,
    var stackNetworkId: Int,
) : ItemStackRequestAction {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.MINE_BLOCK
}
