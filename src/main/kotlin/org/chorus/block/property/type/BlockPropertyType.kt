package org.chorus.block.property.type

import org.chorus.block.property.type.IntPropertyType



/**
 * Allay Project 2023/3/19
 *
 * @author daoge_cmd
 */
interface BlockPropertyType<DATATYPE> {
    val name: String?
    val defaultValue: DATATYPE
    val validValues: List<DATATYPE>?
    val type: Type?

    fun createValue(value: DATATYPE): BlockPropertyValue<DATATYPE, out BlockPropertyType<DATATYPE>, *>

    fun tryCreateValue(value: Any?): BlockPropertyValue<DATATYPE, out BlockPropertyType<DATATYPE>, *>?

    val bitSize: Byte

    fun createDefaultValue(): BlockPropertyValue<DATATYPE, out BlockPropertyType<DATATYPE>, *> {
        return createValue(defaultValue)
    }

    enum class Type {
        BOOLEAN,
        INT,
        ENUM
    }

    abstract class BlockPropertyValue<DATATYPE, PROPERTY : BlockPropertyType<DATATYPE>, SERIALIZED_DATATYPE> internal constructor(
        val propertyType: PROPERTY, val value: DATATYPE
    ) {
        abstract val index: Int

        abstract val serializedValue: SERIALIZED_DATATYPE
    }

    companion object {
        fun getPropertyType(clazz: Class<*>): Type? {
            return when (clazz) {
                BooleanPropertyType::class.java -> Type.BOOLEAN
                IntPropertyType::class.java -> Type.INT
                EnumPropertyType::class.java -> Type.ENUM
                else -> null
            }
        }
    }
}
