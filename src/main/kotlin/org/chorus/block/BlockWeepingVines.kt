package org.chorus.block

import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.IntPropertyType
import cn.nukkit.math.BlockFace

class BlockWeepingVines @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockVinesNether(blockstate) {
    override val name: String
        get() = "Weeping Vines"

    override val growthDirection: BlockFace
        get() = BlockFace.DOWN

    override var vineAge: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.WEEPING_VINES_AGE)
        set(vineAge) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.WEEPING_VINES_AGE, vineAge)
        }

    override val maxVineAge: Int
        get() = CommonBlockProperties.WEEPING_VINES_AGE.getMax()

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.WEEPING_VINES, CommonBlockProperties.WEEPING_VINES_AGE)
            get() = Companion.field
    }
}