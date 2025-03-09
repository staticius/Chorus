package cn.nukkit.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.blockentity.*
import cn.nukkit.dispenser.DispenseBehavior
import cn.nukkit.dispenser.DropperDispenseBehavior
import cn.nukkit.item.*

class BlockDropper @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockDispenser(blockstate) {
    override val name: String
        get() = "Dropper"

    override fun getBlockEntityClass(): Class<out BlockEntityDropper?> {
        return BlockEntityDropper::class.java
    }

    override fun getBlockEntityType(): String {
        return BlockEntity.DROPPER
    }

    override fun dispense() {
        super.dispense()
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

    companion object {
        val properties: BlockProperties =
            BlockProperties(DROPPER, CommonBlockProperties.FACING_DIRECTION, CommonBlockProperties.TRIGGERED_BIT)
            get() = Companion.field
    }
}
