package cn.nukkit.network.protocol.types.itemstack.request.action

import lombok.Value

/**
 * LabTableCombineStackRequestActionData is sent by the client when it uses a lab table to combine item stacks.
 */
@Value
class LabTableCombineAction : ItemStackRequestAction {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.LAB_TABLE_COMBINE
}
