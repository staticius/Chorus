package org.chorus.blockentity

import cn.nukkit.block.*
import cn.nukkit.inventory.SmeltingInventory
import cn.nukkit.inventory.SmokerInventory
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.recipe.SmeltingRecipe

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
