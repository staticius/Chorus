package org.chorus.entity.item

import org.chorus.entity.Entity
import org.chorus.entity.EntityExplosive
import org.chorus.entity.EntityID
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.data.EntityDataTypes
import org.chorus.entity.data.EntityFlag
import org.chorus.entity.mob.monster.EntityEnderDragon
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.level.Explosion
import org.chorus.level.GameRule
import org.chorus.level.Locator
import org.chorus.level.format.IChunk
import org.chorus.math.BlockVector3
import org.chorus.nbt.tag.CompoundTag

/**
 * @author PetteriM1
 */
class EntityEnderCrystal(chunk: IChunk?, nbt: CompoundTag?) : Entity(chunk, nbt), EntityExplosive {
    override fun getIdentifier(): String {
        return EntityID.ENDER_CRYSTAL
    }


    /**
     * @since 1.2.1.0-PN
     */
    protected var detonated: Boolean = false

    override fun initEntity() {
        super.initEntity()

        if (namedTag!!.contains("ShowBottom")) {
            this.setShowBase(namedTag!!.getBoolean("ShowBottom"))
        }

        this.fireProof = true
        this.setDataFlag(EntityFlag.FIRE_IMMUNE, true)
    }

    override fun saveNBT() {
        super.saveNBT()

        namedTag!!.putBoolean("ShowBottom", this.showBase())
    }

    override fun getHeight(): Float {
        return 0.98f
    }

    override fun getWidth(): Float {
        return 0.98f
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (isClosed()) {
            return false
        }

        if (source.cause == DamageCause.FIRE || source.cause == DamageCause.FIRE_TICK || source.cause == DamageCause.LAVA) {
            return false
        }

        if (!super.attack(source)) {
            return false
        }

        if (source is EntityDamageByEntityEvent) {
            if (source.damager is EntityEnderDragon) {
                return false
            }
        }
        explode()

        return true
    }

    override fun explode() {
        if (!this.detonated) {
            this.detonated = true

            val pos: Locator = this.getLocator()
            val explode: Explosion = Explosion(pos, 6.0, this)

            this.close()

            if (level!!.gameRules.getBoolean(GameRule.MOB_GRIEFING)) {
                explode.explodeA()
                explode.explodeB()
            } else {
                explode.explodeB()
            }
        }
    }

    override fun close() {
        super.close()
        for (entity: Entity in level!!.getEntities()) {
            if (entity is EntityEnderDragon) {
                if (entity.position.distance(this.position) <= 28) {
                    entity.attack(EntityDamageEvent(entity, DamageCause.MAGIC, 10f))
                }
                entity.getMemoryStorage()
                    .set<BlockVector3>(CoreMemoryTypes.Companion.LAST_ENDER_CRYSTAL_DESTROY, position.asBlockVector3())
            }
        }
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return false
    }

    fun showBase(): Boolean {
        return this.getDataFlag(EntityFlag.SHOW_BOTTOM)
    }

    fun setShowBase(value: Boolean) {
        this.setDataFlag(EntityFlag.SHOW_BOTTOM, value)
    }

    fun getBeamTarget(): BlockVector3 {
        return this.getDataProperty<BlockVector3>(EntityDataTypes.Companion.BLOCK_TARGET_POS)
    }

    fun setBeamTarget(beamTarget: BlockVector3?) {
        this.setDataProperty(EntityDataTypes.Companion.BLOCK_TARGET_POS, beamTarget!!)
    }

    override fun getOriginalName(): String {
        return "Ender Crystal"
    }
}
