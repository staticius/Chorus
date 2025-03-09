package org.chorus.nbt.tag

class DoubleTag : NumberTag<Double> {
    override var data: Double = 0.0

    constructor()

    constructor(data: Double) {
        this.data = data
    }

    override fun getData(): Double {
        return data
    }

    override fun setData(data: Double?) {
        this.data = data ?: 0.0
    }

    override fun parseValue(): Double {
        return this.data
    }

    override val id: Byte
        get() = Tag.Companion.TAG_Double

    override fun toString(): String {
        return "DoubleTag  (data: $data)"
    }

    override fun toSNBT(): String {
        return data.toString() + "d"
    }

    override fun toSNBT(space: Int): String {
        return data.toString() + "d"
    }

    override fun copy(): Tag {
        return DoubleTag(data)
    }

    override fun equals(obj: Any?): Boolean {
        if (super.equals(obj)) {
            val o = obj as DoubleTag
            return data == o.data
        }
        return false
    }
}
