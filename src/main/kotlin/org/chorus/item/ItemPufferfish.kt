package org.chorus.item

import cn.nukkit.Player
import cn.nukkit.entity.effect.Effect.Companion.get
import cn.nukkit.entity.effect.EffectType

/**
 * @author Snake1999
 * @since 2016/1/14
 */
class ItemPufferfish @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemFish(ItemID.Companion.PUFFERFISH, meta, count) {
    override val foodRestore: Int
        get() = 1

    override val saturationRestore: Float
        get() = 0.2f

    override fun onEaten(player: Player): Boolean {
        player.addEffect(
            get(EffectType.HUNGER)
                .setDuration(15 * 20)
                .setAmplifier(2)
        )
        player.addEffect(
            get(EffectType.POISON)
                .setDuration(60 * 20)
                .setAmplifier(1)
        )
        player.addEffect(
            get(EffectType.NAUSEA)
                .setDuration(15 * 20)
                .setAmplifier(1)
        )

        return true
    }
}
