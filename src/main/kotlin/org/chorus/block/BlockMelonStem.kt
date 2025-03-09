package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.item.ItemID
import cn.nukkit.math.BlockFace

class BlockMelonStem @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockCropsStem(blockstate) {
    override val name: String
        get() = "Melon Stem"

    override val fruitId: String
        get() = BlockID.MELON_BLOCK

    override val seedsId: String
        get() = ItemID.MELON_SEEDS

    override val strippedState: BlockState
        get() = BlockStrippedAcaciaLog.properties.defaultState

    override var blockFace: BlockFace
        get() = facing!!
        set(face) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.FACING_DIRECTION, face.index)
        }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.MELON_STEM, CommonBlockProperties.FACING_DIRECTION, CommonBlockProperties.GROWTH)
            get() = Companion.field
    }
}