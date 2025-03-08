package cn.nukkit.event.block

import cn.nukkit.Player
import cn.nukkit.blockentity.BlockEntityLectern
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item

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
