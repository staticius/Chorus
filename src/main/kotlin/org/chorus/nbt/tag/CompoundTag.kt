package org.chorus.nbt.tag

import io.netty.util.internal.EmptyArrays
import org.jetbrains.annotations.UnmodifiableView
import java.util.*
import kotlin.collections.Collection
import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach
import kotlin.collections.iterator
import kotlin.collections.set
import kotlin.math.max

open class CompoundTag @JvmOverloads constructor(protected val tags: MutableMap<String?, Tag<*>?>? = HashMap()) :
    Tag<MutableMap<String?, Tag<*>?>>() {
    open val allTags: Collection<Tag<*>?>
        get() = tags!!.values

    override val id: Byte
        get() = Tag.Companion.TAG_COMPOUND

    open fun put(name: String?, tag: Tag<*>?): CompoundTag {
        tags!![name] = tag
        return this
    }

    fun putIfNull(name: String?, tag: Tag<*>?): CompoundTag {
        if (!tags!!.containsKey(name)) {
            tags[name] = tag
        }
        return this
    }

    fun putIfNull(other: CompoundTag): CompoundTag {
        for ((key, value) in other.tags!!) {
            this.putIfNull(key, value)
        }
        return this
    }

    open fun putByte(name: String?, value: Int): CompoundTag {
        tags!![name] = ByteTag(value)
        return this
    }

    open fun putShort(name: String?, value: Int): CompoundTag {
        tags!![name] = ShortTag(value)
        return this
    }

    open fun putInt(name: String?, value: Int): CompoundTag {
        tags!![name] = IntTag(value)
        return this
    }

    open fun putLong(name: String?, value: Long): CompoundTag {
        tags!![name] = LongTag(value)
        return this
    }

    open fun putFloat(name: String?, value: Float): CompoundTag {
        tags!![name] = FloatTag(value)
        return this
    }

    open fun putDouble(name: String?, value: Double): CompoundTag {
        tags!![name] = DoubleTag(value)
        return this
    }

    open fun putString(name: String?, value: String): CompoundTag {
        tags!![name] = StringTag(value)
        return this
    }

    open fun putByteArray(name: String?, value: ByteArray?): CompoundTag {
        tags!![name] = ByteArrayTag(value)
        return this
    }

    open fun putIntArray(name: String?, value: IntArray?): CompoundTag {
        tags!![name] = IntArrayTag(value)
        return this
    }

    open fun putList(name: String?, listTag: ListTag<out Tag<*>>?): CompoundTag {
        tags!![name] = listTag
        return this
    }

    open fun putCompound(name: String?, value: CompoundTag?): CompoundTag {
        tags!![name] = value
        return this
    }

    open fun putBoolean(string: String?, `val`: Boolean): CompoundTag {
        putByte(string, if (`val`) 1 else 0)
        return this
    }

    open fun get(name: String?): Tag<*>? {
        return tags!![name]
    }

    open fun contains(name: String?): Boolean {
        return tags!!.containsKey(name)
    }

    open fun containsCompound(name: String?): Boolean {
        return tags!![name] is CompoundTag
    }

    open fun containsString(name: String?): Boolean {
        return tags!![name] is StringTag
    }

    open fun containsIntArray(name: String?): Boolean {
        return tags!![name] is IntArrayTag
    }

    open fun containsByteArray(name: String?): Boolean {
        return tags!![name] is ByteArrayTag
    }

    open fun containsNumber(name: String?): Boolean {
        return tags!![name] is NumberTag<*>
    }

    open fun containsList(name: String?): Boolean {
        return tags!![name] is ListTag<*>
    }

    open fun containsList(name: String?, type: Byte): Boolean {
        val tag = tags!![name] as? ListTag<*> ?: return false
        val listType = tag.type
        return listType.toInt() == 0 || listType == type
    }

    open fun containsByte(name: String?): Boolean {
        return tags!![name] is ByteTag
    }

    open fun containsShort(name: String?): Boolean {
        return tags!![name] is ShortTag
    }

    open fun containsInt(name: String?): Boolean {
        return tags!![name] is IntTag
    }

    open fun containsDouble(name: String?): Boolean {
        return tags!![name] is DoubleTag
    }

    open fun containsFloat(name: String?): Boolean {
        return tags!![name] is FloatTag
    }

    open fun remove(name: String?): CompoundTag? {
        tags!!.remove(name)
        return this
    }

    open fun <T : Tag<*>?> removeAndGet(name: String?): T? {
        return tags!!.remove(name) as T?
    }

    open fun getByte(name: String?): Byte {
        if (!tags!!.containsKey(name)) return 0.toByte()
        return (tags[name] as NumberTag<*>).data!!.toByte()
    }

    open fun getShort(name: String?): Short {
        if (!tags!!.containsKey(name)) return 0
        return (tags[name] as NumberTag<*>).data!!.toShort()
    }

    open fun getInt(name: String?): Int {
        if (!tags!!.containsKey(name)) return 0
        return (tags[name] as NumberTag<*>).data!!.toInt()
    }

    open fun getLong(name: String?): Long {
        if (!tags!!.containsKey(name)) return 0
        return (tags[name] as NumberTag<*>).data!!.toLong()
    }

    open fun getFloat(name: String?): Float {
        if (!tags!!.containsKey(name)) return 0f
        return (tags[name] as NumberTag<*>).data!!.toFloat()
    }

    open fun getDouble(name: String?): Double {
        if (!tags!!.containsKey(name)) return 0.0
        return (tags[name] as NumberTag<*>).data!!.toDouble()
    }

    open fun getString(name: String?): String? {
        if (!tags!!.containsKey(name)) return ""
        val tag = tags[name]
        if (tag is NumberTag<*>) {
            return tag.data.toString()
        }
        return (tag as StringTag).data
    }

    open fun getByteArray(name: String?): ByteArray? {
        if (!tags!!.containsKey(name)) return EmptyArrays.EMPTY_BYTES
        return (tags[name] as ByteArrayTag).data
    }

    open fun getIntArray(name: String?): IntArray? {
        if (!tags!!.containsKey(name)) return EmptyArrays.EMPTY_INTS
        return (tags[name] as IntArrayTag).data
    }

    open fun getCompound(name: String?): CompoundTag? {
        if (!tags!!.containsKey(name)) return CompoundTag()
        return tags[name] as CompoundTag?
    }

    open fun getList(name: String?): ListTag<out Tag<*>>? {
        if (!tags!!.containsKey(name)) return ListTag()
        return tags[name] as ListTag<out Tag<*>>?
    }

    open fun <T : Tag<*>> getList(name: String?, type: Class<T>?): ListTag<T>? {
        if (tags!!.containsKey(name)) {
            return tags[name] as ListTag<T>?
        }
        return ListTag()
    }

    open fun getTags(): MutableMap<String?, Tag<*>?> {
        return HashMap(this.tags)
    }

    val entrySet: @UnmodifiableView MutableSet<Map.Entry<String?, Tag<*>?>>
        get() = Collections.unmodifiableSet(
            tags!!.entries
        )

    override fun parseValue(): MutableMap<String?, Tag<*>?> {
        val value: MutableMap<String?, Tag<*>?> = HashMap(
            tags!!.size
        )

        for ((key, value1) in tags) {
            value[key] = value1!!.parseValue() as Tag<*>
        }

        return value
    }

    open fun getBoolean(name: String?): Boolean {
        return getByte(name).toInt() != 0
    }

    override fun toString(): String {
        val joiner = StringJoiner(",\n\t")
        tags!!.forEach { (key, tag) ->
            joiner.add(
                '\''.toString() + key + "' : " + tag.toString().replace("\n", "\n\t")
            )
        }
        return "CompoundTag '" + "' (" + tags.size + " entries) {\n\t" + joiner + "\n}"
    }

    override fun toSNBT(): String {
        val joiner = StringJoiner(",")
        tags!!.forEach { (key, tag) -> joiner.add("\"" + key + "\":" + tag!!.toSNBT()) }
        return "{$joiner}"
    }

    override fun toSNBT(space: Int): String {
        val addSpace = StringBuilder()
        addSpace.append(" ".repeat(max(0.0, space.toDouble()).toInt()))
        val joiner = StringJoiner(",\n$addSpace")
        tags!!.forEach { (key, tag) ->
            joiner.add(
                "\"$key\": " + tag!!.toSNBT(space).replace(
                    "\n",
                    """
                    
                    $addSpace
                    """.trimIndent()
                )
            )
        }
        return "{\n$addSpace$joiner\n}"
    }

    open val isEmpty: Boolean
        get() = tags!!.isEmpty()

    override fun copy(): CompoundTag {
        val tag = CompoundTag()
        for (key in tags!!.keys) {
            tag.put(key, tags[key]!!.copy())
        }
        return tag
    }

    override fun equals(other: Any?): Boolean {
        if (super.equals(other)) {
            val o = other as CompoundTag
            return tags!!.entries == o.tags!!.entries
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), tags)
    }

    /**
     * Check existence of NBT tag
     *
     * @param name - NBT tag Id.
     * @return - true, if tag exists
     */
    open fun exist(name: String?): Boolean {
        return tags!!.containsKey(name)
    }
}
