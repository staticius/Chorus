package org.chorus.block.property.type

abstract class BaseBlockPropertyType<DATATYPE> protected constructor(
    override val name: String,
    override val validValues: List<DATATYPE>,
    override val defaultValue: DATATYPE,
    val bitSize: Byte
) :
    BlockPropertyType<DATATYPE> {

    override fun getBitSize(): Byte {
        return bitSize
    }
}
