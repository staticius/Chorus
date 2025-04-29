package org.chorus_oss.chorus.camera.data

import org.chorus_oss.chorus.nbt.tag.CompoundTag

interface SerializableData {
    fun serialize(): CompoundTag?
}
