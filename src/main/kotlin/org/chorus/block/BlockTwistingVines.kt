package org.chorus.block

import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.math.BlockFace

class BlockTwistingVines @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockVinesNether(blockstate) {
    override val name: String
        get() = "Twisting Vines"

    override val growthDirection: BlockFace
        get() = BlockFace.UP

    override var vineAge: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.TWISTING_VINES_AGE)
        set(vineAge) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.TWISTING_VINES_AGE, vineAge)
        }

    override val maxVineAge: Int
        get() = CommonBlockProperties.TWISTING_VINES_AGE.getMax()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.TWISTING_VINES, CommonBlockProperties.TWISTING_VINES_AGE)

    }
}