package cn.nukkit.entity.data.property

import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.StringTag

/**
 * @author Peng_Lx
 */
class EnumEntityProperty(identifier: String, enums: Array<String>, defaultValue: String) :
    EntityProperty(identifier) {
    private val enums: Array<String>
    private val defaultValue: String

    init {
        var found: Boolean = false
        for (enumValue: String in enums) {
            if (enumValue == defaultValue) {
                found = true
                break
            }
        }
        require(found) { "Entity Property Error: " + identifier + "Default value not in enums." }

        this.enums = enums
        this.defaultValue = defaultValue
    }

    fun getEnums(): Array<String> {
        return enums
    }

    fun getDefaultValue(): String {
        return defaultValue
    }

    override fun populateTag(tag: CompoundTag) {
        tag.putInt("type", 3)
        val enumList: ListTag<StringTag> = ListTag()
        for (enumValue: String in getEnums()) {
            enumList.add(StringTag(enumValue))
        }
        tag.putList("enum", enumList)
    }

    fun findIndex(value: String): Int {
        for (i in enums.indices) {
            if (enums.get(i) == value) {
                return i
            }
        }
        return -1
    }
}
