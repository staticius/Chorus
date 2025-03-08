package cn.nukkit.nbt.tag

class EndTag : Tag() {
    override val id: Byte
        get() = Tag.Companion.TAG_End

    override fun toString(): String {
        return "EndTag"
    }

    override fun toSNBT(): String {
        return ""
    }

    override fun toSNBT(space: Int): String {
        return ""
    }

    override fun copy(): Tag {
        return EndTag()
    }

    override fun parseValue(): Void? {
        return null
    }
}
