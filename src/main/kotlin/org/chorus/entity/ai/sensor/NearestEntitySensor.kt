package org.chorus.entity.ai.sensor

import org.chorus.entity.Entity
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.mob.EntityMob
import lombok.Getter

//存储最近的玩家的Memory
@Getter
class NearestEntitySensor @JvmOverloads constructor(
    protected var entityClass: Class<out Entity>,
    protected var memoryType: MemoryType<Entity?>,
    protected var range: Double,
    protected var minRange: Double,
    override var period: Int = 1
) :
    ISensor {
    override fun sense(entity: EntityMob) {
        var ent: Entity? = null
        val rangeSquared = this.range * this.range
        val minRangeSquared = this.minRange * this.minRange
        //寻找范围内最近的玩家
        for (e in entity.level!!.entities) {
            if (entityClass.isAssignableFrom(e.javaClass)) {
                if (entity.position.distanceSquared(e.position) <= rangeSquared && entity.position.distanceSquared(e.position) >= minRangeSquared) {
                    if (ent == null) {
                        ent = e
                    } else {
                        if (entity.position.distanceSquared(e.position) < entity.position.distanceSquared(ent.position)) {
                            ent = e
                        }
                    }
                }
            }
        }
        if (ent == null) {
            if (entity.memoryStorage!!.notEmpty(memoryType) && entity.memoryStorage!!.get(memoryType).javaClass.isAssignableFrom(
                    entityClass
                )
            ) {
                entity.memoryStorage!!.clear(memoryType)
            } // We don't want to clear data from different sensors
        } else entity.memoryStorage!!.put(memoryType, ent)
    }
}
