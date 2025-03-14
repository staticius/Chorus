package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf


class BookEditPacket : DataPacket() {
    var action: Action? = null
    var inventorySlot: Int = 0
    var pageNumber: Int = 0
    var secondaryPageNumber: Int = 0

    var text: String? = null
    var photoName: String? = null

    var title: String? = null
    var author: String? = null
    var xuid: String? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.action = Action.entries[byteBuf.readByte().toInt()]
        this.inventorySlot = byteBuf.readByte().toInt()

        when (this.action) {
            Action.REPLACE_PAGE, Action.ADD_PAGE -> {
                this.pageNumber = byteBuf.readByte().toInt()
                this.text = byteBuf.readString()
                this.photoName = byteBuf.readString()
            }

            Action.DELETE_PAGE -> this.pageNumber = byteBuf.readByte().toInt()
            Action.SWAP_PAGES -> {
                this.pageNumber = byteBuf.readByte().toInt()
                this.secondaryPageNumber = byteBuf.readByte().toInt()
            }

            Action.SIGN_BOOK -> {
                this.title = byteBuf.readString()
                this.author = byteBuf.readString()
                this.xuid = byteBuf.readString()
            }
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    enum class Action {
        REPLACE_PAGE,
        ADD_PAGE,
        DELETE_PAGE,
        SWAP_PAGES,
        SIGN_BOOK
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.BOOK_EDIT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
