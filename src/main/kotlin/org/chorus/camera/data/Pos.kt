package org.chorus.camera.data

import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.FloatTag
import org.chorus.nbt.tag.ListTag
import org.chorus.nbt.tag.Tag

/**
 * @author daoge_cmd
 * @date 2023/6/11
 * PowerNukkitX Project
 */
@JvmRecord
data class Pos(val x: Float, val y: Float, val z: Float) : SerializableData {
    override fun serialize(): CompoundTag? {
        return CompoundTag() //pos
            .putList(
                "pos", ListTag<Tag>()
                    .add(FloatTag(x))
                    .add(FloatTag(y))
                    .add(FloatTag(z))
            )
    }
}
