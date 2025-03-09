package org.chorus.entity.mob

import org.chorus.entity.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.mob.monster.EntityCreeper
import org.chorus.entity.mob.monster.EntityMonster
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

abstract class EntityGolem(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntityWalkable, EntityCanAttack {
    override fun attack(source: EntityDamageEvent): Boolean {
        if (source is EntityDamageByEntityEvent && source.damager !is EntityCreeper) {
            getMemoryStorage()!!.put<Entity>(CoreMemoryTypes.Companion.ATTACK_TARGET, source.damager)
        }
        return super.attack(source)
    }

    override fun attackTarget(entity: Entity): Boolean {
        if (entity is EntityGolem) return false
        if (entity is EntityAllay) return false
        return entity is EntityMonster
    }
}
