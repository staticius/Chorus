package org.chorus.network.protocol

import cn.nukkit.nbt.tag.ListTag.get
import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.inventory.InventoryLayout
import cn.nukkit.network.protocol.types.inventory.InventoryTabLeft
import cn.nukkit.network.protocol.types.inventory.InventoryTabRight
import lombok.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class SetPlayerInventoryOptionsPacket : DataPacket() {
    var leftTab: InventoryTabLeft? = null
    var rightTab: InventoryTabRight? = null
    var filtering: Boolean = false
    var layout: InventoryLayout? = null
    var craftingLayout: InventoryLayout? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.leftTab = InventoryTabLeft.Companion.VALUES.get(byteBuf.readVarInt())
        this.rightTab = InventoryTabRight.Companion.VALUES.get(byteBuf.readVarInt())
        this.filtering = byteBuf.readBoolean()
        this.layout = InventoryLayout.Companion.VALUES.get(byteBuf.readVarInt())
        this.craftingLayout = InventoryLayout.Companion.VALUES.get(byteBuf.readVarInt())
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(leftTab!!.ordinal())
        byteBuf.writeVarInt(rightTab!!.ordinal())
        byteBuf.writeBoolean(this.filtering)
        byteBuf.writeVarInt(layout!!.ordinal())
        byteBuf.writeVarInt(craftingLayout!!.ordinal())
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_PLAYER_INVENTORY_OPTIONS_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
