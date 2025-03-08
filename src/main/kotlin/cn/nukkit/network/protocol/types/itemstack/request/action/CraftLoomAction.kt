package cn.nukkit.network.protocol.types.itemstack.request.action

import lombok.Value

@Value
class CraftLoomAction : ItemStackRequestAction {
    var patternId: String? = null

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_LOOM
}