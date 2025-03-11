package org.chorus.block

import org.chorus.entity.Entity
import org.chorus.item.Item
import org.chorus.item.ItemTool

class BlockWeb @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFlowable(blockstate) {
    override val hardness: Double
        get() = 4.0

    override val resistance: Double
        get() = 20.0

    override val toolType: Int
        get() = ItemTool.TYPE_SWORD

    override val waterloggingLevel: Int
        get() = 1

    override fun onEntityCollide(entity: Entity) {
        entity.resetFallDistance()
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return if (item.isShears) {
            arrayOf<Item?>(
                this.toItem()
            )
        } else if (item.isSword) {
            arrayOf<Item?>(
                ItemString()
            )
        } else {
            Item.EMPTY_ARRAY
        }
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun diffusesSkyLight(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WEB)
            
    }
}