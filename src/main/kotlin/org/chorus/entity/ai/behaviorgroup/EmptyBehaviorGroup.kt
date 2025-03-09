package org.chorus.entity.ai.behaviorgroup

import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.controller.IController
import cn.nukkit.entity.ai.memory.IMemoryStorage
import cn.nukkit.entity.ai.memory.MemoryStorage
import cn.nukkit.entity.ai.route.finder.IRouteFinder
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.mob.EntityMob

/**
 * 用于未实现AI的实体，作为占位符使用
 */
class EmptyBehaviorGroup(protected var entity: EntityMob) : IBehaviorGroup {
    protected var memoryStorage: IMemoryStorage = MemoryStorage(entity)


    override fun evaluateBehaviors(entity: EntityMob?) {
    }

    override fun evaluateCoreBehaviors(entity: EntityMob?) {
    }

    override fun collectSensorData(entity: EntityMob?) {
    }

    override fun tickRunningBehaviors(entity: EntityMob?) {
    }

    override fun tickRunningCoreBehaviors(entity: EntityMob?) {
    }

    override fun applyController(entity: EntityMob?) {
    }

    override fun getBehaviors(): Set<IBehavior>? {
        return null
    }

    override fun getCoreBehaviors(): Set<IBehavior>? {
        return null
    }

    override fun getRunningBehaviors(): Set<IBehavior>? {
        return null
    }

    override fun getRunningCoreBehaviors(): Set<IBehavior>? {
        return null
    }

    override fun getSensors(): Set<ISensor> {
        return emptySet()
    }

    override fun getControllers(): Set<IController> {
        return emptySet()
    }

    override fun getRouteFinder(): IRouteFinder? {
        return null
    }

    override fun updateRoute(entity: EntityMob?) {
    }

    override fun getMemoryStorage(): IMemoryStorage {
        return this.memoryStorage
    }

    override fun isForceUpdateRoute(): Boolean {
        return false
    }

    override fun setForceUpdateRoute(forceUpdateRoute: Boolean) {
    }
}
