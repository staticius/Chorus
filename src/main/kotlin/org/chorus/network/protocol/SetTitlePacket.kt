package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf







class SetTitlePacket : DataPacket() {
    @JvmField
    var type: Int = 0
    @JvmField
    var text: String = ""
    @JvmField
    var fadeInTime: Int = 0
    @JvmField
    var stayTime: Int = 0
    @JvmField
    var fadeOutTime: Int = 0
    var xuid: String = ""
    var platformOnlineId: String = ""
    private var filteredTitleText = ""

    override fun decode(byteBuf: HandleByteBuf) {
        this.type = byteBuf.readVarInt()
        this.text = byteBuf.readString()
        this.fadeInTime = byteBuf.readVarInt()
        this.stayTime = byteBuf.readVarInt()
        this.fadeOutTime = byteBuf.readVarInt()
        this.xuid = byteBuf.readString()
        this.platformOnlineId = byteBuf.readString()
        this.filteredTitleText = byteBuf.readString()
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeVarInt(type)
        byteBuf.writeString(text)
        byteBuf.writeVarInt(fadeInTime)
        byteBuf.writeVarInt(stayTime)
        byteBuf.writeVarInt(fadeOutTime)
        byteBuf.writeString(xuid)
        byteBuf.writeString(platformOnlineId)
        byteBuf.writeString(this.filteredTitleText)
    }

    var titleAction: TitleAction
        get() {
            val currentType = this.type
            if (currentType >= 0 && currentType < TITLE_ACTIONS.size) {
                return TITLE_ACTIONS[currentType]
            }
            throw UnsupportedOperationException("Bad type: $currentType")
        }
        set(type) {
            this.type = type.ordinal
        }

    enum class TitleAction {
        CLEAR,
        RESET,
        SET_TITLE_MESSAGE,
        SET_SUBTITLE_MESSAGE,
        SET_ACTION_BAR_MESSAGE,
        SET_ANIMATION_TIMES,
        SET_TITLE_JSON,
        SET_SUBTITLE_JSON,
        SET_ACTIONBAR_JSON,
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.SET_TITLE_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        private val TITLE_ACTIONS = TitleAction.entries.toTypedArray()

        const val TYPE_CLEAR: Int = 0
        const val TYPE_RESET: Int = 1
        const val TYPE_TITLE: Int = 2
        const val TYPE_SUBTITLE: Int = 3
        const val TYPE_ACTION_BAR: Int = 4
        const val TYPE_ANIMATION_TIMES: Int = 5
        const val TYPE_TITLE_JSON: Int = 6
        const val TYPE_SUBTITLE_JSON: Int = 7
        const val TYPE_ACTIONBAR_JSON: Int = 8
    }
}
