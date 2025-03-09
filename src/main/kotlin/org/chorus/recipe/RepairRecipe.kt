package org.chorus.recipe

import org.chorus.inventory.InventoryType
import org.chorus.item.Item
import org.chorus.recipe.descriptor.DefaultDescriptor
import org.chorus.registry.RecipeRegistry
import java.util.List

class RepairRecipe(val inventoryType: InventoryType, result: Item, ingredients: Collection<Item>) :
    BaseRecipe(RecipeRegistry.computeRecipeIdWithItem(List.of(result), ingredients, RecipeType.REPAIR)) {
    init {
        results.add(result.clone())
        for (item in ingredients) {
            require(item.getCount() >= 1) { "Recipe Ingredient amount was not 1 (value: " + item.getCount() + ")" }
            this.ingredients.add(DefaultDescriptor(item.clone()))
        }
    }

    override fun match(input: Input): Boolean {
        return true
    }

    override val type: RecipeType
        get() = RecipeType.REPAIR
}
