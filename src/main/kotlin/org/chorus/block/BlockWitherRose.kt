package org.chorus.block

import org.chorus.Player
import org.chorus.entity.Entity
import org.chorus.entity.effect.EffectType
import org.chorus.item.Item
import org.chorus.math.AxisAlignedBB
import org.chorus.math.BlockFace

class BlockWitherRose @JvmOverloads constructor(blockstate: BlockState = Companion.properties.getDefaultState()) :
    BlockFlower(blockstate) {
    override fun canPlantOn(block: Block): Boolean {
        return super.canPlantOn(block) || block.id == BlockID.NETHERRACK || block.id == BlockID.SOUL_SAND
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        return false
    }

    override fun onEntityCollide(entity: Entity) {
        if (Server.instance.getDifficulty() != 0 && entity is EntityLiving) {
            if (!entity.invulnerable && !entity.hasEffect(EffectType.WITHER) && (entity !is Player || !entity.isCreative && !entity.isSpectator)) {
                val effect = get(EffectType.WITHER)
                effect.setDuration(40)
                effect.setAmplifier(1)
                entity.addEffect(effect)
            }
        }
    }

    override fun recalculateCollisionBoundingBox(): AxisAlignedBB? {
        return this
    }

    override fun hasEntityCollision(): Boolean {
        return true
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.WITHER_ROSE)

    }
}
