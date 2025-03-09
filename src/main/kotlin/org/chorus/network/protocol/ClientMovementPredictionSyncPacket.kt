package org.chorus.network.protocol

import org.chorus.entity.data.EntityFlag
import org.chorus.math.Vector3f
import org.chorus.network.connection.util.HandleByteBuf
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import lombok.*
import java.math.BigInteger






class ClientMovementPredictionSyncPacket : DataPacket() {
    private val flags: MutableSet<EntityFlag> = ObjectOpenHashSet()
    private var actorBoundingBox: Vector3f? = null
    private var movementAttributesComponent: MovementAttributesComponent? = null
    private var actorRuntimeId: Long = 0

    override fun decode(byteBuf: HandleByteBuf) {
        val flagsInt = byteBuf.readUnsignedBigVarInt(EntityFlag.entries.size)
        for (flag in EntityFlag.entries) {
            if (flagsInt.testBit(flag.ordinal())) {
                flags.add(flag)
            }
        }
        actorBoundingBox = byteBuf.readVector3f()
        readMovementAttributesComponent(byteBuf)
        actorRuntimeId = byteBuf.readEntityRuntimeId()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        var flagsInt = BigInteger.ZERO
        for (flag in flags) {
            flagsInt = flagsInt.setBit(flag.ordinal())
        }
        byteBuf.writeUnsignedBigVarInt(flagsInt)
        byteBuf.writeVector3f(actorBoundingBox!!)
        writeMovementAttributesComponent(byteBuf)
        byteBuf.writeEntityRuntimeId(actorRuntimeId)
    }

    fun writeMovementAttributesComponent(byteBuf: HandleByteBuf) {
        byteBuf.writeFloatLE(getMovementAttributesComponent().movementSpeed)
        byteBuf.writeFloatLE(getMovementAttributesComponent().underwaterMovementSpeed)
        byteBuf.writeFloatLE(getMovementAttributesComponent().lavaMovementSpeed)
        byteBuf.writeFloatLE(getMovementAttributesComponent().jumpStrength)
        byteBuf.writeFloatLE(getMovementAttributesComponent().health)
        byteBuf.writeFloatLE(getMovementAttributesComponent().hunger)
    }

    fun readMovementAttributesComponent(byteBuf: HandleByteBuf) {
        movementAttributesComponent = MovementAttributesComponent(
            byteBuf.readFloatLE(),
            byteBuf.readFloatLE(),
            byteBuf.readFloatLE(),
            byteBuf.readFloatLE(),
            byteBuf.readFloatLE(),
            byteBuf.readFloatLE()
        )
    }

    @JvmRecord
    data class MovementAttributesComponent(
        movementSpeed: Float,
        underwaterMovementSpeed: Float,
        lavaMovementSpeed: Float,
        jumpStrength: Float,
        health: Float,
        hunger: Float
    ) {
        val movementSpeed: Float = movementSpeed
        val underwaterMovementSpeed: Float = underwaterMovementSpeed
        val lavaMovementSpeed: Float = lavaMovementSpeed
        val jumpStrength: Float = jumpStrength
        val health: Float = health
        val hunger: Float = hunger
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CLIENT_MOVEMENT_PREDICTION_SYNC_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
