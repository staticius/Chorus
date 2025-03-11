package org.chorus.nbt.tag

import com.google.common.base.Preconditions
import java.util.*

class StringTag : Tag<String> {
    var data: String? = null

    constructor()

    constructor(data: String) {
        this.data = Preconditions.checkNotNull(data, "Empty string not allowed")
    }

    override fun parseValue(): String? {
        return this.data
    }

    override val id: Byte
        get() = TAG_STRING

    override fun toString(): String {
        return "StringTag  (data: $data)"
    }

    override fun toSNBT(): String {
        return "\"" + data + "\""
    }

    override fun toSNBT(space: Int): String {
        return "\"" + data + "\""
    }

    override fun copy(): Tag<String> {
        return StringTag(data!!)
    }

    override fun equals(other: Any?): Boolean {
        if (super.equals(other)) {
            val o = other as StringTag
            return ((data == null && o.data == null) || (data != null && data == o.data))
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), data)
    }
}
