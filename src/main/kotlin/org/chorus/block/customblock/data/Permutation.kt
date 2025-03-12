package org.chorus.block.customblock.data

import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.ListTag
import org.chorus.nbt.tag.StringTag

/**
 * The type Permutation builder.
 */
@JvmRecord
data class Permutation(val component: Component, val condition: String?, val blockTags: Array<String>) :
    NBTData {
    constructor(component: Component, condition: String?) : this(component, condition, arrayOf<String>())

    override fun toCompoundTag(): CompoundTag {
        val result = CompoundTag()
            .putCompound("components", component.toCompoundTag())
            .putString("condition", condition!!)
        val stringTagListTag = ListTag<StringTag?>()
        for (s in blockTags) {
            stringTagListTag.add(StringTag(s))
        }
        if (stringTagListTag.size() > 0) {
            result.putList("blockTags", stringTagListTag)
        }
        return result
    }
}
