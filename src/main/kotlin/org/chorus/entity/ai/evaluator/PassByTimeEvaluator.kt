package org.chorus.entity.ai.evaluator

import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.mob.EntityMob

/**
 * 用于判断一个时间类型的记忆是否在指定范围内的评估器
 *
 *
 * An evaluator used to determine whether a time type of memory is within a specified range
 */
class PassByTimeEvaluator
/**
 * 用于判断一个时间类型的记忆是否在指定范围内的评估器
 *
 *
 * An evaluator used to determine whether a time type of memory is within a specified range
 *
 * @param timedMemory        the timed memory
 * @param minPassByTimeRange the min pass by time range
 * @param maxPassByTimeRange the max pass by time range
 */ @JvmOverloads constructor(
    protected var timedMemory: MemoryType<Int?>,
    protected var minPassByTimeRange: Int,
    protected var maxPassByTimeRange: Int = Int.MAX_VALUE
) :
    IBehaviorEvaluator {
    override fun evaluate(entity: EntityMob): Boolean {
        val time = entity.memoryStorage!!.get(timedMemory)
        val passByTime = entity.level!!.tick - time!!
        return passByTime >= minPassByTimeRange && passByTime <= maxPassByTimeRange
    }
}
