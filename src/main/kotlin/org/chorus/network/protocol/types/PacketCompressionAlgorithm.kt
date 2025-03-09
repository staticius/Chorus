package org.chorus.network.protocol.types

enum class PacketCompressionAlgorithm : CompressionAlgorithm {
    ZLIB,
    SNAPPY,
    NONE
}