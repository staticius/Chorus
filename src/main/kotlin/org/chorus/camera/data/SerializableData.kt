package org.chorus.camera.data

import org.chorus.nbt.tag.CompoundTag

interface SerializableData {
    fun serialize(): CompoundTag?
}
