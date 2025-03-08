package cn.nukkit.nbt.tag

import java.util.*

abstract class Tag protected constructor() {
    abstract override fun toString(): String

    abstract fun toSNBT(): String

    abstract fun toSNBT(space: Int): String

    abstract val id: Byte

    abstract fun copy(): Tag

    abstract fun <T> parseValue(): T?

    override fun equals(obj: Any?): Boolean {
        if (obj !is Tag) {
            return false
        }
        return id == obj.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    companion object {
        const val TAG_End: Byte = 0
        const val TAG_Byte: Byte = 1
        const val TAG_Short: Byte = 2
        const val TAG_Int: Byte = 3
        const val TAG_Long: Byte = 4
        const val TAG_Float: Byte = 5
        const val TAG_Double: Byte = 6
        const val TAG_Byte_Array: Byte = 7
        const val TAG_String: Byte = 8
        const val TAG_List: Byte = 9
        const val TAG_Compound: Byte = 10
        const val TAG_Int_Array: Byte = 11

        fun newTag(type: Byte): Tag {
            return when (type) {
                TAG_End -> EndTag()
                TAG_Byte -> ByteTag()
                TAG_Short -> ShortTag()
                TAG_Int -> IntTag()
                TAG_Long -> LongTag()
                TAG_Float -> FloatTag()
                TAG_Double -> DoubleTag()
                TAG_Byte_Array -> ByteArrayTag()
                TAG_Int_Array -> IntArrayTag()
                TAG_String -> StringTag()
                TAG_List -> ListTag<Tag>()
                TAG_Compound -> CompoundTag()
                else -> EndTag()
            }
        }

        fun getTagName(type: Byte): String {
            return when (type) {
                TAG_End -> "TAG_End"
                TAG_Byte -> "TAG_Byte"
                TAG_Short -> "TAG_Short"
                TAG_Int -> "TAG_Int"
                TAG_Long -> "TAG_Long"
                TAG_Float -> "TAG_Float"
                TAG_Double -> "TAG_Double"
                TAG_Byte_Array -> "TAG_Byte_Array"
                TAG_Int_Array -> "TAG_Int_Array"
                TAG_String -> "TAG_String"
                TAG_List -> "TAG_List"
                TAG_Compound -> "TAG_Compound"
                else -> "UNKNOWN"
            }
        }

        fun getTagType(type: Class<*>): Int {
            return if (type == ListTag::class.java) {
                TAG_List.toInt()
            } else if (type == CompoundTag::class.java) {
                TAG_Compound.toInt()
            } else if (type == EndTag::class.java) {
                TAG_End.toInt()
            } else if (type == ByteTag::class.java) {
                TAG_Byte.toInt()
            } else if (type == ShortTag::class.java) {
                TAG_Short.toInt()
            } else if (type == IntTag::class.java) {
                TAG_Int.toInt()
            } else if (type == FloatTag::class.java) {
                TAG_Float.toInt()
            } else if (type == LongTag::class.java) {
                TAG_Long.toInt()
            } else if (type == DoubleTag::class.java) {
                TAG_Double.toInt()
            } else if (type == ByteArrayTag::class.java) {
                TAG_Byte_Array.toInt()
            } else if (type == IntArrayTag::class.java) {
                TAG_Int_Array.toInt()
            } else if (type == StringTag::class.java) {
                TAG_Int_Array.toInt()
            } else {
                throw IllegalArgumentException("Unknown tag type " + type.simpleName)
            }
        }
    }
}
