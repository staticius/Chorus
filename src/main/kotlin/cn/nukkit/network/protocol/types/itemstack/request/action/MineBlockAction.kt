package cn.nukkit.network.protocol.types.itemstack.request.action

import lombok.Value

/**
 * MineBlockStackRequestActionData is sent by the client when it breaks a block.
 */
@Value
class MineBlockAction : ItemStackRequestAction {
    var hotbarSlot: Int = 0
    var predictedDurability: Int = 0
    var stackNetworkId: Int = 0

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.MINE_BLOCK
}
