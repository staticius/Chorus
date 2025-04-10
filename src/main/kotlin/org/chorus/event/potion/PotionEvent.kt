package org.chorus.event.potion

import org.chorus.entity.effect.PotionType
import org.chorus.event.Event

abstract class PotionEvent(var potion: PotionType) : Event()
