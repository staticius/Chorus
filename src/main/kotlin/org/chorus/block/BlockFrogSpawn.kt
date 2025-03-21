package org.chorus.block

import org.chorus.Player
import org.chorus.entity.Entity
import org.chorus.entity.item.EntityFallingBlock
import org.chorus.item.*
import org.chorus.level.Locator
import org.chorus.math.BlockFace

class BlockFrogSpawn : BlockFlowable {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState) : super(blockState)

    override val name: String
        get() = "Frog Spawn"

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
        if (supportable(block)) {
            if (block.id == BlockID.AIR) return super.place(item, block, target, face, fx, fy, fz, player)
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (!supportable(this)) this.onBreak(null)
        return super.onUpdate(type)
    }

    override fun onEntityCollide(entity: Entity) {
        if (entity is EntityFallingBlock) this.onBreak(null)
    }

    fun supportable(pos: Locator): Boolean {
        val under = pos.getSide(BlockFace.DOWN).levelBlock
        return under.id === BlockID.FLOWING_WATER || under.id === BlockID.WATER || under.getLevelBlockAtLayer(1).id === BlockID.FLOWING_WATER || under.getLevelBlockAtLayer(
            1
        ).id === BlockID.WATER
    }

    override fun getDrops(item: Item): Array<Item> {
        return Item.EMPTY_ARRAY
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
        val properties: BlockProperties = BlockProperties(BlockID.FROG_SPAWN)
    }
}
