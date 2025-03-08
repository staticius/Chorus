package cn.nukkit.utils.collection

class PureByteArray : ByteArrayWrapper {
    override var rawBytes: ByteArray

    internal constructor(src: ByteArray) {
        this.rawBytes = src
    }

    internal constructor(length: Int) {
        this.rawBytes = ByteArray(length)
    }

    override fun getByte(index: Int): Byte {
        return rawBytes[index]
    }

    override fun setByte(index: Int, b: Byte) {
        rawBytes[index] = b
    }
}
