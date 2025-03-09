package org.chorus.event.potion

import cn.nukkit.entity.effect.PotionType
import cn.nukkit.entity.projectile.throwable.EntitySplashPotion
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

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
