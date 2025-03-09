package org.chorus.camera.data

import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author daoge_cmd
 * @date 2023/6/11
 * PowerNukkitX Project
 */
@JvmRecord
data class Time(@JvmField val fadeIn: Float, @JvmField val hold: Float, @JvmField val fadeOut: Float) : SerializableData {
    override fun serialize(): CompoundTag? {
        return CompoundTag() //time
            .putFloat("fadeIn", fadeIn)
            .putFloat("hold", hold)
            .putFloat("fadeOut", fadeOut)
    }
}
