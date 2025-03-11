package org.chorus.entity.projectile.throwable

import org.chorus.Player
import org.chorus.block.*
import org.chorus.entity.*
import org.chorus.entity.mob.monster.EntityEndermite
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent.DamageCause
import org.chorus.event.player.PlayerTeleportEvent.TeleportCause
import org.chorus.level.GameRule
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.tag.*
import org.chorus.network.protocol.LevelEventPacket
import java.util.concurrent.*

class EntityEnderPearl @JvmOverloads constructor(chunk: IChunk?, nbt: CompoundTag, shootingEntity: Entity? = null) :
    EntityThrowable(chunk, nbt, shootingEntity) {
    override fun getIdentifier(): String {
        return EntityID.Companion.ENDER_PEARL
    }

    override fun getWidth(): Float {
        return 0.25f
    }

    override fun getLength(): Float {
        return 0.25f
    }

    override fun getHeight(): Float {
        return 0.25f
    }

    override fun getGravity(): Float {
        return 0.03f
    }

    override fun getDrag(): Float {
        return 0.01f
    }

    override fun onUpdate(currentTick: Int): Boolean {
        if (this.closed) {
            return false
        }
        var hasUpdate: Boolean = super.onUpdate(currentTick)

        if (this.isCollided && shootingEntity is Player) {
            var portal: Boolean = false
            for (collided: Block in getCollisionBlocks()!!) {
                if (collided.getId() === Block.PORTAL) {
                    portal = true
                }
            }
            if (!portal) {
                teleport()
            }
        }

        if (this.age > 1200 || this.isCollided) {
            this.kill()
            hasUpdate = true
        }

        return hasUpdate
    }

    override fun onCollideWithEntity(entity: Entity) {
        if (shootingEntity is Player) {
            teleport()
        }
        super.onCollideWithEntity(entity)
    }

    private fun teleport() {
        if (this.level != shootingEntity!!.level) {
            return
        }

        level!!.addLevelEvent(
            shootingEntity!!.position.add(0.5, 0.5, 0.5),
            LevelEventPacket.EVENT_SOUND_TELEPORT_ENDERPEARL
        )
        val destination: Vector3 = Vector3(
            ChorusMath.floorDouble(position.x) + 0.5,
            position.y + 1, ChorusMath.floorDouble(position.z) + 0.5
        )
        shootingEntity!!.teleport(destination, TeleportCause.ENDER_PEARL)
        if (((shootingEntity as Player).gamemode and 0x01) == 0) {
            shootingEntity.attack(EntityDamageByEntityEvent(this, shootingEntity, DamageCause.PROJECTILE, 5f, 0f))
        }
        level!!.addLevelEvent(this.position, LevelEventPacket.EVENT_PARTICLE_TELEPORT)
        level.addLevelEvent(
            shootingEntity.position.add(0.5, 0.5, 0.5),
            LevelEventPacket.EVENT_SOUND_TELEPORT_ENDERPEARL
        )
        if (level!!.gameRules.getBoolean(GameRule.DO_MOB_SPAWNING)) {
            if (ThreadLocalRandom.current().nextInt(1, 20) == 1) {
                val endermite: EntityEndermite? = Entity.Companion.createEntity(
                    EntityID.Companion.ENDERMITE,
                    level!!.getChunk(destination.getFloorX() shr 4, destination.getFloorZ() shr 4), CompoundTag()
                        .putList(
                            "Pos", ListTag<Tag>()
                                .add(FloatTag(destination.getX() + 0.5))
                                .add(FloatTag(destination.getY() + 0.0625))
                                .add(FloatTag(destination.getZ() + 0.5))
                        )
                        .putList(
                            "Motion", ListTag<Tag>()
                                .add(FloatTag(0f))
                                .add(FloatTag(0f))
                                .add(FloatTag(0f))
                        )
                        .putList(
                            "Rotation", ListTag<Tag>()
                                .add(FloatTag(0f))
                                .add(FloatTag(0f))
                        )
                ) as EntityEndermite?
                endermite!!.spawnToAll()
            }
        }
    }

    override fun getOriginalName(): String {
        return "Ender Pearl"
    }
}
