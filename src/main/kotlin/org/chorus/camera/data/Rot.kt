package org.chorus.camera.data

import org.chorus.nbt.tag.CompoundTag

/**
 * @author daoge_cmd
 * @date 2023/6/11
 * PowerNukkitX Project
 */
@JvmRecord
data class Rot(val x: Float, val y: Float) : SerializableData {
    override fun serialize(): CompoundTag? {
        return CompoundTag() //"rot"
            .putFloat("x", x)
            .putFloat("y", y)
    }
}
