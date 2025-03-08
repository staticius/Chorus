package cn.nukkit.entity.ai.memory.codec

import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.nbt.tag.CompoundTag
import lombok.Getter
import java.util.function.BiConsumer
import java.util.function.Function

@Getter
open class MemoryCodec<Data>(
    decoder: Function<CompoundTag, Data?>,
    encoder: BiConsumer<Data, CompoundTag>
) : IMemoryCodec<Data> {
    override val decoder: Function<CompoundTag, Data>
    override val encoder: BiConsumer<Data, CompoundTag>
    private var onInit: BiConsumer<Data?, EntityMob>? = null

    init {
        this.decoder = decoder
        this.encoder = encoder
    }

    /**
     * BiConsumer<Data></Data>, EntityMob> Data can be Null
     */
    fun onInit(onInit: BiConsumer<Data, EntityMob>?): MemoryCodec<Data> {
        this.onInit = onInit
        return this
    }

    override fun init(data: Data?, entity: EntityMob) {
        if (onInit != null) {
            onInit!!.accept(data, entity)
        }
    }
}
