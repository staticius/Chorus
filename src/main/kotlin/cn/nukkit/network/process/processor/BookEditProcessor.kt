package cn.nukkit.network.process.processor

import cn.nukkit.PlayerHandle
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.event.player.PlayerEditBookEvent
import cn.nukkit.item.*
import cn.nukkit.item.Item.Companion.get
import cn.nukkit.network.process.DataPacketProcessor
import cn.nukkit.network.protocol.BookEditPacket
import cn.nukkit.network.protocol.ProtocolInfo
import lombok.extern.slf4j.Slf4j

@Slf4j
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
            player.getServer().getPluginManager().callEvent(editBookEvent)
            if (!editBookEvent.isCancelled) {
                player.getInventory().setItem(pk.inventorySlot, editBookEvent.newBook)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.BOOK_EDIT_PACKET
}
