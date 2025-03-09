package org.chorus.block

import cn.nukkit.Player
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.entity.Entity
import cn.nukkit.item.*
import cn.nukkit.level.Level
import cn.nukkit.math.BlockFace

open class BlockFlowingWater @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockLiquid(blockstate) {
    override val name: String
        get() = "Flowing Water"

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
        val ret = level.setBlock(this.position, this, true, false)
        level.scheduleUpdate(this, this.tickRate())
        return ret
    }

    override fun afterRemoval(newBlock: Block, update: Boolean) {
        if (!update) {
            return
        }

        val newId = newBlock.id
        if (newId == FLOWING_WATER || newId == WATER) {
            return
        }

        val up = up(1, 0)
        for (diagonalFace in BlockFace.Plane.HORIZONTAL) {
            val diagonal = up!!.getSide(diagonalFace)
            if (diagonal!!.id == REEDS) {
                diagonal.onUpdate(Level.BLOCK_UPDATE_SCHEDULED)
            }
        }
    }

    override fun getLiquidWithNewDepth(depth: Int): BlockLiquid {
        return BlockFlowingWater(
            blockState!!.setPropertyValue(
                Companion.properties,
                CommonBlockProperties.LIQUID_DEPTH.createValue(depth)
            )
        )
    }

    override fun onEntityCollide(entity: Entity) {
        super.onEntityCollide(entity)

        if (entity.fireTicks > 0) {
            entity.extinguish()
        }
    }

    override fun tickRate(): Int {
        return 5
    }

    override fun usesWaterLogging(): Boolean {
        return true
    }

    override val passableBlockFrictionFactor: Double
        get() = 0.5

    companion object {
        val properties: BlockProperties = BlockProperties(FLOWING_WATER, CommonBlockProperties.LIQUID_DEPTH)
            get() = Companion.field
    }
}