package org.chorus.block.property.type

import java.util.*

/**
 * Allay Project 2023/3/19
 *
 * @author daoge_cmd
 */
abstract class BaseBlockPropertyType<DATATYPE> protected constructor(
    name: String,
    validValues: List<DATATYPE>,
    defaultValue: DATATYPE,
    bitSize: Byte
) :
    BlockPropertyType<DATATYPE> {
    @JvmField
    protected val name: String
    @JvmField
    protected val validValues: List<DATATYPE>
    protected val defaultValue: DATATYPE
    protected val bitSize: Byte

    init {
        Objects.requireNonNull(defaultValue)
        this.name = name
        this.validValues = validValues
        this.defaultValue = defaultValue
        this.bitSize = bitSize
    }

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
