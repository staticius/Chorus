package org.chorus.entity.ai.sensor

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.item.EntityItem
import org.chorus.entity.mob.EntityMob
import org.chorus.item.Item


//存储最近的玩家的Memory

class NearestItemSensor @JvmOverloads constructor(
    protected var range: Double,
    protected var minRange: Double,
    override var period: Int = 1
) :
    ISensor {
    override fun sense(entity: EntityMob) {
        val itemClass =
            entity.memoryStorage!!.get<Class<out Item>>(CoreMemoryTypes.Companion.LOOKING_ITEM)

        var item: EntityItem? = null
        val rangeSquared = this.range * this.range
        val minRangeSquared = this.minRange * this.minRange
        //寻找范围内最近的玩家
        for (e in entity.level!!.entities) {
            if (e is EntityItem) {
                if (itemClass.isAssignableFrom(e.item!!.javaClass)) {
                    if (entity.position.distanceSquared(e.position) <= rangeSquared && entity.position.distanceSquared(e.position) >= minRangeSquared) {
                        if (item == null) {
                            item = e
                        } else {
                            if (entity.position.distanceSquared(e.position) < entity.position.distanceSquared(item.position)) {
                                item = e
                            }
                        }
                    }
                }
            }
        }
        entity.memoryStorage!!.put<EntityItem>(CoreMemoryTypes.Companion.NEAREST_ITEM, item)
    }
}
