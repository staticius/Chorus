package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.item.*
import cn.nukkit.math.*

/**
 * @author Pub4Game
 * @since 03.01.2016
 */
class BlockBarrier @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockState) {
    override val name: String
        get() = "Barrier"

    override val waterloggingLevel: Int
        get() = 1

    override fun canBeFlowedInto(): Boolean {
        return false
    }

    override val hardness: Double
        get() = -1.0

    override val resistance: Double
        get() = 18000000.0

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return player != null && player.isCreative
    }

    override fun canBePushed(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BARRIER)
            get() = Companion.field
    }
}
