package org.chorus.event.item

import org.chorus.event.Event
import org.chorus.item.Item


abstract class ItemEvent(val item: Item) : Event()
