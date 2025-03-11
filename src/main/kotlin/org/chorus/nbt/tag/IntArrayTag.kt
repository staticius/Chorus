package org.chorus.nbt.tag

class IntArrayTag @JvmOverloads constructor(var data: IntArray? = IntArray(0)) : Tag<IntArray>() {
    fun getData(): IntArray? {
        return data
    }

    fun setData(data: IntArray?) {
        this.data = data ?: IntArray(0)
    }

    override fun parseValue(): IntArray? {
        return this.data
    }

    override val id: Byte
        get() = TAG_INT_ARRAY

    override fun toString(): String {
        return "IntArrayTag " + " [" + data!!.size + " bytes]"
    }

    override fun toSNBT(): String {
        return data.contentToString().replace("[", "[I;")
    }

    override fun toSNBT(space: Int): String {
        return data.contentToString().replace("[", "[I;")
    }

    override fun equals(other: Any?): Boolean {
        if (super.equals(other)) {
            val intArrayTag = other as IntArrayTag
            return ((data == null && intArrayTag.data == null) || (data != null && data.contentEquals(intArrayTag.data)))
        }
        return false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun copy(): Tag<IntArray> {
        val cp = IntArray(data!!.size)
        System.arraycopy(data!!, 0, cp, 0, data!!.size)
        return IntArrayTag(cp)
    }
}
