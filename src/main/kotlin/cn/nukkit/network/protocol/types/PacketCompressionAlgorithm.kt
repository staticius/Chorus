package cn.nukkit.network.protocol.types

enum class PacketCompressionAlgorithm : CompressionAlgorithm {
    ZLIB,
    SNAPPY,
    NONE
}