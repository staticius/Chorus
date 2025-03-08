package cn.nukkit.network.protocol.types


@JvmRecord
data class PropertySyncData(val intProperties: IntArray, val floatProperties: FloatArray)
