package org.chorus.entity.ai.executor.villager

import org.chorus.Server
import org.chorus.block.BlockBed
import org.chorus.entity.*
import org.chorus.entity.ai.executor.EntityBreedingExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.villagers.EntityVillagerV2
import org.chorus.network.protocol.EntityEventPacket
import java.util.*

class VillagerBreedingExecutor(entityClass: Class<*>, findingRangeSquared: Int, duration: Int, moveSpeed: Float) :
    EntityBreedingExecutor<Any?>(entityClass, findingRangeSquared, duration, moveSpeed) {
    override fun onStart(entity: EntityMob) {
        super.onStart(entity)
        finded = true
        another = entity.memoryStorage!!.get<Entity>(CoreMemoryTypes.Companion.ENTITY_SPOUSE) as EntityMob
    }

    override fun onStop(entity: EntityMob) {
        clearData(entity)
        if (another != null) {
            clearData(another)
        }
    }

    override fun execute(uncasted: EntityMob?): Boolean {
        if (another == null) return false
        return super.execute(uncasted)
    }

    override fun bear(entity: EntityMob) {
        val range = 48
        val lookY = 5
        var block: BlockBed? = null
        for (x in -range..range) {
            for (z in -range..range) {
                for (y in -lookY..lookY) {
                    val lookTransform = entity.transform.add(x.toDouble(), y.toDouble(), z.toDouble())
                    val lookBlock = lookTransform.levelBlock
                    if (lookBlock is BlockBed) {
                        if (!lookBlock.isHeadPiece && Arrays.stream<Entity>(entity.level!!.entities)
                                .noneMatch { entity1: Entity ->
                                    entity1 is EntityVillagerV2 && entity1.memoryStorage!!
                                        .notEmpty(CoreMemoryTypes.Companion.OCCUPIED_BED) && entity1.bed == lookBlock
                                }
                        ) {
                            block = lookBlock.footPart
                        }
                    }
                }
            }
        }
        if (block == null) {
            sendAngryParticles(entity)
            sendAngryParticles(another)
            return
        } else {
            sendInLoveParticles(entity)
            sendInLoveParticles(another)
        }

        val baby = Entity.Companion.createEntity(entity.networkId, entity.locator) as EntityVillagerV2
        baby.setBaby(true)
        //防止小屁孩去生baby
        baby.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, entity.level!!.tick)
        baby.memoryStorage!!.put<Entity>(CoreMemoryTypes.Companion.PARENT, entity)
        baby.spawnToAll()
    }

    override fun clearData(entity: EntityMob) {
        entity.memoryStorage!!.clear(CoreMemoryTypes.Companion.ENTITY_SPOUSE)
        //clear move target
        entity.moveTarget = null
        //clear look target
        entity.lookTarget = null
        //reset move speed
        entity.movementSpeed = 0.1f
        //interrupt in love status
        entity.memoryStorage!!.put<Boolean>(CoreMemoryTypes.Companion.WILLING, false)
        entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, entity.level!!.tick)
    }

    protected fun sendInLoveParticles(entity: EntityMob) {
        val pk = EntityEventPacket()
        pk.eid = entity.id
        pk.event = EntityEventPacket.LOVE_PARTICLES
        Server.broadcastPacket(entity.viewers.values, pk)
    }

    protected fun sendAngryParticles(entity: EntityMob) {
        val pk = EntityEventPacket()
        pk.eid = entity.id
        pk.event = EntityEventPacket.VILLAGER_ANGRY
        Server.broadcastPacket(entity.viewers.values, pk)
    }
}
