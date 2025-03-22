package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.BlockEntityBlastFurnace
import org.chorus.blockentity.BlockEntityFurnace
import org.chorus.blockentity.BlockEntityID
import org.chorus.item.Item
import org.chorus.item.ItemBlock

open class BlockLitBlastFurnace @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockLitFurnace(blockstate) {
    override val name: String
        get() = "Burning Blast Furnace"

    override fun getBlockEntityType(): String = BlockEntityID.BLAST_FURNACE

    override fun getBlockEntityClass(): Class<out BlockEntityFurnace> = BlockEntityBlastFurnace::class.java

    override fun toItem(): Item {
        return ItemBlock(BlockBlastFurnace())
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIT_BLAST_FURNACE, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)

    }
}