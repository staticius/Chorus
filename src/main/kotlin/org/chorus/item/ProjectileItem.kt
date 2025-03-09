package org.chorus.item

import org.chorus.Player
import org.chorus.entity.*
import org.chorus.entity.Entity.Companion.createEntity
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.event.entity.ProjectileLaunchEvent
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.FloatTag
import org.chorus.nbt.tag.ListTag
import org.chorus.network.protocol.LevelSoundEventPacketV2

/**
 * @author CreeperFace
 */
abstract class ProjectileItem(id: String, meta: Int, count: Int, name: String?) :
    Item(id, meta, count, name) {
    abstract val projectileEntityType: String

    abstract val throwForce: Float

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(player.position.x))
                    .add(FloatTag(player.position.y + player.getEyeHeight() - 0.30000000149011612))
                    .add(FloatTag(player.position.z))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(directionVector.x))
                    .add(FloatTag(directionVector.y))
                    .add(FloatTag(directionVector.z))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag(player.rotation.yaw.toFloat()))
                    .add(FloatTag(player.rotation.pitch.toFloat()))
            )

        this.correctNBT(nbt)

        var projectile = createEntity(
            this.projectileEntityType,
            player.level!!.getChunk(player.position.floorX shr 4, player.position.floorZ shr 4),
            nbt,
            player
        )
        if (projectile != null) {
            projectile = correctProjectile(player, projectile)
            if (projectile == null) {
                return false
            }

            projectile.setMotion(projectile.getMotion().multiply(throwForce.toDouble()))

            if (projectile is EntityProjectile) {
                val ev = ProjectileLaunchEvent(projectile, player)

                player.getServer().getPluginManager().callEvent(ev)
                if (ev.isCancelled) {
                    projectile.kill()
                } else {
                    if (!player.isCreative) {
                        count--
                    }
                    projectile.spawnToAll()
                    addThrowSound(player)
                }
            }
        } else {
            return false
        }
        return true
    }

    protected fun addThrowSound(player: Player) {
        player.level!!.addLevelSoundEvent(
            player.position,
            LevelSoundEventPacketV2.SOUND_THROW,
            -1,
            "minecraft:player",
            false,
            false
        )
    }

    protected open fun correctProjectile(player: Player, projectile: Entity): Entity? {
        return projectile
    }

    protected open fun correctNBT(nbt: CompoundTag) {
    }
}
