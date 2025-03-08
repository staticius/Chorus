package cn.nukkit.nbt.tag

class ByteTag : NumberTag<Int> {
    override var data: Int = 0

    constructor()

    constructor(data: Int) {
        this.data = data.toByte().toInt()
    }

    override fun getData(): Int {
        return data
    }

    override fun setData(data: Int?) {
        this.data = (data ?: 0).toByte().toInt()
    }

    override val id: Byte
        get() = Tag.Companion.TAG_Byte

    override fun parseValue(): Int {
        return data
    }

    override fun toString(): String {
        var hex = Integer.toHexString(this.data)
        if (hex.length < 2) {
            hex = "0$hex"
        }
        return "ByteTag  (data: 0x$hex)"
    }

    override fun toSNBT(): String {
        return data.toString() + "b"
    }

    override fun toSNBT(space: Int): String {
        return data.toString() + "b"
    }

    override fun equals(obj: Any?): Boolean {
        if (super.equals(obj)) {
            val byteTag = obj as ByteTag
            return data == byteTag.data
        }
        return false
    }

    override fun copy(): Tag {
        return ByteTag(data)
    }
}
