package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.event.player.PlayerEditBookEvent
import org.chorus.item.*
import org.chorus.item.Item.Companion.get
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.BookEditPacket
import org.chorus.network.protocol.ProtocolInfo


class BookEditProcessor : DataPacketProcessor<BookEditPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: BookEditPacket) {
        val player = playerHandle.player

        val oldBook = player.getInventory().getItem(pk.inventorySlot)
        if (oldBook.id != Item.WRITABLE_BOOK) {
            return
        }

        if (pk.action != BookEditPacket.Action.SIGN_BOOK) {
            if (pk.text == null || pk.text.length > 512) {
                return
            }
        }

        var newBook: Item = oldBook.clone()
        val success: Boolean
        when (pk.action) {
            BookEditPacket.Action.REPLACE_PAGE -> success =
                (newBook as ItemWritableBook).setPageText(pk.pageNumber, pk.text)

            BookEditPacket.Action.ADD_PAGE -> success = (newBook as ItemWritableBook).insertPage(pk.pageNumber, pk.text)
            BookEditPacket.Action.DELETE_PAGE -> success = (newBook as ItemWritableBook).deletePage(pk.pageNumber)
            BookEditPacket.Action.SWAP_PAGES -> success =
                (newBook as ItemWritableBook).swapPages(pk.pageNumber, pk.secondaryPageNumber)

            BookEditPacket.Action.SIGN_BOOK -> {
                if (pk.title == null || pk.author == null || pk.xuid == null || pk.title.length > 64 || pk.author.length > 64 || pk.xuid.length > 64) {
                    BookEditProcessor.log.debug(playerHandle.username + ": Invalid BookEditPacket action SIGN_BOOK: title/author/xuid is too long")
                    return
                }
                newBook = get(Item.WRITTEN_BOOK, 0, 1, oldBook.compoundTag)
                success = (newBook as ItemWrittenBook).signBook(
                    pk.title,
                    pk.author,
                    pk.xuid,
                    ItemWrittenBook.GENERATION_ORIGINAL
                )
            }

            else -> return
        }

        if (success) {
            val editBookEvent = PlayerEditBookEvent(player, oldBook, newBook, pk.action)
            Server.instance.pluginManager.callEvent(editBookEvent)
            if (!editBookEvent.isCancelled) {
                player.getInventory().setItem(pk.inventorySlot, editBookEvent.newBook)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.BOOK_EDIT_PACKET
}
