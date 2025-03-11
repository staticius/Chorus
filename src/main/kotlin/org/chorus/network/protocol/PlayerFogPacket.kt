package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.utils.*







class PlayerFogPacket : DataPacket() {
    //Fog stack containing fog effects from the /fog command
    @JvmField
    var fogStack: List<Fog> = ArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeArray(
            fogStack
        ) { fog: Fog -> byteBuf.writeString(fog.identifier.toString()) }
    }

    /**
     * @param identifier The namespace id of this fog
     * @param userProvidedId User-specified feature id
     */
    @JvmRecord
    data class Fog(identifier: Identifier, userProvidedId: String) {
        val identifier: Identifier = identifier
        val userProvidedId: String = userProvidedId
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAYER_FOG_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
