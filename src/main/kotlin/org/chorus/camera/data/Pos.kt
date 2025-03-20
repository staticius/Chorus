package org.chorus.camera.data

import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.FloatTag
import org.chorus.nbt.tag.ListTag

@JvmRecord
data class Pos(val x: Float, val y: Float, val z: Float) : SerializableData {
    override fun serialize(): CompoundTag {
        return CompoundTag() //pos
            .putList(
                "pos", ListTag<FloatTag>()
                    .add(FloatTag(x))
                    .add(FloatTag(y))
                    .add(FloatTag(z))
            )
    }
}
