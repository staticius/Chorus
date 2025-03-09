package org.chorus.utils.collection


interface ByteArrayWrapper {
    var rawBytes: ByteArray

    @ShouldThaw
    fun getByte(index: Int): Byte

    @ShouldThaw
    fun setByte(index: Int, b: Byte)
}
