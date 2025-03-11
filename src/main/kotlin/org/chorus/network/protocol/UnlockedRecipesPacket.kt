package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import it.unimi.dsi.fastutil.objects.ObjectArrayList







class UnlockedRecipesPacket : DataPacket() {
    var unlockedNotification: Boolean = false
    val unlockedRecipes: MutableList<String> = ObjectArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
        this.unlockedNotification = byteBuf.readBoolean()
        for (i in 0..<byteBuf.readUnsignedVarInt()) {
            unlockedRecipes.add(byteBuf.readString())
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeBoolean(this.unlockedNotification)
        byteBuf.writeUnsignedVarInt(unlockedRecipes.size)
        for (recipe in this.unlockedRecipes) {
            byteBuf.writeString(recipe)
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.UNLOCKED_RECIPES_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
