package org.chorus.network.protocol.types

class EntityLink(
    var fromEntityUniqueId: Long,
    var toEntityUniqueId: Long,
    type: Type,
    var immediate: Boolean,
    var riderInitiated: Boolean,
    var vehicleAngularVelocity: Float
) {
    var type: Byte = type.ordinal.toByte()

    @Deprecated("")
    constructor(
        fromEntityUniqueId: Long,
        toEntityUniqueId: Long,
        type: Type,
        immediate: Boolean,
        riderInitiated: Boolean
    ) : this(fromEntityUniqueId, toEntityUniqueId, type, immediate, riderInitiated, 0f)

    enum class Type {
        REMOVE,
        RIDER,
        PASSENGER
    }

    companion object {
        val EMPTY_ARRAY: Array<EntityLink?> = arrayOfNulls(0)
    }
}
