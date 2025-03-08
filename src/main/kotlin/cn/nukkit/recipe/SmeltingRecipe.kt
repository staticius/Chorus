package cn.nukkit.recipe

import cn.nukkit.item.Item
import cn.nukkit.recipe.descriptor.ItemDescriptor

abstract class SmeltingRecipe protected constructor(id: String) : BaseRecipe(id) {
    var input: ItemDescriptor?
        get() = ingredients.getFirst()
        set(item) {
            ingredients[0] = item
        }

    val result: Item
        get() = results.getFirst()
}
