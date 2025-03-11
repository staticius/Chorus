package org.chorus.nbt.tag

class LongTag : NumberTag<Long> {
    override var data: Long = 0

    constructor()

    constructor(data: Long) {
        this.data = data
    }

    override fun getData(): Long {
        return data
    }

    override fun setData(data: Long?) {
        this.data = data ?: 0
    }

    override fun parseValue(): Long {
        return this.data
    }

    override val id: Byte
        get() = Tag.Companion.TAG_LONG

    override fun toString(): String {
        return "LongTag  (data:$data)"
    }

    override fun toSNBT(): String {
        return data.toString() + "L"
    }

    override fun toSNBT(space: Int): String {
        return data.toString() + "L"
    }

    override fun copy(): Tag {
        return LongTag(data)
    }

    override fun equals(obj: Any?): Boolean {
        if (super.equals(obj)) {
            val o = obj as LongTag
            return data == o.data
        }
        return false
    }
}
