package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class ServerboundDiagnosticsPacket : DataPacket() {
    var avgFps: Float = 0f
    var avgServerSimTickTimeMS: Float = 0f
    var avgClientSimTickTimeMS: Float = 0f
    var avgBeginFrameTimeMS: Float = 0f
    var avgInputTimeMS: Float = 0f
    var avgRenderTimeMS: Float = 0f
    var avgEndFrameTimeMS: Float = 0f
    var avgRemainderTimePercent: Float = 0f
    var avgUnaccountedTimePercent: Float = 0f

    override fun decode(byteBuf: HandleByteBuf) {
        this.avgFps = byteBuf.readFloatLE()
        this.avgServerSimTickTimeMS = byteBuf.readFloatLE()
        this.avgClientSimTickTimeMS = byteBuf.readFloatLE()
        this.avgBeginFrameTimeMS = byteBuf.readFloatLE()
        this.avgInputTimeMS = byteBuf.readFloatLE()
        this.avgRenderTimeMS = byteBuf.readFloatLE()
        this.avgEndFrameTimeMS = byteBuf.readFloatLE()
        this.avgRemainderTimePercent = byteBuf.readFloatLE()
        this.avgUnaccountedTimePercent = byteBuf.readFloatLE()
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SERVERBOUND_DIAGNOSTICS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
