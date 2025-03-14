package org.chorus.entity.ai.memory

import org.chorus.entity.*
import org.chorus.entity.ai.memory.codec.IMemoryCodec
import org.chorus.utils.*

import java.util.function.Supplier

/**
 * 实体记忆是一个存储实体数据的类，同时如果实现了[IMemoryCodec]，实体记忆还可以被持久化存储以及链接实体元数据
 *
 *
 * Entity memory is a class that stores entity data, and if [IMemoryCodec] is implemented, entity memory can also be persistently stored and linked entity metadata
 */
class MemoryType<Data> @JvmOverloads constructor(
    val identifier: Identifier,
    private val defaultData: Supplier<Data?> = Supplier { null }
) {


    var codec: IMemoryCodec<Data>? = null
        private set

    constructor(identifier: Identifier, defaultData: Data) : this(
        identifier,
        Supplier<Data?> { defaultData })

    constructor(identifier: String) : this(
        Identifier(identifier),
        Supplier<Data?> { null })

    constructor(identifier: String, defaultData: Data) : this(
        Identifier(identifier),
        Supplier<Data?> { defaultData })

    constructor(identifier: String, defaultData: Supplier<Data?>) : this(Identifier(identifier), defaultData)

    fun getDefaultData(): Data? {
        return defaultData.get()
    }

    fun withCodec(codec: IMemoryCodec<Data>?): MemoryType<Data> {
        this.codec = codec
        PERSISTENT_MEMORIES.add(this)
        return this
    }

    fun encode(entity: Entity, data: Data) {
        codec?.encode(data, entity.namedTag)
    }

    /**
     * 强制编码一个记忆
     *
     *
     * 会将给定的data值强转到Data类型
     *
     * @param entity 目标实体
     * @param data   数据
     */
    fun forceEncode(entity: Entity, data: Any) {
        codec?.encode(data as Data, entity.namedTag)
    }

    fun decode(entity: Entity): Data? {
        return if (codec != null) codec!!.decode(entity.namedTag) else null
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other is MemoryType<*>) {
            return identifier == other.identifier
        }
        return false
    }

    companion object {
        /**
         * 可持久化的记忆类型
         */
        private val PERSISTENT_MEMORIES: MutableSet<MemoryType<*>> = HashSet()

        val persistentMemories: Set<MemoryType<*>>
            get() = PERSISTENT_MEMORIES
    }
}
