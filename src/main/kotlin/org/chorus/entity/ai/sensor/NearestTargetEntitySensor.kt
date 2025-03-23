package org.chorus.entity.ai.sensor

import org.chorus.entity.*
import org.chorus.entity.ai.memory.MemoryType
import org.chorus.entity.mob.EntityMob
import org.chorus.utils.*
import java.util.*
import java.util.function.Function

/**
 * 用来搜索最近的目标实体，构造函数中接受一个目标函数`Function<T, Boolean> target`的Set，用于实体检测，最终结果保存到`List<MemoryType<Entity>> memories`中.
 *
 *
 * The constructor accepts a Set of Integer to target function `Function<T, Boolean> target` to search for the nearest target entity, and the final result is saved to `List<MemoryType<Entity>> memories`.
 */
class NearestTargetEntitySensor<T : Entity?> @SafeVarargs constructor(
    protected var minRange: Double,
    protected var maxRange: Double,
    override var period: Int,
    memories: List<MemoryType<Entity?>?>,
    vararg allTargetFunction: Function<T?, Boolean>
) :
    ISensor {
    protected var allTargetFunction: Array<Function<T, Boolean>>?

    protected var memories: List<MemoryType<Entity?>?>

    /**
     * 不指定目标函数，默认将全部结果存入第一个记忆
     *
     *
     * Without specifying the target function, all results will be stored in the first memory by default
     *
     * @see .NearestTargetEntitySensor
     */
    constructor(minRange: Double, maxRange: Double, memories: List<MemoryType<Entity?>?>) : this(
        minRange,
        maxRange,
        1,
        memories,
        null as Function<T, Boolean?>?
    )

    /**
     * @param minRange          最小搜索范围<br></br>Minimum Search Range
     * @param maxRange          最大搜索范围<br></br>Maximum Search Range
     * @param period            传感器执行周期，单位tick<br></br>Senor execute period
     * @param allTargetFunction 接收一个Set，将指定目标函数筛选的结果映射到指定索引的记忆上，目标函数接受一个参数T，返回一个Boolean<br></br>Receives a Set that set the results filtered by the specified target function to the memory of the specified index, the target function accepts a parameter T and returns a Boolean
     * @param memories          保存结果的记忆类型<br></br>Memory class type for saving results
     */
    init {
        if (allTargetFunction == null) this.allTargetFunction = null
        else {
            if (memories.size >= 1 && allTargetFunction.size == memories.size) {
                this.allTargetFunction = allTargetFunction
            } else throw IllegalArgumentException("All Target Function must correspond to memories one by one")
        }
        this.memories = memories
    }

    override fun sense(entity: EntityMob) {
        val minRangeSquared = this.minRange * this.minRange
        val maxRangeSquared = this.maxRange * this.maxRange

        if (allTargetFunction == null && memories.size == 1) {
            val currentMemory = memories[0]
            val current = entity.memoryStorage!!.get(currentMemory)
            if (current != null && current.isAlive) return

            //寻找范围内最近的实体
            val entities = Collections.synchronizedList(SortedList(Comparator.comparingDouble { e: Entity ->
                e.position.distanceSquared(
                    entity.position
                )
            }))
            for (p in entity.level!!.entities) {
                if (entity.position.distanceSquared(p.position) <= maxRangeSquared && entity.position.distanceSquared(p.position) >= minRangeSquared && (p != entity)) {
                    entities.add(p)
                }
            }

            if (entities.isEmpty()) {
                entity.memoryStorage!!.clear(currentMemory)
            } else entity.memoryStorage!!.set(currentMemory, entities[0])
            return
        }
        if (allTargetFunction != null) {
            val sortEntities: MutableList<MutableList<Entity>> = ArrayList(memories.size)

            run {
                var i = 0
                val len = memories.size
                while (i < len) {
                    sortEntities.add(SortedList(Comparator.comparingDouble { e: Entity ->
                        e.position.distanceSquared(
                            entity.position
                        )
                    }))
                    ++i
                }
            }

            for (p in entity.level!!.entities) {
                if (entity.position.distanceSquared(p.position) <= maxRangeSquared && entity.position.distanceSquared(p.position) >= minRangeSquared && (p != entity)) {
                    var i = 0
                    for (targetFunction in allTargetFunction!!) {
                        if (targetFunction.apply(p as T)) {
                            sortEntities[i].add(p)
                        }
                        ++i
                    }
                }
            }

            var i = 0
            val len = sortEntities.size
            while (i < len) {
                val currentMemory = memories[i]
                val current = entity.memoryStorage!!.get(currentMemory)
                if (current != null && current.isAlive) {
                    ++i
                    continue
                }

                if (sortEntities[i].isEmpty()) {
                    entity.memoryStorage!!.clear(currentMemory)
                } else entity.memoryStorage!!.set(currentMemory, sortEntities[i][0])
                ++i
            }
        }
    }
}