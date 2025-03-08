package cn.nukkit.level.updater.util

import cn.nukkit.nbt.tag.*

object TagUtils {
    fun toMutable(immutable: Tag): Any {
        return when (immutable) {
            -> {
                val mutable: MutableMap<String, Any> = LinkedHashMap()
                t.getTags().forEach { (k: String?, v: Tag?) -> mutable.put(k, toMutable(v)) }
                mutable
            }

            -> {
                val list: MutableList<Any> = ArrayList()
                t.getAll().forEach(({ v: Any -> list.add(toMutable(v)) }))
                list
            }

            -> (t.parseValue() and 0xff).toByte()
            -> (t.parseValue() and 0xffff).toShort()
            else -> immutable.parseValue<Any>()
        }
    }

    fun toImmutable(mutable: Any): Tag {
        return when (mutable) {
            -> {
                val compoundTag = CompoundTag()
                map.forEach { k: Any?, v: Any ->
                    if (k is String) {
                        compoundTag.put(k, toImmutable(v))
                    }
                }
                compoundTag
            }

            -> {
                val listTag = ListTag<Tag>()
                list.forEach { v: Any -> listTag.add(toImmutable(v)) }
                listTag
            }

            else -> byClass(mutable)
        }
    }

    private fun byClass(mutable: Any): Tag {
        return when (mutable) {
            -> IntTag(v)
            -> ByteTag(v.toInt())
            -> ShortTag(v.toInt())
            -> LongTag(v)
            -> FloatTag(v)
            -> DoubleTag(v)
            -> StringTag(v)
            -> ByteArrayTag(v)
            -> IntArrayTag(v)
            null -> EndTag()
            else -> throw IllegalArgumentException("unhandled error in TagUtils")
        }
    }
}
