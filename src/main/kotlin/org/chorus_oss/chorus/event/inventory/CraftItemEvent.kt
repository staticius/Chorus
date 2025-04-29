package org.chorus_oss.chorus.event.inventory

import org.chorus_oss.chorus.Player
import org.chorus_oss.chorus.event.Cancellable
import org.chorus_oss.chorus.event.Event
import org.chorus_oss.chorus.event.HandlerList
import org.chorus_oss.chorus.item.Item
import org.chorus_oss.chorus.recipe.Recipe

class CraftItemEvent(val player: Player, input: Array<Item>, recipe: Recipe, count: Int) :
    Event(), Cancellable {
    var input: Array<Item> = Item.EMPTY_ARRAY
        private set

    val recipe: Recipe

    val count: Int

    init {
        this.input = input
        this.recipe = recipe
        this.count = count
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}