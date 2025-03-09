package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class CommandBlockUpdatePacket : DataPacket() {
    var isBlock: Boolean = false
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0
    var commandBlockMode: Int = 0
    var isRedstoneMode: Boolean = false
    var isConditional: Boolean = false
    var minecartEid: Long = 0
    var command: String? = null
    var lastOutput: String? = null
    var name: String? = null
    private var filteredName: String? = null
    var shouldTrackOutput: Boolean = false
    var tickDelay: Int = 0
    var executingOnFirstTick: Boolean = false

    override fun decode(byteBuf: HandleByteBuf) {
        this.isBlock = byteBuf.readBoolean()
        if (this.isBlock) {
            val v = byteBuf.readBlockVector3()
            this.x = v.x
            this.y = v.y
            this.z = v.z
            this.commandBlockMode = byteBuf.readUnsignedVarInt()
            this.isRedstoneMode = byteBuf.readBoolean()
            this.isConditional = byteBuf.readBoolean()
        } else {
            this.minecartEid = byteBuf.readEntityRuntimeId()
        }
        this.command = byteBuf.readString()
        this.lastOutput = byteBuf.readString()
        this.name = byteBuf.readString()
        this.filteredName = byteBuf.readString()
        this.shouldTrackOutput = byteBuf.readBoolean()
        this.tickDelay = byteBuf.readIntLE()
        this.executingOnFirstTick = byteBuf.readBoolean()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBoolean(this.isBlock)
        if (this.isBlock) {
            byteBuf.writeBlockVector3(this.x, this.y, this.z)
            byteBuf.writeUnsignedVarInt(this.commandBlockMode)
            byteBuf.writeBoolean(this.isRedstoneMode)
            byteBuf.writeBoolean(this.isConditional)
        } else {
            byteBuf.writeEntityRuntimeId(this.minecartEid)
        }
        byteBuf.writeString(command!!)
        byteBuf.writeString(lastOutput!!)
        byteBuf.writeString(name!!)
        byteBuf.writeString(filteredName!!)
        byteBuf.writeBoolean(this.shouldTrackOutput)
        byteBuf.writeIntLE(this.tickDelay)
        byteBuf.writeBoolean(this.executingOnFirstTick)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.COMMAND_BLOCK_UPDATE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
