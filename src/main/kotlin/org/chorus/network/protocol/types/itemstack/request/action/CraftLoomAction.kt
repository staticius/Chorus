package org.chorus.network.protocol.types.itemstack.request.action


class CraftLoomAction(
    var patternId: String
) : ItemStackRequestAction {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_LOOM
}