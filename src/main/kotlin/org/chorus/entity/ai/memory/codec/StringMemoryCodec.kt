package org.chorus.entity.ai.memory.codec

import cn.nukkit.nbt.tag.CompoundTag
import java.util.function.BiConsumer
import java.util.function.Function


class StringMemoryCodec(key: String?) : MemoryCodec<String?>(
    Function { tag: CompoundTag -> if (tag.contains(key)) tag.getString(key) else null },
    BiConsumer { data: String?, tag: CompoundTag ->
        tag.putString(
            key,
            data!!
        )
    }
)
