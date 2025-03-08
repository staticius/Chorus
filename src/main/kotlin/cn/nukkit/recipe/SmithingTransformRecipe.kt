/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2020  José Roberto de Araújo Júnior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.nukkit.recipe

import cn.nukkit.item.Item
import cn.nukkit.recipe.descriptor.ItemDescriptor

/**
 * The type Smithing recipe for upgrade equipment.
 *
 * @author joserobjr
 * @since 2020 -09-28
 */
class SmithingTransformRecipe(
    recipeId: String,
    result: Item?,
    base: ItemDescriptor?,
    addition: ItemDescriptor?,
    template: ItemDescriptor?
) :
    BaseRecipe(recipeId) {
    init {
        ingredients.add(base)
        ingredients.add(addition)
        ingredients.add(template)
        results.add(result)
    }

    override fun match(input: Input): Boolean {
        return false
    }

    val result: Item
        get() = results.getFirst()

    override val type: RecipeType
        get() = RecipeType.SMITHING_TRANSFORM

    val base: ItemDescriptor
        get() = ingredients.getFirst()

    val addition: ItemDescriptor
        get() = ingredients[1]

    val template: ItemDescriptor
        get() = ingredients[2]
}
