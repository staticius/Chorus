package cn.nukkit.camera.data

import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author daoge_cmd
 * @date 2023/6/11
 * PowerNukkitX Project
 */
@JvmRecord
data class Ease(@JvmField val time: Float, @JvmField val easeType: EaseType) : SerializableData {
    override fun serialize(): CompoundTag? {
        return CompoundTag() //ease
            .putFloat("time", time)
            .putString("type", easeType.type)
    }
}
