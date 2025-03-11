package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.AbilityType
import org.chorus.network.protocol.types.PlayerAbility







class RequestAbilityPacket : DataPacket() {
    var ability: PlayerAbility? = null
    var type: AbilityType? = null
    var boolValue: Boolean = false
    var floatValue: Float = 0f

    override fun decode(byteBuf: HandleByteBuf) {
        this.ability = ABILITIES[byteBuf.readVarInt()]
        this.type = ABILITY_TYPES[byteBuf.readByte().toInt()]
        this.boolValue = byteBuf.readBoolean()
        this.floatValue = byteBuf.readFloatLE()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        throw UnsupportedOperationException()
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.REQUEST_ABILITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        val ABILITIES: Array<PlayerAbility> = UpdateAbilitiesPacket.Companion.VALID_FLAGS
        val ABILITY_TYPES: Array<AbilityType> = AbilityType.entries.toTypedArray()
    }
}
