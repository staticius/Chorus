package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.blockentity.*
import org.chorus.dispenser.DispenseBehavior
import org.chorus.dispenser.DropperDispenseBehavior
import org.chorus.item.*

class BlockDropper @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockDispenser(blockstate) {
    override val name: String
        get() = "Dropper"

    override fun getBlockEntityClass(): Class<out BlockEntityDropper?> {
        return BlockEntityDropper::class.java
    }

    override fun getBlockEntityType(): String {
        return BlockEntityID.DROPPER
    }

    override fun getDispenseBehavior(item: Item): DispenseBehavior {
        return DropperDispenseBehavior()
    }

    override val resistance: Double
        get() = 3.5

    override val hardness: Double
        get() = 3.5

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties =
            BlockProperties(
                BlockID.DROPPER,
                CommonBlockProperties.FACING_DIRECTION,
                CommonBlockProperties.TRIGGERED_BIT
            )
    }
}
