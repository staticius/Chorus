package org.chorus.nbt.tag

class FloatTag : NumberTag<Float> {
    override var data: Float = 0f

    constructor()

    constructor(data: Double) {
        this.data = data.toFloat()
    }

    constructor(data: Float) {
        this.data = data
    }

    override fun getData(): Float {
        return data
    }

    override fun setData(data: Float?) {
        this.data = data ?: 0f
    }

    override fun parseValue(): Float {
        return this.data
    }

    override val id: Byte
        get() = Tag.Companion.TAG_Float

    override fun toString(): String {
        return "FloatTag  (data: $data)"
    }

    override fun toSNBT(): String {
        return data.toString() + "f"
    }

    override fun toSNBT(space: Int): String {
        return data.toString() + "f"
    }

    override fun copy(): Tag {
        return FloatTag(data)
    }

    override fun equals(obj: Any?): Boolean {
        if (super.equals(obj)) {
            val o = obj as FloatTag
            return data == o.data
        }
        return false
    }
}
