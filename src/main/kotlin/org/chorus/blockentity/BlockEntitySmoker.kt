package org.chorus.blockentity

import org.chorus.block.*
import org.chorus.inventory.SmeltingInventory
import org.chorus.inventory.SmokerInventory
import org.chorus.item.*
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.recipe.SmeltingRecipe

class BlockEntitySmoker(chunk: IChunk, nbt: CompoundTag) : BlockEntityFurnace(chunk, nbt) {
    override val furnaceName: String
        get() = "Smoker"

    override val clientName: String
        get() = BlockEntityID.Companion.SMOKER

    override val idleBlockId: String
        get() = Block.SMOKER

    override val burningBlockId: String
        get() = Block.LIT_SMOKER

    override fun createInventory(): SmeltingInventory {
        return SmokerInventory(this)
    }

    override fun matchRecipe(raw: Item): SmeltingRecipe? {
        return server.recipeRegistry.findSmokerRecipe(raw)
    }

    override val speedMultiplier: Int
        get() = 2
}
