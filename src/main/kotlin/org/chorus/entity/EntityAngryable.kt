package org.chorus.entity

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.data.EntityFlag

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
