package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.item.Item
import cn.nukkit.item.ItemTool
import cn.nukkit.math.BlockFace

class BlockResinClump @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockLichen(blockstate) {
    override fun canHarvestWithHand(): Boolean {
        return true
    }

    override val toolType: Int
        get() = ItemTool.TYPE_NONE

    override fun getDrops(item: Item): Array<Item?>? {
        val drop = toItem()
        drop!!.setCount(growthSides.size)
        return arrayOf(drop)
    }

    override fun witherAtSide(side: BlockFace) {
        if (isGrowthToSide(side)) {
            setPropertyValue<Int, IntPropertyType>(
                CommonBlockProperties.MULTI_FACE_DIRECTION_BITS,
                getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.MULTI_FACE_DIRECTION_BITS) xor (1 shl side.dUSWNEIndex)
            )
            level.setBlock(this.position, this, true, true)
            level.dropItem(this.position, toItem()!!)
        }
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.RESIN_CLUMP, CommonBlockProperties.MULTI_FACE_DIRECTION_BITS)
            get() = Companion.field
    }
}