package org.chorus.network.protocol

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.chorus.network.connection.util.HandleByteBuf


class UnlockedRecipesPacket : DataPacket() {
    var unlockedNotification: Boolean = false
    val unlockedRecipes: MutableList<String> = ObjectArrayList()

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBoolean(this.unlockedNotification)
        byteBuf.writeUnsignedVarInt(unlockedRecipes.size)
        for (recipe in this.unlockedRecipes) {
            byteBuf.writeString(recipe)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.UNLOCKED_RECIPES_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object : PacketDecoder<UnlockedRecipesPacket> {
        override fun decode(byteBuf: HandleByteBuf): UnlockedRecipesPacket {
            val packet = UnlockedRecipesPacket()

            packet.unlockedNotification = byteBuf.readBoolean()
            for (i in 0..<byteBuf.readUnsignedVarInt()) {
                packet.unlockedRecipes.add(byteBuf.readString())
            }

            return packet
        }
    }
}
