package org.chorus.event.inventory

import cn.nukkit.Player
import cn.nukkit.event.Cancellable
import cn.nukkit.event.Event
import cn.nukkit.event.HandlerList
import cn.nukkit.item.Item
import cn.nukkit.recipe.Recipe

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