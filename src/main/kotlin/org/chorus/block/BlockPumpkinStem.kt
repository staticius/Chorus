package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.item.ItemID
import org.chorus.math.BlockFace

class BlockPumpkinStem @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
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
        get() = facing
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face.index)
        }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.PUMPKIN_STEM, CommonBlockProperties.FACING_DIRECTION, CommonBlockProperties.GROWTH)

    }
}