package org.chorus.block

import org.chorus.Player
import org.chorus.item.Item
import org.chorus.math.BlockFace
import org.chorus.math.Vector3

class BlockStructureVoid @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
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

    override val properties: BlockProperties
        get() = Companion.properties

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.STRUCTURE_VOID)
    }
}
