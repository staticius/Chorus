package cn.nukkit.entity.data.property

import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author Peng_Lx
 */
class FloatEntityProperty(
    identifier: String,
    private val defaultValue: Float,
    private val maxValue: Float,
    private val minValue: Float
) :
    EntityProperty(identifier) {
    fun getDefaultValue(): Float {
        return defaultValue
    }

    fun getMaxValue(): Float {
        return maxValue
    }

    fun getMinValue(): Float {
        return minValue
    }

    override fun populateTag(tag: CompoundTag) {
        tag.putInt("type", 1)
        tag.putFloat("max", getMaxValue())
        tag.putFloat("min", getMinValue())
    }
}