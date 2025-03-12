package org.chorus.item

import org.chorus.Player
import org.chorus.entity.effect.EffectType

/**
 * @author Snake1999
 * @since 2016/1/14
 */
class ItemRottenFlesh @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemFood(ItemID.Companion.ROTTEN_FLESH, meta, count, "Rotten Flesh") {
    override val foodRestore: Int
        get() = 4

    override val saturationRestore: Float
        get() = 0.8f

    override fun onEaten(player: Player): Boolean {
        if (0.8f >= Math.random()) {
            player.addEffect(get(EffectType.HUNGER).setDuration(30 * 20))
        }

        return true
    }
}
