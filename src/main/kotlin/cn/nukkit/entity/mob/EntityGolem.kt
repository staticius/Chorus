package cn.nukkit.entity.mob

import cn.nukkit.entity.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.mob.monster.EntityCreeper
import cn.nukkit.entity.mob.monster.EntityMonster
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

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
