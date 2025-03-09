package org.chorus.network.protocol.types.itemstack.request.action

import lombok.Value


class CraftGrindstoneAction : ItemStackRequestAction {
    var recipeNetworkId: Int = 0
    var numberOfRequestedCrafts: Int = 0
    var repairCost: Int = 0

    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_REPAIR_AND_DISENCHANT
}