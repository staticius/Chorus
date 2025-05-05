package org.chorus_oss.chorus.camera.data

import org.chorus_oss.chorus.nbt.tag.CompoundTag

@JvmRecord
data class Ease(@JvmField val time: Float, @JvmField val easeType: EaseType) : SerializableData {
    override fun serialize(): CompoundTag {
        return CompoundTag() //ease
            .putFloat("time", time)
            .putString("type", easeType.type)
    }
}
