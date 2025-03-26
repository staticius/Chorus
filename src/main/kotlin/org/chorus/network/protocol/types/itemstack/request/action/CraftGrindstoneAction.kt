package org.chorus.network.protocol.types.itemstack.request.action


class CraftGrindstoneAction(
    var recipeNetworkId: Int,
    var numberOfRequestedCrafts: Int,
    var repairCost: Int,
) : ItemStackRequestAction {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_REPAIR_AND_DISENCHANT
}