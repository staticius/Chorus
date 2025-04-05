package org.chorus.entity

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.utils.*

/**
 * 实现这个接口的实体拥有变种属性
 */
interface EntityVariant : EntityComponent {
    fun getVariant(): Int {
        return getMemoryStorage()[CoreMemoryTypes.VARIANT]!!
    }

    fun setVariant(variant: Int) {
        getMemoryStorage()[CoreMemoryTypes.VARIANT] = variant
    }

    fun hasVariant(): Boolean {
        return getMemoryStorage().notEmpty(CoreMemoryTypes.VARIANT)
    }

    /**
     * 随机一个变种值
     */
    fun randomVariant(): Int {
        return getAllVariant()[Utils.rand(0, getAllVariant().size - 1)]
    }

    /**
     * 定义全部可能的变种
     */
    fun getAllVariant(): IntArray
}
