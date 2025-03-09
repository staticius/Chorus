package org.chorus.block.customblock.data

import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.ListTag
import org.chorus.nbt.tag.StringTag
import java.util.function.Consumer

@JvmRecord
data class CraftingTable(val tableName: String, val craftingTags: List<String>?) : NBTData {
    override fun toCompoundTag(): CompoundTag? {
        val listTag = ListTag<StringTag?>()
        craftingTags?.forEach(Consumer { t: String? ->
            listTag.add(
                StringTag(
                    t!!
                )
            )
        })
        return CompoundTag()
            .putList("crafting_tags", listTag)
            .putString("table_name", tableName)
    }
}
