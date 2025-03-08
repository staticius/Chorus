package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.entity.effect.Effect.Companion.get
import cn.nukkit.entity.effect.EffectType

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
