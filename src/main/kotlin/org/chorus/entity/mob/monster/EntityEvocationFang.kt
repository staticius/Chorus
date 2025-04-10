package org.chorus.entity.mob.monster

import org.chorus.Player
import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.entity.EntityWalkable
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.mob.monster.humanoid_monster.EntityEvocationIllager
import org.chorus.entity.mob.monster.humanoid_monster.EntityIllager
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.EntityEventPacket
import org.chorus.network.protocol.LevelSoundEventPacket

class EntityEvocationFang(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityWalkable {
    var evocationIllager: EntityEvocationIllager? = null

    override fun getIdentifier(): String {
        return EntityID.EVOCATION_FANG
    }

    override fun initEntity() {
        this.maxHealth = 1
        super.initEntity()
        level!!.addLevelSoundEvent(
            this.position,
            LevelSoundEventPacket.SOUND_FANG,
            -1,
            EntityID.EVOCATION_FANG,
            false,
            false
        )
        for (entity in level!!.getCollidingEntities(getBoundingBox())) {
            if (attackTarget(entity)) {
                val event = EntityDamageByEntityEvent(this, entity, DamageCause.MAGIC, 6f)
                entity.attack(event)
            }
        }
    }

    override fun spawnTo(player: Player) {
        super.spawnTo(player)
        val pk = EntityEventPacket()
        pk.eid = this.getRuntimeID()
        pk.data = 0
        pk.event = EntityEventPacket.ARM_SWING
        player.dataPacket(pk)
    }

    override fun onUpdate(currentTick: Int): Boolean {
        val ticks = 18 - ticksLived
        setDataProperty(EntityDataTypes.Companion.DATA_LIFETIME_TICKS, ticks)
        if (ticks == -1) close()
        return super.onUpdate(currentTick)
    }

    override fun getWidth(): Float {
        return 1f
    }

    override fun getHeight(): Float {
        return 0.8f
    }

    override fun getOriginalName(): String {
        return "Evoker Fang"
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        return false
    }

    override fun attackTarget(entity: Entity): Boolean {
        return entity !is EntityIllager
    }
}
