package org.chorus_oss.chorus.camera.data

import org.chorus_oss.chorus.nbt.tag.CompoundTag

@JvmRecord
data class Rot(val x: Float, val y: Float) : SerializableData {
    override fun serialize(): CompoundTag {
        return CompoundTag() //"rot"
            .putFloat("x", x)
            .putFloat("y", y)
    }
}
