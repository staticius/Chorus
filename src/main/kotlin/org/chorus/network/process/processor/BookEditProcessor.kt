package org.chorus.network.process.processor

import org.chorus.PlayerHandle
import org.chorus.Server
import org.chorus.event.player.PlayerEditBookEvent
import org.chorus.item.Item
import org.chorus.item.Item.Companion.get
import org.chorus.item.ItemID
import org.chorus.item.ItemWritableBook
import org.chorus.item.ItemWrittenBook
import org.chorus.network.process.DataPacketProcessor
import org.chorus.network.protocol.BookEditPacket
import org.chorus.network.protocol.ProtocolInfo
import org.chorus.utils.Loggable


class BookEditProcessor : DataPacketProcessor<BookEditPacket>() {
    override fun handle(playerHandle: PlayerHandle, pk: BookEditPacket) {
        val player = playerHandle.player

        val oldBook = player.inventory.getItem(pk.bookSlot.toInt())
        if (oldBook.id != ItemID.WRITABLE_BOOK) {
            return
        }

        var newBook: Item = oldBook.clone()
        val success: Boolean
        when (pk.action) {
            BookEditPacket.Action.REPLACE_PAGE -> {
                val actionData = pk.actionData as BookEditPacket.ReplacePageData
                if (actionData.text.length > 512) return

                success = (newBook as ItemWritableBook).setPageText(actionData.pageIndex.toInt(), actionData.text)
            }

            BookEditPacket.Action.ADD_PAGE -> {
                val actionData = pk.actionData as BookEditPacket.AddPageData
                if (actionData.text.length > 512) return

                success = (newBook as ItemWritableBook).insertPage(actionData.pageIndex.toInt(), actionData.text)
            }

            BookEditPacket.Action.DELETE_PAGE -> {
                val actionData = pk.actionData as BookEditPacket.DeletePageData
                success = (newBook as ItemWritableBook).deletePage(actionData.pageIndex.toInt())
            }

            BookEditPacket.Action.SWAP_PAGES -> {
                val actionData = pk.actionData as BookEditPacket.SwapPagesData
                success = (newBook as ItemWritableBook).swapPages(
                    actionData.pageIndexA.toInt(),
                    actionData.pageIndexB.toInt()
                )
            }

            BookEditPacket.Action.FINALIZE -> {
                val actionData = pk.actionData as BookEditPacket.FinalizeData
                if (actionData.title.length > 64 || actionData.author.length > 64 || actionData.xuid.length > 64) return

                newBook = get(ItemID.WRITTEN_BOOK, 0, 1, oldBook.compoundTag)
                success = (newBook as ItemWrittenBook).signBook(
                    actionData.title,
                    actionData.author,
                    actionData.xuid,
                    ItemWrittenBook.GENERATION_ORIGINAL
                )
            }

            else -> return
        }

        if (success) {
            val editBookEvent = PlayerEditBookEvent(player, oldBook, newBook, pk.action)
            Server.instance.pluginManager.callEvent(editBookEvent)
            if (!editBookEvent.isCancelled) {
                player.inventory.setItem(pk.bookSlot.toInt(), editBookEvent.newBook)
            }
        }
    }

    override val packetId: Int
        get() = ProtocolInfo.BOOK_EDIT_PACKET

    companion object : Loggable
}
