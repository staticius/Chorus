package org.chorus.nbt.tag

class EndTag : Tag<Void>() {
    override val id: Byte
        get() = TAG_END

    override fun toString(): String {
        return "EndTag"
    }

    override fun toSNBT(): String {
        return ""
    }

    override fun toSNBT(space: Int): String {
        return ""
    }

    override fun copy(): Tag<Void> {
        return EndTag()
    }

    override fun parseValue(): Void? {
        return null
    }
}
