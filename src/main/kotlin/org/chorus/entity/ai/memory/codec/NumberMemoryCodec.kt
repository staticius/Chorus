package org.chorus.entity.ai.memory.codec

import org.chorus.nbt.tag.*
import java.util.function.BiConsumer
import java.util.function.Function

class NumberMemoryCodec<Data : Number?>(key: String?) : MemoryCodec<Data>(
    Function { tag: CompoundTag ->
        if (tag.contains(key)) (TagReader(
            tag[key] as NumberTag<Data>
        )).data else null
    },
    BiConsumer { data: Data, tag: CompoundTag -> tag.put(key, newTag(data)) }
) {
    private class TagReader<Data : Number?>(var tag: NumberTag<Data>) {
        val data: Data
            get() {
                val simpleName = tag.javaClass.simpleName
                val data: Number = tag.data
                //hack convert byteTag and shortTag because they data storage is all int type
                return if (simpleName == "ByteTag") {
                    data.toByte() as Data
                } else if (simpleName == "ShortTag") {
                    data.toShort() as Data
                } else if (data is Int) {
                    tag.data
                } else if (data is Long) {
                    tag.data
                } else if (data is Float) {
                    tag.data
                } else if (data is Double) {
                    tag.data
                } else {
                    throw IllegalArgumentException("Unknown number type: " + data.javaClass.name)
                }
            }
    }

    companion object {
        protected fun newTag(data: Number): NumberTag<*> {
            return if (data is Byte) {
                ByteTag(data.toByte().toInt())
            } else if (data is Short) {
                ShortTag(data.toShort().toInt())
            } else if (data is Int) {
                IntTag(data.toInt())
            } else if (data is Long) {
                LongTag(data.toLong())
            } else if (data is Float) {
                FloatTag(data.toFloat())
            } else if (data is Double) {
                DoubleTag(data.toDouble())
            } else {
                throw IllegalArgumentException("Unknown number type: " + data.javaClass.name)
            }
        }
    }
}
