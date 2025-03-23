package org.chorus.entity.ai.memory.codec

import org.chorus.nbt.tag.CompoundTag
import java.util.function.BiConsumer
import java.util.function.Function


class StringMemoryCodec(key: String) : MemoryCodec<String>(
    Function { tag ->
        if (tag.contains(key))
            tag.getString(key)
        else null
    },
    BiConsumer { data, tag ->
        tag.putString(key, data)
    }
)
