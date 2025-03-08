package cn.nukkit.network.protocol

import cn.nukkit.lang.TranslationContainer
import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class DeathInfoPacket : DataPacket() {
    @JvmField
    var translation: TranslationContainer? = null

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(translation!!.text)
        byteBuf.writeArray(
            translation!!.parameters
        ) { str: String? -> byteBuf.writeString(str!!) }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.DEATH_INFO_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
