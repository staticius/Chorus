package org.chorus.block

import org.chorus.Player
import org.chorus.blockentity.*
import org.chorus.entity.Entity
import org.chorus.item.*
import org.chorus.level.Level
import org.chorus.math.BlockFace
import org.chorus.math.Vector3

class BlockEndGateway : BlockSolid, BlockEntityHolder<BlockEntityEndGateway> {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

    override val name: String
        get() = "End Gateway"

    override fun getBlockEntityClass() = BlockEntityEndGateway::class.java

    override fun getBlockEntityType(): String = BlockEntityID.END_GATEWAY

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        return BlockEntityHolder.Companion.setBlockAndCreateEntity<BlockEntityEndGateway?, BlockEndGateway>(this) != null
    }

    override fun canPassThrough(): Boolean {
        if (this.level == null) {
            return false
        }

        return if (level.dimension != Level.DIMENSION_THE_END) {
            false
        } else {
            !getOrCreateBlockEntity().isTeleportCooldown()
        }
    }

    override fun isBreakable(vector: Vector3, layer: Int, face: BlockFace?, item: Item?, player: Player?): Boolean {
        return false
    }

    override val hardness: Double
        get() = -1.0

    override val resistance: Double
        get() = 3600000.0

    override val lightLevel: Int
        get() = 15

    override fun hasEntityCollision(): Boolean {
        return true
    }

    override fun toItem(): Item? {
        return ItemBlock(get(AIR))
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    override fun onEntityCollide(entity: Entity?) {
        if (this.level == null) {
            return
        }

        if (level.dimension != Level.DIMENSION_THE_END) {
            return
        }

        if (entity == null) {
            return
        }

        val endGateway = getOrCreateBlockEntity() ?: return

        if (!endGateway.isTeleportCooldown()) {
            endGateway.teleportEntity(entity)
        }
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.END_GATEWAY)

    }
}
