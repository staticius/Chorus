package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.*
import org.chorus.item.*

open class BlockLitSmoker @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockLitFurnace(blockstate) {
    override val name: String
        get() = "Burning Smoker"

    override val blockEntityType: String
        get() = BlockEntity.SMOKER

    override val blockEntityClass: Class<out BlockEntityFurnace>
        get() = BlockEntitySmoker::class.java

    override fun toItem(): Item? {
        return ItemBlock(BlockSmoker())
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIT_SMOKER, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)

    }
}