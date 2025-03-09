package org.chorus.entity.ai.memory

import org.chorus.entity.mob.EntityMob
import lombok.Getter
import java.util.concurrent.ConcurrentHashMap

/**
 * 记忆存储器标准实现
 */
class MemoryStorage(@field:Getter override var entity: EntityMob) : IMemoryStorage {
    protected var memoryMap: MutableMap<MemoryType<*>, Any> = ConcurrentHashMap()

    override fun <D> put(type: MemoryType<D>, data: D?) {
        val codec = type.codec
        codec?.init(data, entity)
        memoryMap[type] = data ?: EMPTY_VALUE
    }

    override fun <D> get(type: MemoryType<D>): D? {
        if (!memoryMap.containsKey(type)) {
            var data = type.decode(getEntity())
            if (data == null) data = type.defaultData
            put(type, data)
        }
        val value: D
        return if (((memoryMap[type] as D?).also { value = it }) !== EMPTY_VALUE) value else null
    }

    override val all: Map<MemoryType<*>, *>
        get() {
            val hashMap =
                HashMap<MemoryType<*>, Any>()
            memoryMap.forEach { (k: MemoryType<*>?, v: Any?) ->
                if (v !== EMPTY_VALUE) hashMap[k] = v
            }
            return hashMap
        }

    override fun clear(type: MemoryType<*>) {
        memoryMap.remove(type)
    }

    companion object {
        //表示一个空值(null)，这样做是因为在ConcurrentHashMap中不允许放入null值
        val EMPTY_VALUE: Any = Any()
    }
}
