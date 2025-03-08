package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.TrimMaterial
import cn.nukkit.network.protocol.types.TrimPattern
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import lombok.*
import java.util.function.Consumer

@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
@Builder
@Getter
@Setter
@ToString(doNotUseGetters = true)
@AllArgsConstructor
class TrimDataPacket : DataPacket() {
    val patterns: MutableList<TrimPattern> = ObjectArrayList()
    val materials: MutableList<TrimMaterial> = ObjectArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
        val length1 = byteBuf.readUnsignedVarInt()
        for (i in 0..<length1) {
            patterns.add(TrimPattern(byteBuf.readString(), byteBuf.readString()))
        }
        val length2 = byteBuf.readUnsignedVarInt()
        for (i in 0..<length2) {
            materials.add(TrimMaterial(byteBuf.readString(), byteBuf.readString(), byteBuf.readString()))
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeUnsignedVarInt(patterns.size)
        patterns.forEach(Consumer { p: TrimPattern ->
            byteBuf.writeString(p.itemName)
            byteBuf.writeString(p.patternId)
        })
        byteBuf.writeUnsignedVarInt(materials.size)
        materials.forEach(Consumer { m: TrimMaterial ->
            byteBuf.writeString(m.materialId)
            byteBuf.writeString(m.color)
            byteBuf.writeString(m.itemName)
        })
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.TRIM_DATA
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
