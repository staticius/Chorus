package cn.nukkit.entity

import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.data.EntityFlag

/**
 * 可生气实体
 *
 *
 */
interface EntityAngryable : EntityComponent {
    fun isAngry(): Boolean {
        return getMemoryStorage().get<Boolean>(CoreMemoryTypes.Companion.IS_ANGRY)
    }

    fun setAngry(angry: Boolean) {
        getMemoryStorage().put<Boolean>(CoreMemoryTypes.Companion.IS_ANGRY, angry)
        asEntity()!!.setDataFlag(EntityFlag.ANGRY, angry)
    }
}
