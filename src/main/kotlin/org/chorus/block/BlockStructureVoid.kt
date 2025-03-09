package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.math.BlockFace
import cn.nukkit.math.Vector3

/**
 * @author good777LUCKY
 */
class BlockStructureVoid @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Structure Void"

    override val hardness: Double
        get() = 0.0

    override val resistance: Double
        get() = 0.0

    override fun canPassThrough(): Boolean {
        return true
    }

    override fun isSolid(side: BlockFace): Boolean {
        return false
    }

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return false
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.STRUCTURE_VOID)
            get() = Companion.field
    }
}
