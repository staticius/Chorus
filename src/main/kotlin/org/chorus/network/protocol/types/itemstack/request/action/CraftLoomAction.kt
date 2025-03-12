package org.chorus.network.protocol.types.itemstack.request.action


class CraftLoomAction : ItemStackRequestAction {
    var patternId: String? = null

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_LOOM
}