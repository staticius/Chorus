package org.chorus.entity.ai.executor

import cn.nukkit.Server
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.entity.projectile.EntitySmallFireball
import cn.nukkit.event.entity.ProjectileLaunchEvent
import cn.nukkit.level.Sound
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import kotlin.math.cos
import kotlin.math.sin

class WitherShootExecutor(protected var targetMemory: MemoryType<out Entity?>) :
    EntityControl, IBehaviorExecutor {
    protected var tick: Int = 0

    override fun execute(entity: EntityMob): Boolean {
        val target = entity.memoryStorage!![targetMemory] ?: return false
        tick++
        if (tick <= 40) {
            if (tick % 10 == 0) {
                spawn(entity, tick == 40)
            }
            setRouteTarget(entity, entity.position)
            setLookTarget(entity, target.position)
            return true
        } else {
            entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, entity.level!!.tick)
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
        Server.getInstance().pluginManager.callEvent(projectev)
        if (projectev.isCancelled) {
            projectile.kill()
        } else {
            projectile.spawnToAll()
            entity.level!!.addSound(entity.position, Sound.MOB_WITHER_SHOOT)
        }
    }
}
