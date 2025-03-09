package org.chorus.entity

import cn.nukkit.entity.ai.memory.CoreMemoryTypes

/**
 * 实体可剪切
 *
 *
 * 例如羊就可被剪羊毛
 *
 *
 * 若作用于此实体的物品的isShears()为true，则将会调用此方法
 * <br></br>
 * Entities that can be sheared. Stores value with [CoreMemoryTypes.IS_SHEARED]
 */
interface EntityShearable : EntityComponent {
    /**
     * @return 此次操作是否有效。若有效，将会减少物品耐久 true if shearing succeeded.
     */
    fun shear(): Boolean {
        if (this.isSheared() || (asEntity() is EntityAgeable && age.isBaby())) {
            return false
        }
        this.setIsSheared(true)
        return true
    }

    fun isSheared(): Boolean {
        return getMemoryStorage().get<Boolean>(CoreMemoryTypes.Companion.IS_SHEARED)
    }

    fun setIsSheared(isSheared: Boolean) {
        getMemoryStorage().put<Boolean>(CoreMemoryTypes.Companion.IS_SHEARED, isSheared)
    }
}
