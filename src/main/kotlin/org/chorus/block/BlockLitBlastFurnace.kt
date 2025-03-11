package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.*
import org.chorus.item.*

open class BlockLitBlastFurnace @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLitFurnace(blockstate) {
    override val name: String
        get() = "Burning Blast Furnace"

    override val blockEntityType: String
        get() = BlockEntity.BLAST_FURNACE

    override val blockEntityClass: Class<out BlockEntityFurnace>
        get() = BlockEntityBlastFurnace::class.java

    override fun toItem(): Item? {
        return ItemBlock(BlockBlastFurnace())
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIT_BLAST_FURNACE, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)

    }
}