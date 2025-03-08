package cn.nukkit.camera.data

import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.Tag

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
