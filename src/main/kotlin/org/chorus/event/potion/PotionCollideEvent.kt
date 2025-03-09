package org.chorus.event.potion

import org.chorus.entity.effect.PotionType
import org.chorus.entity.projectile.throwable.EntitySplashPotion
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

/**
 * @author Snake1999
 * @since 2016/1/12
 */
class PotionCollideEvent(potion: PotionType, val thrownPotion: EntitySplashPotion) : PotionEvent(potion),
    Cancellable {
    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
