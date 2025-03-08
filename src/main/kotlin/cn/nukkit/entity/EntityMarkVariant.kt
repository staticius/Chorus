package cn.nukkit.entity

import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.utils.*

/**
 * 实现这个接口的实体拥有次要变种属性
 */
interface EntityMarkVariant : EntityComponent {
    fun getMarkVariant(): Int {
        return getMemoryStorage().get<Int>(CoreMemoryTypes.Companion.MARK_VARIANT)
    }

    fun setMarkVariant(variant: Int) {
        getMemoryStorage().put<Int>(CoreMemoryTypes.Companion.MARK_VARIANT, variant)
    }

    fun hasMarkVariant(): Boolean {
        return getMemoryStorage().notEmpty(CoreMemoryTypes.Companion.MARK_VARIANT)
    }

    /**
     * 随机一个变种值
     */
    fun randomMarkVariant(): Int {
        return getAllMarkVariant().get(Utils.rand(0, getAllMarkVariant().length - 1))
    }

    /**
     * 定义全部可能的变种
     */
    fun getAllMarkVariant(): IntArray
}
