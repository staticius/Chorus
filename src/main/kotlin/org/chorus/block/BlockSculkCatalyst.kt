package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntityID
import org.chorus.blockentity.BlockEntitySculkCatalyst
import org.chorus.item.ItemTool

class BlockSculkCatalyst @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate), BlockEntityHolder<BlockEntitySculkCatalyst> {
    override val name: String
        get() = "Sculk Catalyst"

    override fun canBePulled(): Boolean {
        return false
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override val lightLevel: Int
        get() = 6

    override val resistance: Double
        get() = 3.0

    override val hardness: Double
        get() = 3.0

    override val toolType: Int
        get() = ItemTool.TYPE_HOE

    override fun getBlockEntityClass() = BlockEntitySculkCatalyst::class.java

    override fun getBlockEntityType() = BlockEntityID.SCULK_CATALYST

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SCULK_CATALYST, CommonBlockProperties.BLOOM)
    }
}
