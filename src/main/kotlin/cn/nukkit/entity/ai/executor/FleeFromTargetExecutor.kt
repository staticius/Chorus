package cn.nukkit.entity.ai.executor

import cn.nukkit.entity.EntityLiving
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.math.*
import lombok.Getter

@Getter
open class FleeFromTargetExecutor @JvmOverloads constructor(//指示执行器应该从哪个Memory获取目标位置
    protected var memory: MemoryType<out IVector3?>,
    protected var speed: Float,
    protected var updateRouteImmediatelyWhenTargetChange: Boolean = false,
    protected var minDistance: Float = -1f,
    protected var clearDataWhenLose: Boolean = false
) :
    EntityControl, IBehaviorExecutor {
    protected var oldTarget: Vector3? = null
    protected var enableRangeTest: Boolean = false

    override fun execute(entity: EntityMob): Boolean {
        if (!entity.isEnablePitch) entity.isEnablePitch = true
        if (entity.behaviorGroup!!.memoryStorage!!.isEmpty(memory)) {
            return false
        }

        val target = entity.behaviorGroup!!.memoryStorage!![memory].getVector3()
        val moveTarget = target.add(
            Vector3(
                entity.position.south - target.x,
                entity.position.up - target.y,
                entity.position.west - target.z
            ).normalize().multiply(minDistance.toDouble())
        )

        if (target.distance(entity.position) > minDistance) {
            setLookTarget(entity, target)
            return false
        }

        setRouteTarget(entity, moveTarget)
        setLookTarget(entity, moveTarget)

        //This gives the Evoker enough time to turn around before attacking.
        entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, entity.level!!.tick)

        if (updateRouteImmediatelyWhenTargetChange) {
            val floor = target.floor()

            if (oldTarget == null || oldTarget == floor) entity.behaviorGroup!!.isForceUpdateRoute = true

            oldTarget = floor
        }

        if (entity.movementSpeed != speed) entity.movementSpeed = speed

        return true
    }

    override fun onInterrupt(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        entity.isEnablePitch = false
        if (clearDataWhenLose) entity.behaviorGroup!!.memoryStorage!!.clear(memory)
    }

    override fun onStop(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        entity.isEnablePitch = false
        if (clearDataWhenLose) entity.behaviorGroup!!.memoryStorage!!.clear(memory)
    }
}
