package org.chorus.entity.ai.executor

import org.chorus.Server
import org.chorus.entity.*
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.entity.projectile.EntitySmallFireball
import org.chorus.event.entity.ProjectileLaunchEvent
import org.chorus.level.Sound
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.FloatTag
import org.chorus.nbt.tag.ListTag
import kotlin.math.cos
import kotlin.math.sin

class WitherShootExecutor(protected var targetMemory: MemoryType<out Entity?>) :
    EntityControl, IBehaviorExecutor {
    protected var tick: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        val target = entity.memoryStorage!![targetMemory]
        tick++
        if (tick <= 40) {
            if (tick % 10 == 0) {
                spawn(entity, tick == 40)
            }
            setRouteTarget(entity, entity.position)
            setLookTarget(entity, target.position)
            return true
        } else {
            entity.memoryStorage!!.set<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, entity.level!!.tick)
            return false
        }
    }


    override fun onStart(entity: EntityMob) {
        removeRouteTarget(entity)
        tick = 0
    }

    protected fun spawn(entity: EntityMob, charged: Boolean) {
        val fireballTransform = entity.transform
        fireballTransform.add(entity.directionVector)
        val nbt = CompoundTag()
            .putList(
                "Pos", ListTag<FloatTag>()
                    .add(FloatTag(fireballTransform.position.x))
                    .add(FloatTag(fireballTransform.position.y))
                    .add(FloatTag(fireballTransform.position.z))
            )
            .putList(
                "Motion", ListTag<FloatTag>()
                    .add(FloatTag(-sin(entity.headYaw / 180 * Math.PI) * cos(entity.rotation.pitch / 180 * Math.PI)))
                    .add(FloatTag(-sin(entity.rotation.pitch / 180 * Math.PI)))
                    .add(FloatTag(cos(entity.headYaw / 180 * Math.PI) * cos(entity.rotation.pitch / 180 * Math.PI)))
            )
            .putList(
                "Rotation", ListTag<FloatTag>()
                    .add(FloatTag((if (entity.headYaw > 180) 360 else 0).toFloat() - entity.headYaw))
                    .add(FloatTag(-entity.rotation.pitch.toFloat()))
            )
            .putDouble("damage", 2.0)

        val projectile: Entity = Entity.Companion.createEntity(
            if (charged) EntityID.Companion.WITHER_SKULL_DANGEROUS else EntityID.Companion.WITHER_SKULL,
            entity.level!!.getChunk(entity.position.chunkX, entity.position.chunkZ),
            nbt
        )
        if (projectile == null) {
            return
        }
        if (projectile is EntitySmallFireball) {
            projectile.shootingEntity = entity
        }

        val projectev = ProjectileLaunchEvent(projectile as EntityProjectile, entity)
        Server.instance.pluginManager.callEvent(projectev)
        if (projectev.isCancelled) {
            projectile.kill()
        } else {
            projectile.spawnToAll()
            entity.level!!.addSound(entity.position, Sound.MOB_WITHER_SHOOT)
        }
    }
}
