package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.entity.item.EntityFallingBlock
import cn.nukkit.item.*
import cn.nukkit.level.Locator
import cn.nukkit.math.BlockFace

class BlockFrogSpawn : BlockFlowable {
    constructor() : super(Companion.properties.defaultState)

    constructor(blockState: BlockState?) : super(blockState)

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
            if (block.id == Block.AIR) return super.place(item, block, target, face, fx, fy, fz, player)
        }
        return false
    }

    override fun onUpdate(type: Int): Int {
        if (!supportable(this)) this.onBreak(null)
        return super.onUpdate(type)
    }

    override fun onEntityCollide(entity: Entity?) {
        if (entity is EntityFallingBlock) this.onBreak(null)
    }

    fun supportable(pos: Locator): Boolean {
        val under = pos.getSide(BlockFace.DOWN)!!.levelBlock
        return under!!.id === FLOWING_WATER || under!!.id === WATER || under!!.getLevelBlockAtLayer(1)!!.id === FLOWING_WATER || under!!.getLevelBlockAtLayer(
            1
        )!!.id === WATER
    }

    override fun getDrops(item: Item): Array<Item?>? {
        return Item.EMPTY_ARRAY
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override fun canBePulled(): Boolean {
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(FROG_SPAWN)
            get() = Companion.field
    }
}
