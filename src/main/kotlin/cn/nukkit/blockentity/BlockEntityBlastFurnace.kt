package cn.nukkit.blockentity

import cn.nukkit.block.*
import cn.nukkit.inventory.BlastFurnaceInventory
import cn.nukkit.inventory.SmeltingInventory
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.recipe.SmeltingRecipe

class BlockEntityBlastFurnace(chunk: IChunk, nbt: CompoundTag) : BlockEntityFurnace(chunk, nbt) {
    override val furnaceName: String
        get() = "Blast Furnace"


    override val clientName: String
        get() = BlockEntityID.Companion.BLAST_FURNACE


    override val idleBlockId: String
        get() = Block.BLAST_FURNACE


    override val burningBlockId: String
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
