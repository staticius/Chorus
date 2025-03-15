package org.chorus.nbt.tag

import java.util.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

class TreeMapCompoundTag : CompoundTag {
    constructor() : super(TreeMap<String, Tag<*>>())

    constructor(tags: Map<String, Tag<*>>) : super(TreeMap<String, Tag<*>>(tags))

    override fun getTags(): MutableMap<String, Tag<*>> {
        return TreeMap(this.tags)
    }

    override fun parseValue(): MutableMap<String, Any> {
        val value: MutableMap<String, Any> = TreeMap()
        for ((key, value1) in tags) {
            value[key] = value1.parseValue()
        }
        return value
    }

    override fun copy(): TreeMapCompoundTag {
        val nbt = TreeMapCompoundTag()
        getTags().forEach { (key: String?, value: Tag<*>?) -> nbt.put(key, value.copy()) }
        return nbt
    }
}
