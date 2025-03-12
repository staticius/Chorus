package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.camera.aimassist.ClientCameraAimAssistPacketAction


class ClientCameraAimAssistPacket : DataPacket() {
    private var cameraPresetId: String? = null
    private var action: ClientCameraAimAssistPacketAction? = null
    private var allowAimAssist = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.cameraPresetId = byteBuf.readString()
        this.action = ClientCameraAimAssistPacketAction.entries[byteBuf.readByte().toInt()]
        this.allowAimAssist = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(cameraPresetId!!)
        byteBuf.writeByte(action!!.ordinal())
        byteBuf.writeBoolean(allowAimAssist)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CLIENT_CAMERA_AIM_ASSIST_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
