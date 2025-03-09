package org.chorus.block.property.type

import org.chorus.block.property.type.IntPropertyType
import lombok.Getter
import lombok.ToString

/**
 * Allay Project 2023/3/19
 *
 * @author daoge_cmd
 */
interface BlockPropertyType<DATATYPE> {
    @JvmField
    val name: String?

    @JvmField
    val defaultValue: DATATYPE

    @JvmField
    val validValues: List<DATATYPE>?

    @JvmField
    val type: Type?

    fun createValue(value: DATATYPE): BlockPropertyValue<DATATYPE, out BlockPropertyType<DATATYPE>?, *>

    fun tryCreateValue(value: Any?): BlockPropertyValue<DATATYPE, out BlockPropertyType<DATATYPE>?, *>?

    @JvmField
    val bitSize: Byte

    fun createDefaultValue(): BlockPropertyValue<DATATYPE, out BlockPropertyType<DATATYPE>?, *> {
        return createValue(defaultValue)
    }

    @Getter
    enum class Type {
        BOOLEAN,
        INT,
        ENUM
    }

    @Getter
    @ToString
    abstract class BlockPropertyValue<DATATYPE, PROPERTY : BlockPropertyType<DATATYPE>?, SERIALIZED_DATATYPE> internal constructor(
        protected val propertyType: PROPERTY, @JvmField protected val value: DATATYPE
    ) {
        abstract val index: Int

        abstract val serializedValue: SERIALIZED_DATATYPE
    }

    companion object {
        fun getPropertyType(clazz: Class<*>): Type? {
            return if (clazz == BooleanPropertyType::class.java) Type.BOOLEAN
            else if (clazz == IntPropertyType::class.java) Type.INT
            else if (clazz == EnumPropertyType::class.java) Type.ENUM
            else null
        }
    }
}
