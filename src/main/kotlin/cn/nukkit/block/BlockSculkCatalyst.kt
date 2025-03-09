package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.item.ItemTool

class BlockSculkCatalyst @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
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

    override val blockEntityType: String
        get() = BlockEntity.SCULK_CATALYST

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SCULK_CATALYST, CommonBlockProperties.BLOOM)
            get() = Companion.field
    }
}
