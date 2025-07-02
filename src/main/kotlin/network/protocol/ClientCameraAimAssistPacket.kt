package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf
import org.chorus_oss.chorus.network.protocol.types.camera.aimassist.ClientCameraAimAssistPacketAction

data class ClientCameraAimAssistPacket(
    val cameraPresetID: String,
    val action: ClientCameraAimAssistPacketAction,
    val allowAimAssist: Boolean,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(this.cameraPresetID)
        byteBuf.writeByte(this.action.ordinal)
        byteBuf.writeBoolean(this.allowAimAssist)
    }

    override fun pid(): Int {
        return ProtocolInfo.CLIENT_CAMERA_AIM_ASSIST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<ClientCameraAimAssistPacket> {
        override fun decode(byteBuf: HandleByteBuf): ClientCameraAimAssistPacket {
            return ClientCameraAimAssistPacket(
                cameraPresetID = byteBuf.readString(),
                action = ClientCameraAimAssistPacketAction.entries[byteBuf.readByte().toInt()],
                allowAimAssist = byteBuf.readBoolean(),
            )
        }
    }
}
