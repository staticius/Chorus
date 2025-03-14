package org.chorus.block.property.type

interface BlockPropertyType<DATATYPE> {
    fun getName(): String
    fun getDefaultValue(): DATATYPE
    fun getValidValues(): List<DATATYPE>
    fun getType(): Type

    fun createValue(value: DATATYPE): BlockPropertyValue<DATATYPE, out BlockPropertyType<DATATYPE>, *>

    fun tryCreateValue(value: Any?): BlockPropertyValue<DATATYPE, out BlockPropertyType<DATATYPE>, *>?

    fun getBitSize(): Byte

    fun createDefaultValue(): BlockPropertyValue<DATATYPE, out BlockPropertyType<DATATYPE>, *> {
        return createValue(getDefaultValue())
    }

    enum class Type {
        BOOLEAN,
        INT,
        ENUM
    }

    abstract class BlockPropertyValue<DATATYPE, PROPERTY : BlockPropertyType<DATATYPE>, SERIALIZED_DATATYPE> internal constructor(
        val propertyType: PROPERTY, val value: DATATYPE
    ) {
        abstract fun getIndex(): Int

        abstract fun getSerializedValue(): SERIALIZED_DATATYPE
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
