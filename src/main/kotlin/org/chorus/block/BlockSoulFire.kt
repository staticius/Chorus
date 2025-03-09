package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.level.Level

class BlockSoulFire @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFire(blockstate) {
    override val name: String
        get() = "Soul Fire Block"

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val downId = down()!!.id
            if (downId != Block.SOUL_SAND && downId != Block.SOUL_SOIL) {
                level.setBlock(
                    this.position, get(BlockID.FIRE).setPropertyValue<Int, IntPropertyType>(
                        CommonBlockProperties.AGE_16, this.age
                    )
                )
            }
            return type
        }
        return 0
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SOUL_FIRE, CommonBlockProperties.AGE_16)
            get() = Companion.field
    }
}