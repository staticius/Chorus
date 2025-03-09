package org.chorus.item

import org.chorus.Player
import org.chorus.entity.effect.Effect.Companion.get
import org.chorus.entity.effect.EffectType

/**
 * @author Snake1999
 * @since 2016/1/14
 */
class ItemSpiderEye @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemFood(ItemID.Companion.SPIDER_EYE, meta, count, "Spider Eye") {
    override val foodRestore: Int
        get() = 2

    override val saturationRestore: Float
        get() = 3.2f

    override fun onEaten(player: Player): Boolean {
        player.addEffect(get(EffectType.POISON).setDuration(5 * 20))

        return true
    }
}
