package cn.nukkit.block.customblock.data

import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.StringTag
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
