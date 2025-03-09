package org.chorus.recipe

import cn.nukkit.network.protocol.types.RecipeUnlockingRequirement
import java.util.*

/**
 * @author CreeperFace
 */
abstract class CraftingRecipe protected constructor(
    id: String,
    /**
     * Get the priority of this recipe,
     * the lower the value, the higher the priority.
     * and the same output recipe will be to match the higher priority
     *
     * @return the priority
     */
    val priority: Int, recipeUnlockingRequirement: RecipeUnlockingRequirement?
) :
    BaseRecipe(id) {
    var uUID: UUID? = null
    val requirement: RecipeUnlockingRequirement =
        recipeUnlockingRequirement ?: RecipeUnlockingRequirement(RecipeUnlockingRequirement.UnlockingContext.NONE)
}
