package org.chorus.network.protocol.types.itemstack.request.action

import org.chorus.item.Item

/**
 * CraftResultsDeprecatedStackRequestAction is an additional, deprecated packet sent by the client after
 * crafting. It holds the final results and the amount of times the recipe was crafted. It shouldn't be used.
 * This action is also sent when an item is enchanted. Enchanting should be treated mostly the same way as
 * crafting, where the old item is consumed.
 */
class CraftResultsDeprecatedAction(
    var resultItems: Array<Item>,
    var timesCrafted: Int,
) : ItemStackRequestAction {
    override val type: ItemStackRequestActionType
        get() = ItemStackRequestActionType.CRAFT_RESULTS_DEPRECATED
}
