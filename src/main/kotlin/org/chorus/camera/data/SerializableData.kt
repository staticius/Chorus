package org.chorus.camera.data

import org.chorus.nbt.tag.CompoundTag

/**
 * @author daoge_cmd
 * @date 2023/6/11
 * PowerNukkitX Project
 */
interface SerializableData {
    fun serialize(): CompoundTag?
}
