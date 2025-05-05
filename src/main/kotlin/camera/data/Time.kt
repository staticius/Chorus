package org.chorus_oss.chorus.camera.data

import org.chorus_oss.chorus.nbt.tag.CompoundTag

@JvmRecord
data class Time(@JvmField val fadeIn: Float, @JvmField val hold: Float, @JvmField val fadeOut: Float) :
    SerializableData {
    override fun serialize(): CompoundTag {
        return CompoundTag() //time
            .putFloat("fadeIn", fadeIn)
            .putFloat("hold", hold)
            .putFloat("fadeOut", fadeOut)
    }
}
