package org.chorus.network.protocol.types

import org.chorus.recipe.descriptor.ItemDescriptor
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.Value


@Value
class RecipeUnlockingRequirement {
    var context: UnlockingContext? = null
    var ingredients: List<ItemDescriptor> = ObjectArrayList()

    enum class UnlockingContext {
        NONE,
        ALWAYS_UNLOCKED,
        PLAYER_IN_WATER,
        PLAYER_HAS_MANY_ITEMS;

        companion object {
            private val VALUES = entries.toTypedArray()

            fun from(id: Int): UnlockingContext {
                return VALUES[id]
            }
        }
    }

    val isInvalid: Boolean
        get() = ingredients.isEmpty() && this.context == UnlockingContext.NONE

    companion object {
        val INVALID: RecipeUnlockingRequirement = RecipeUnlockingRequirement(UnlockingContext.NONE)
    }
}