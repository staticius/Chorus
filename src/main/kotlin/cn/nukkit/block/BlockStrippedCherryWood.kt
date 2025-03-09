package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.property.enums.WoodType
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace

class BlockStrippedCherryWood @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockWoodStripped(blockstate) {
    override val name: String
        get() = "Stripped Cherry Wood"

    override val hardness: Double
        get() = 2.0

    override val resistance: Double
        get() = 10.0

    override val burnChance: Int
        get() = 5

    override val burnAbility: Int
        get() = 5

    override fun getStrippedState(): BlockState {
        return blockState!!
    }

    override fun canBeActivated(): Boolean {
        return false
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        return false
    }

    override fun getWoodType(): WoodType {
        throw UnsupportedOperationException()
    }

    companion object {
        val properties: BlockProperties =
            BlockProperties(BlockID.STRIPPED_CHERRY_WOOD, CommonBlockProperties.PILLAR_AXIS)
            get() = Companion.field
    }
}