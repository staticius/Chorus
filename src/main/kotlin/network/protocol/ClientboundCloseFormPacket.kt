package org.chorus_oss.chorus.network.protocol

class ClientboundCloseFormPacket : DataPacket() {
    override fun pid(): Int {
        return ProtocolInfo.CLIENTBOUND_CLOSE_FORM_PACKET
    }
}
