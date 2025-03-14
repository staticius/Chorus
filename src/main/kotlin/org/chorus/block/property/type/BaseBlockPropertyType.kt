package org.chorus.block.property.type

abstract class BaseBlockPropertyType<DATATYPE> protected constructor(
    val name: String,
    val validValues: List<DATATYPE>,
    val defaultValue: DATATYPE,
    val bitSize: Byte
) :
    BlockPropertyType<DATATYPE> {

    override fun getName(): String {
        return name
    }

    override fun getDefaultValue(): DATATYPE {
        return defaultValue
    }

    override fun getValidValues(): List<DATATYPE> {
        return validValues
    }

    override fun getBitSize(): Byte {
        return bitSize
    }
}
