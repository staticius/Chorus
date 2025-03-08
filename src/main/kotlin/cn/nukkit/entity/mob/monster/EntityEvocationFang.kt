package cn.nukkit.entity.mob.monster

import cn.nukkit.Player
import cn.nukkit.entity.*
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityEvocationIllager
import cn.nukkit.entity.mob.monster.humanoid_monster.EntityIllager
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.EntityEventPacket
import cn.nukkit.network.protocol.LevelSoundEventPacketV2
import lombok.Getter
import lombok.Setter

/**
 * @author PikyCZ
 */
class EntityEvocationFang(chunk: IChunk?, nbt: CompoundTag) : EntityMonster(chunk, nbt), EntityWalkable {
    @Getter
    @Setter
    private val evocationIllager: EntityEvocationIllager? = null

    override fun getIdentifier(): String {
        return EntityID.Companion.EVOCATION_FANG
    }

    override fun initEntity() {
        this.maxHealth = 1
        super.initEntity()
        level!!.addLevelSoundEvent(
            this.position,
            LevelSoundEventPacketV2.SOUND_FANG,
            -1,
            EntityID.Companion.EVOCATION_FANG,
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
        pk.eid = this.getId()
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
