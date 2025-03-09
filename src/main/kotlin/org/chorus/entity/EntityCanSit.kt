package org.chorus.entity

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.data.EntityFlag

/**
 * 可坐下实体接口
 *
 *
 */
interface EntityCanSit : EntityComponent {
    fun isSitting(): Boolean {
        return getMemoryStorage().get<Boolean>(CoreMemoryTypes.Companion.IS_SITTING)
    }

    fun setSitting(sitting: Boolean) {
        getMemoryStorage().put<Boolean>(CoreMemoryTypes.Companion.IS_SITTING, sitting)
        asEntity()!!.setDataFlag(EntityFlag.SITTING, sitting)
    }
}
