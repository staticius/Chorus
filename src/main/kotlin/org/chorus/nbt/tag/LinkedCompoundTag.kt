package org.chorus.nbt.tag

import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

class LinkedCompoundTag @JvmOverloads constructor(tags: MutableMap<String, Tag<*>> = LinkedHashMap()) :
    CompoundTag(tags) {
    override fun getTags(): MutableMap<String, Tag<*>> {
        return LinkedHashMap(this.tags)
    }

    override fun parseValue(): MutableMap<String, Any> {
        val value: MutableMap<String, Any> = LinkedHashMap(
            tags.size
        )

        for ((key, value1) in tags) {
            value[key] = value1.parseValue<Any>()
        }

        return value
    }

    override fun copy(): LinkedCompoundTag {
        val nbt = LinkedCompoundTag()
        getTags().forEach { (key, value) -> nbt.put(key, value.copy()) }
        return nbt
    }
}
