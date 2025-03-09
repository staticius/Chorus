package org.chorus.entity.ai.memory.codec

import org.chorus.nbt.tag.CompoundTag
import java.util.function.BiConsumer
import java.util.function.Function


class BooleanMemoryCodec(key: String?) : MemoryCodec<Boolean?>(
    Function { tag: CompoundTag -> if (tag.contains(key)) tag.getBoolean(key) else null },
    BiConsumer { data: Boolean?, tag: CompoundTag ->
        tag.putBoolean(
            key,
            data!!
        )
    }
)
