package org.chorus.entity.ai.executor.enderdragon

import org.chorus.entity.*
import org.chorus.entity.ai.executor.EntityControl
import org.chorus.entity.ai.executor.IBehaviorExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.item.EntityEnderCrystal
import org.chorus.entity.mob.EntityMob
import org.chorus.entity.mob.monster.EntityEnderDragon
import org.chorus.math.*
import org.chorus.utils.*

import java.util.*
import kotlin.math.cos
import kotlin.math.sin


class CircleMovementExecutor(//指示执行器应该从哪个Memory获取目标位置
    protected var memory: MemoryType<out Vector3?>?,
    protected var speed: Float,
    protected var updateRouteImmediatelyWhenTargetChange: Boolean,
    private val size: Int,
    private val sections: Int,
    private val circles: Int
) :
    EntityControl, IBehaviorExecutor {
    private var startLoc = 0
    private var circleLoc = 0

    private var lastLocation: Vector3? = null

    private val ticks = 0


    constructor(memory: MemoryType<out Vector3?>?, speed: Float, size: Int, sections: Int, circles: Int) : this(
        memory,
        speed,
        false,
        size,
        sections,
        circles
    )

    override fun execute(entity: EntityMob): Boolean {
        if (entity.isEnablePitch) entity.isEnablePitch = false
        if (needUpdateTarget(entity)) {
            circleLoc++
            val target = next(entity)
            lastLocation = target
            if (entity.movementSpeed != speed) entity.movementSpeed = speed
            entity.behaviorGroup!!.isForceUpdateRoute = updateRouteImmediatelyWhenTargetChange
        }
        setRouteTarget(entity, lastLocation)
        setLookTarget(entity, lastLocation)
        return circleLoc < circles
    }

    override fun onStart(entity: EntityMob?) {
        startLoc = Utils.rand(0, sections)
        circleLoc = 0
    }

    override fun onInterrupt(entity: EntityMob) {
        stop(entity)
    }

    override fun onStop(entity: EntityMob) {
        stop(entity)
        if (entity is EntityEnderDragon) {
            if (Utils.rand(0, 3 + Arrays.stream<Entity>(entity.level!!.entities).filter { entity1: Entity ->
                    entity1 is EntityEnderCrystal && entity1.position.toHorizontal().distance(
                        Vector2.ZERO
                    ) < 128
                }.count().toInt()) < 1) {
                entity.getMemoryStorage()!!.set<Boolean>(CoreMemoryTypes.Companion.FORCE_PERCHING, true)
            }
        }
    }

    protected fun stop(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        entity.isEnablePitch = true
    }

    protected fun needUpdateTarget(entity: EntityMob): Boolean {
        entity.recalculateBoundingBox(false)
        return lastLocation == null || entity.getBoundingBox().grow(10.0, 10.0, 10.0).isVectorInside(lastLocation)
    }

    protected fun next(entity: EntityMob): Vector3 {
        val origin = entity.behaviorGroup!!.memoryStorage!![memory]
        val angleIncrement = 360.0 / sections
        val angle = Math.toRadians(((circleLoc + startLoc) * angleIncrement))
        val particleX = origin!!.getX() + cos(angle) * size
        val particleZ = origin!!.getZ() + sin(angle) * size
        return Vector3(particleX, origin!!.y + Utils.rand(-5, 7), particleZ)
    }
}
