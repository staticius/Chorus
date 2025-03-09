package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemTool

open class BlockStonePressurePlate @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockPressurePlateBase(blockstate) {
    init {
        this.onPitch = 0.6f
        this.offPitch = 0.5f
    }

    override val name: String
        get() = "Stone Pressure Plate"

    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 6.0

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun computeRedstoneStrength(): Int {
        val bb: AxisAlignedBB? = collisionBoundingBox

        for (entity in level.getCollidingEntities(bb)) {
            if (entity is EntityLiving && entity!!.doesTriggerPressurePlate()) {
                return 15
            }
        }

        return 0
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STONE_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)
            get() = Companion.field
    }
}