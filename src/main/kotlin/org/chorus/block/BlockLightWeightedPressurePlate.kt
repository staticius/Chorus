package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.item.ItemTool
import org.chorus.math.ChorusMath.ceilFloat
import kotlin.math.min

class BlockLightWeightedPressurePlate @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockPressurePlateBase(blockstate) {
    init {
        this.onPitch = 0.90000004f
        this.offPitch = 0.75f
    }

    override val name: String
        get() = "Weighted Pressure Plate (Light)"

    override val hardness: Double
        get() = 0.5

    override val resistance: Double
        get() = 2.5

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override fun computeRedstoneStrength(): Int {
        val count = min(
            level.getCollidingEntities(collisionBoundingBox!!).size.toDouble(),
            maxWeight.toDouble()
        ).toInt()

        if (count > 0) {
            val f = min(maxWeight.toDouble(), count.toDouble()).toFloat() / maxWeight.toFloat()
            return ceilFloat(f * 15.0f)
        } else {
            return 0
        }
    }

    val maxWeight: Int
        get() = 15

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.LIGHT_WEIGHTED_PRESSURE_PLATE, CommonBlockProperties.REDSTONE_SIGNAL)

    }
}