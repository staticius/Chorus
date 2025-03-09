package cn.nukkit.block

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.entity.effect.Effect.Companion.get
import cn.nukkit.entity.effect.EffectType
import cn.nukkit.item.Item
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.BlockFace

class BlockWitherRose @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.getDefaultState()) :
    BlockFlower(blockstate) {
    override fun canPlantOn(block: Block): Boolean {
        return super.canPlantOn(block) || block.id == BlockID.NETHERRACK || block.id == BlockID.SOUL_SAND
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

    override fun onEntityCollide(entity: Entity?) {
        if (level.server.getDifficulty() != 0 && entity is EntityLiving) {
            if (!entity!!.invulnerable && !entity.hasEffect(EffectType.WITHER) && (entity !is Player || !entity.isCreative && !entity.isSpectator)) {
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
            get() = Companion.field
    }
}
