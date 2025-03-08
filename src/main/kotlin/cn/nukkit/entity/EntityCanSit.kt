package cn.nukkit.entity

import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.data.EntityFlag

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
