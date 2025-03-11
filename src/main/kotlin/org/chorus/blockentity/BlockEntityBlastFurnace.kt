package org.chorus.blockentity

import org.chorus.block.*
import org.chorus.inventory.BlastFurnaceInventory
import org.chorus.inventory.SmeltingInventory
import org.chorus.item.*
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.recipe.SmeltingRecipe

class BlockEntityBlastFurnace(chunk: IChunk, nbt: CompoundTag) : BlockEntityFurnace(chunk, nbt) {
    override val furnaceName: String
        get() = "Blast Furnace"


    override val clientName: String
        get() = BlockEntityID.Companion.BLAST_FURNACE


    override val idleBlockID.String
        get() = Block.BLAST_FURNACE


    override val burningBlockID.String
        get() = Block.LIT_BLAST_FURNACE


    override fun createInventory(): SmeltingInventory {
        return BlastFurnaceInventory(this)
    }

    override fun matchRecipe(raw: Item): SmeltingRecipe? {
        return server.recipeRegistry.findBlastFurnaceRecipe(raw)
    }

    override val speedMultiplier: Int
        get() = 2
}
