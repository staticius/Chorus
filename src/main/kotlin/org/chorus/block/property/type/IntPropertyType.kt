package org.chorus.block.property.type

import cn.nukkit.block.property.type.BlockPropertyType.BlockPropertyValue
import cn.nukkit.utils.Utils.computeRequiredBits
import lombok.Getter
import java.util.stream.IntStream

/**
 * Allay Project 2023/3/19
 *
 * @author daoge_cmd
 */
class IntPropertyType private constructor(
    name: String,
    @field:Getter private val min: Int,
    @field:Getter private val max: Int,
    defaultData: Int
) :
    BaseBlockPropertyType<Int?>(
        name, IntStream.range(min, max + 1).boxed().toList(), defaultData, computeRequiredBits(
            min,
            max
        )
    ) {
    private val cachedValues: Array<IntPropertyValue>

    init {
        cachedValues = arrayOfNulls(max - min + 1)
        for (i in min..max) {
            val value = IntPropertyValue(i)
            cachedValues[i] = value
        }
    }

    override fun getType(): BlockPropertyType.Type {
        return BlockPropertyType.Type.INT
    }

    override fun createValue(value: Int): IntPropertyValue {
        return cachedValues[value - min]
    }

    override fun tryCreateValue(value: Any): IntPropertyValue {
        if (value is Number) {
            return cachedValues[value.toInt() - min]
        } else throw IllegalArgumentException("Invalid value for int property type: $value")
    }

    inner class IntPropertyValue internal constructor(value: Int?) :
        BlockPropertyValue<Int?, IntPropertyType?, Int?>(this@IntPropertyType, value) {
        override fun getIndex(): Int {
            return value!! - min
        }

        override fun getSerializedValue(): Int {
            return value!!
        }

        override fun toString(): String {
            return "IntPropertyValue(name=$name, value=$value)"
        }
    }

    companion object {
        fun of(name: String, min: Int, max: Int, defaultData: Int): IntPropertyType {
            return IntPropertyType(name, min, max, defaultData)
        }
    }
}
