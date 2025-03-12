package org.chorus.nbt.tag

class ShortTag : NumberTag<Int> {
    override var data: Short = 0

    constructor()

    constructor(data: Int) {
        this.data = data.toShort()
    }

    override fun getData(): Int {
        return data.toInt()
    }

    override fun setData(data: Int?) {
        this.data = (data ?: 0).toShort()
    }

    override fun parseValue(): Int {
        return data.toInt()
    }

    override val id: Byte
        get() = TAG_SHORT

    override fun toString(): String {
        return "ShortTag (data: $data)"
    }

    override fun toSNBT(): String {
        return data.toString() + "s"
    }

    override fun toSNBT(space: Int): String {
        return data.toString() + "s"
    }

    override fun copy(): Tag {
        return ShortTag(data.toInt())
    }

    override fun equals(obj: Any?): Boolean {
        if (super.equals(obj)) {
            val o = obj as ShortTag
            return data == o.data
        }
        return false
    }
}
