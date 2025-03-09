package cn.nukkit.block.customblock.data

import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.Tag

@JvmRecord
data class CollisionBox(
    val originX: Double, val originY: Double, val originZ: Double, val sizeX: Double, val sizeY: Double,
    val sizeZ: Double
) : NBTData {
    override fun toCompoundTag(): CompoundTag? {
        return CompoundTag()
            .putBoolean("enabled", true)
            .putList(
                "origin", ListTag<Tag?>()
                    .add(FloatTag(originX.toFloat()))
                    .add(FloatTag(originY.toFloat()))
                    .add(FloatTag(originZ.toFloat()))
            )
            .putList(
                "size", ListTag<FloatTag?>()
                    .add(FloatTag(sizeX.toFloat()))
                    .add(FloatTag(sizeY.toFloat()))
                    .add(FloatTag(sizeZ.toFloat()))
            )
    }
}
