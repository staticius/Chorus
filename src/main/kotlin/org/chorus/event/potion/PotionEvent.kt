package org.chorus.event.potion

import org.chorus.entity.effect.PotionType
import org.chorus.event.Event

/**
 * @author Snake1999
 * @since 2016/1/12
 */
abstract class PotionEvent(var potion: PotionType) : Event()
