package org.chorus.entity.ai.executor

import org.chorus.Server
import org.chorus.block.Block
import org.chorus.block.BlockID
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.animal.EntitySheep
import org.chorus.level.GameRule
import org.chorus.level.particle.DestroyBlockParticle
import org.chorus.network.protocol.EntityEventPacket

class EatGrassExecutor(protected var duration: Int) : IBehaviorExecutor {
    protected var currentTick: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        if (currentTick == 0) {
            playEatGrassAnimation(entity)
        }
        currentTick++
        if (currentTick > duration) {
            currentTick = 0
            entity.level!!.addParticle(DestroyBlockParticle(entity.position, Block.get(BlockID.TALL_GRASS)))
            if (entity.level!!.gameRules.getBoolean(GameRule.MOB_GRIEFING)) {
                if (entity.transform.levelBlock.id == BlockID.TALL_GRASS) {
                    entity.level!!.setBlock(entity.position, Block.get(BlockID.AIR))
                } else {
                    entity.level!!.setBlock(entity.position.add(0.0, -1.0, 0.0), Block.get(BlockID.DIRT))
                }
            }
            if (entity is EntitySheep) {
                if (entity.isSheared()) {
                    entity.growWool()
                    return false
                }
                if (entity.isBaby())  // TODO: Accelerated growth instead of instant growth
                    entity.setBaby(false)
            }
            return false
        }
        return true
    }

    override fun onInterrupt(entity: EntityMob) {
        currentTick = 0
    }

    protected fun playEatGrassAnimation(entity: EntityMob) {
        val pk = EntityEventPacket()
        pk.eid = entity.getRuntimeID()
        pk.event = EntityEventPacket.EAT_GRASS_ANIMATION
        Server.broadcastPacket(entity.viewers.values, pk)
    }
}
