package org.chorus.event.block

import org.chorus.Player
import org.chorus.blockentity.BlockEntityLectern
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList
import org.chorus.item.Item

class LecternPlaceBookEvent(val player: Player, val lectern: BlockEntityLectern, private var book: Item) :
    BlockEvent(lectern.block), Cancellable {
    fun getBook(): Item {
        return book.clone()
    }

    fun setBook(book: Item) {
        this.book = book
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
