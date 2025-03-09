package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.item.ItemTool

class BlockSculkShrieker @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFlowable(blockstate), BlockEntityHolder<BlockEntitySculkShrieker?> {
    override val name: String
        get() = "Sculk Shrieker"

    override fun canBePulled(): Boolean {
        return false
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override val resistance: Double
        get() = 3.0

    override val hardness: Double
        get() = 3.0

    override val toolType: Int
        get() = ItemTool.TYPE_HOE

    override val blockEntityClass: Class<out Any>
        get() = BlockEntitySculkShrieker::class.java

    override val blockEntityType: String
        get() = BlockEntity.SCULK_SHRIEKER

    override fun canPassThrough(): Boolean {
        return false
    }

    override fun breaksWhenMoved(): Boolean {
        return false
    }

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    override fun recalculateBoundingBox(): AxisAlignedBB? {
        return this
    }

    override val waterloggingLevel: Int
        get() = 1

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.SCULK_SHRIEKER, CommonBlockProperties.ACTIVE, CommonBlockProperties.CAN_SUMMON)
            get() = Companion.field
    }
}
