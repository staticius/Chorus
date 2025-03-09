package org.chorus.event.item

import org.chorus.event.Event
import org.chorus.item.Item

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class ItemEvent(val item: Item) : Event()
