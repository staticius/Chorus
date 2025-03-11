package org.chorus.nbt.tag

class IntTag : NumberTag<Int> {
    override var data: Int = 0

    constructor()

    constructor(data: Int) {
        this.data = data
    }

    override fun getData(): Int {
        return data
    }

    override fun setData(data: Int?) {
        this.data = data ?: 0
    }

    override fun parseValue(): Int {
        return this.data
    }

    override val id: Byte
        get() = Tag.Companion.TAG_INT

    override fun toString(): String {
        return "IntTag (data: $data)"
    }

    override fun toSNBT(): String {
        return data.toString()
    }

    override fun toSNBT(space: Int): String {
        return data.toString()
    }

    override fun copy(): Tag {
        return IntTag(data)
    }

    override fun equals(obj: Any?): Boolean {
        if (super.equals(obj)) {
            val o = obj as IntTag
            return data == o.data
        }
        return false
    }
}
