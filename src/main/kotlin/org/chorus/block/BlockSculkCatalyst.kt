package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.BlockEntity
import org.chorus.item.ItemTool

class BlockSculkCatalyst @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate), BlockEntityHolder<BlockEntitySculkCatalyst?> {
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

    override val blockEntityClass: Class<out Any>
        get() = BlockEntitySculkCatalyst::class.java

    override fun getBlockEntityType(): String {
        return BlockEntity.SCULK_CATALYST

        companion object {
            val properties: BlockProperties = BlockProperties(BlockID.SCULK_CATALYST, CommonBlockProperties.BLOOM)

        }
    }
