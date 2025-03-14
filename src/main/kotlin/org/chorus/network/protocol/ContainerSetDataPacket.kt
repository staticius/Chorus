package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class ContainerSetDataPacket : DataPacket() {
    var windowId: Int = 0
    var property: Int = 0
    var value: Int = 0

    override fun decode(byteBuf: HandleByteBuf) {
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeByte(windowId.toByte().toInt())
        byteBuf.writeVarInt(this.property)
        byteBuf.writeVarInt(this.value)
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.CONTAINER_SET_DATA_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        const val PROPERTY_FURNACE_TICK_COUNT: Int = 0
        const val PROPERTY_FURNACE_LIT_TIME: Int = 1
        const val PROPERTY_FURNACE_LIT_DURATION: Int = 2

        //TODO: check property 3
        const val PROPERTY_FURNACE_FUEL_AUX: Int = 4

        const val PROPERTY_BREWING_STAND_BREW_TIME: Int = 0
        const val PROPERTY_BREWING_STAND_FUEL_AMOUNT: Int = 1
        const val PROPERTY_BREWING_STAND_FUEL_TOTAL: Int = 2
    }
}
