package org.chorus.event.inventory

import org.chorus.Player
import org.chorus.event.Cancellable
import org.chorus.event.Event
import org.chorus.event.HandlerList
import org.chorus.item.Item
import org.chorus.recipe.Recipe

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