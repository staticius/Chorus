package org.chorus.entity.data

import com.google.common.base.Preconditions
import java.util.*

class EntityDataMap : MutableMap<EntityDataType<*>?, Any?> {
    private val map: MutableMap<EntityDataType<*>, Any> = LinkedHashMap()

    fun getOrCreateFlags(): EnumSet<EntityFlag?> {
        var flags: EnumSet<EntityFlag?> = get<EnumSet<EntityFlag?>>(EntityDataTypes.Companion.FLAGS)
        if (flags == null) {
            flags = get<EnumSet<EntityFlag?>>(EntityDataTypes.Companion.FLAGS_2)
            if (flags == null) {
                flags = EnumSet.noneOf(EntityFlag::class.java)
            }
            this.putFlags(flags)
        }
        return flags
    }

    fun getFlags(): EnumSet<EntityFlag> {
        return get<EnumSet<EntityFlag>>(EntityDataTypes.Companion.FLAGS)
    }

    fun setFlag(flag: EntityFlag, value: Boolean): EntityFlag {
        Objects.requireNonNull(flag, "flag")
        val flags: EnumSet<EntityFlag?> = this.getOrCreateFlags()
        if (value) {
            flags.add(flag)
        } else {
            flags.remove(flag)
        }

        return flag
    }

    fun existFlag(flag: EntityFlag): Boolean {
        Objects.requireNonNull(flag, "flag")
        val flags: EnumSet<EntityFlag?> = this.getOrCreateFlags()
        return flags.contains(flag)
    }

    fun putFlags(flags: EnumSet<EntityFlag?>): EnumSet<EntityFlag?> {
        Objects.requireNonNull(flags, "flags")
        map.put(EntityDataTypes.Companion.FLAGS, flags)
        map.put(EntityDataTypes.Companion.FLAGS_2, flags)
        return flags
    }

    fun <T> get(type: EntityDataType<T>): T {
        return map.getOrDefault(type, type.getDefaultValue()) as T
    }

    fun <T> getOrDefault(type: EntityDataType<T>, defaultValue: T): T {
        Objects.requireNonNull(type, "type")
        val `object`: Any = map.getOrDefault(type, defaultValue)
        try {
            return `object` as T
        } catch (e: ClassCastException) {
            return defaultValue
        }
    }

    fun <T> putType(type: EntityDataType<T>, value: T) {
        this.put(type, value)
    }

    override fun size(): Int {
        return map.size
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun containsKey(key: Any): Boolean {
        return map.containsKey(key)
    }

    override fun containsValue(value: Any): Boolean {
        return map.containsValue(value)
    }

    override fun get(key: Any): Any? {
        return map.get(key)
    }

    override fun put(key: EntityDataType<*>, value: Any): Any? {
        var value: Any = value
        Preconditions.checkNotNull(key, "type")
        Preconditions.checkNotNull(value, "value was null for %s", key)
        if (key === EntityDataTypes.Companion.FLAGS || key === EntityDataTypes.Companion.FLAGS_2) {
            return this.putFlags(value as EnumSet<EntityFlag?>)
        }
        if (Number::class.java.isAssignableFrom(value.javaClass)) {
            val type: Class<*>? = key.getType()
            val number: Number = value as Number
            if (type == Long::class.javaPrimitiveType || type == Long::class.java) {
                value = number.toLong()
            } else if (type == Int::class.javaPrimitiveType || type == Int::class.java) {
                value = number.toInt()
            } else if (type == Short::class.javaPrimitiveType || type == Short::class.java) {
                value = number.toShort()
            } else if (type == Byte::class.javaPrimitiveType || type == Byte::class.java) {
                value = number.toByte()
            } else if (type == Float::class.javaPrimitiveType || type == Float::class.java) {
                value = number.toFloat()
            } else if (type == Double::class.javaPrimitiveType || type == Double::class.java) {
                value = number.toDouble()
            }
        }
        return map.put(key, value)
    }

    override fun remove(key: Any): Any? {
        return map.remove(key)
    }

    override fun putAll(map: Map<out EntityDataType<*>, *>) {
        Preconditions.checkNotNull(map, "map")
        this.map.putAll(map)
    }

    override fun clear() {
        map.clear()
    }

    override fun keySet(): Set<EntityDataType<*>> {
        return map.keys
    }

    override fun values(): Collection<Any> {
        return map.values
    }

    override fun entrySet(): Set<Map.Entry<EntityDataType<*>, Any>> {
        return map.entries
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that: EntityDataMap = o as EntityDataMap
        return this.map == that.map
    }

    override fun hashCode(): Int {
        return map.hashCode()
    }

    fun copy(vararg entityDataTypes: EntityDataType<*>): EntityDataMap {
        val entityDataMap: EntityDataMap = EntityDataMap()
        for (t: EntityDataType<*> in entityDataTypes) {
            val o: Any? = this.get(t)
            if (o != null) {
                entityDataMap.put(t, o)
            }
        }
        return entityDataMap
    }

    override fun toString(): String {
        val i: Iterator<Map.Entry<EntityDataType<*>, Any>> = map.entries.iterator()
        if (!i.hasNext()) return "{}"

        val sb: StringBuilder = StringBuilder()
        sb.append('{')
        while (i.hasNext()) {
            val e: Map.Entry<EntityDataType<*>, Any> = i.next()
            val key: EntityDataType<*> = e.key
            if (key === EntityDataTypes.Companion.FLAGS_2) continue  // We don't want this to be visible.

            val stringVal: String = e.value.toString()
            sb.append(key.toString()).append('=').append(stringVal)
            if (!i.hasNext()) return sb.append('}').toString()
            sb.append(',').append(' ')
        }
        return sb.toString()
    }
}
