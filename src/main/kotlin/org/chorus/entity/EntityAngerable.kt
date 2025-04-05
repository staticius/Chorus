package org.chorus.entity

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.data.EntityFlag

interface EntityAngerable : EntityComponent {
    fun isAngry(): Boolean {
        return getMemoryStorage()[CoreMemoryTypes.IS_ANGRY]!!
    }

    fun setAngry(angry: Boolean) {
        getMemoryStorage().set(CoreMemoryTypes.IS_ANGRY, angry)
        asEntity()!!.setDataFlag(EntityFlag.ANGRY, angry)
    }
}
