package org.chorus.event.potion

import org.chorus.entity.effect.PotionType
import org.chorus.entity.projectile.throwable.EntitySplashPotion
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

class PotionCollideEvent(potion: PotionType, val thrownPotion: EntitySplashPotion) : PotionEvent(potion),
    Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
