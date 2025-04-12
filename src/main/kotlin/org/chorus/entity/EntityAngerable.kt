package org.chorus.entity

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.data.EntityFlag

interface EntityAngerable : EntityComponent {
    fun isAngry(): Boolean {
        return memoryStorage[CoreMemoryTypes.IS_ANGRY]
    }

    fun setAngry(angry: Boolean) {
        memoryStorage.set(CoreMemoryTypes.IS_ANGRY, angry)
        asEntity()!!.setDataFlag(EntityFlag.ANGRY, angry)
    }
}
