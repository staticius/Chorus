package org.chorus_oss.chorus.network.protocol

import org.chorus_oss.chorus.network.ProtocolInfo
import org.chorus_oss.chorus.network.connection.util.HandleByteBuf

class NPCDialoguePacket : DataPacket() {
    @JvmField
    var runtimeEntityId: Long = 0

    @JvmField
    var action: NPCDialogAction = NPCDialogAction.OPEN

    @JvmField
    var dialogue: String = "" //content

    @JvmField
    var sceneName: String = ""

    @JvmField
    var npcName: String = ""

    @JvmField
    var actionJson: String = ""

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeLongLE(runtimeEntityId)
        byteBuf.writeVarInt(action.ordinal)
        byteBuf.writeString(dialogue)
        byteBuf.writeString(sceneName)
        byteBuf.writeString(npcName)
        byteBuf.writeString(actionJson)
    }


    enum class NPCDialogAction {
        OPEN,
        CLOSE
    }

    override fun pid(): Int {
        return ProtocolInfo.NPC_DIALOGUE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<NPCDialoguePacket> {
        override fun decode(byteBuf: HandleByteBuf): NPCDialoguePacket {
            val packet = NPCDialoguePacket()

            packet.runtimeEntityId = byteBuf.readLongLE()
            packet.action = ACTIONS[byteBuf.readVarInt()]
            packet.dialogue = byteBuf.readString()
            packet.sceneName = byteBuf.readString()
            packet.npcName = byteBuf.readString()
            packet.actionJson = byteBuf.readString()

            return packet
        }

        private val ACTIONS = NPCDialogAction.entries.toTypedArray()
    }
}
