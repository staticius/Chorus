package cn.nukkit.event.item

import cn.nukkit.event.Event
import cn.nukkit.item.Item

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class ItemEvent(val item: Item) : Event()
