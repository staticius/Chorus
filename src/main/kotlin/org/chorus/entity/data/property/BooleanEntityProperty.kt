package org.chorus.entity.data.property

import org.chorus.nbt.tag.CompoundTag

/**
 * @author Peng_Lx
 */
class BooleanEntityProperty(identifier: String, private val defaultValue: Boolean) : EntityProperty(identifier) {
    fun getDefaultValue(): Boolean {
        return defaultValue
    }

    override fun populateTag(tag: CompoundTag) {
        tag.putInt("type", 2)
    }
}
