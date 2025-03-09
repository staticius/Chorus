package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.type.BooleanPropertyType
import cn.nukkit.item.*
import cn.nukkit.math.*

/**
 * @author Angelic47 (Nukkit Project)
 * @apiNote Extends BlockSolidMeta instead of BlockSolid only in PowerNukkit
 */
class BlockBedrock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    var burnIndefinitely: Boolean
        get() = getPropertyValue<Boolean, BooleanPropertyType>(CommonBlockProperties.INFINIBURN_BIT)
        set(infiniburn) {
            setPropertyValue<Boolean, BooleanPropertyType>(
                CommonBlockProperties.INFINIBURN_BIT,
                infiniburn
            )
        }

    override val hardness: Double
        get() = -1.0

    override val resistance: Double
        get() = 18000000.0

    override val name: String
        get() = "Bedrock"

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return player != null && player.isCreative
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BEDROCK, CommonBlockProperties.INFINIBURN_BIT)
            get() = Companion.field
    }
}
