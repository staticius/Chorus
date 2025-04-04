package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.level.Level

class BlockSoulFire @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockFire(blockstate) {
    override val name: String
        get() = "Soul Fire Block"

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            val downId = down().id
            if (downId != BlockID.SOUL_SAND && downId != BlockID.SOUL_SOIL) {
                level.setBlock(
                    this.position, get(BlockID.FIRE).setPropertyValue(
                        CommonBlockProperties.AGE_16, this.age
                    )
                )
            }
            return type
        }
        return 0
    }

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.SOUL_FIRE, CommonBlockProperties.AGE_16)
    }
}