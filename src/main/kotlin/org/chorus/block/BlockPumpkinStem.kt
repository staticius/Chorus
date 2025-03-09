package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.item.ItemID
import cn.nukkit.math.BlockFace

class BlockPumpkinStem @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCropsStem(blockstate) {
    override val name: String
        get() = "Pumpkin Stem"

    override val fruitId: String
        get() = BlockID.PUMPKIN

    override val seedsId: String
        get() = ItemID.PUMPKIN_SEEDS

    override val strippedState: BlockState
        get() = BlockStrippedAcaciaLog.properties.defaultState

    override var blockFace: BlockFace
        get() = facing!!
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face.index)
        }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PUMPKIN_STEM, CommonBlockProperties.FACING_DIRECTION, CommonBlockProperties.GROWTH)
            get() = Companion.field
    }
}