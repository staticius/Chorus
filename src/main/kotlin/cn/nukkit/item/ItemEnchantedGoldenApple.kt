package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.entity.effect.Effect.Companion.get
import cn.nukkit.entity.effect.EffectType
import cn.nukkit.math.*

class ItemEnchantedGoldenApple : ItemFood(ItemID.Companion.ENCHANTED_GOLDEN_APPLE) {
    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        return true
    }

    override val foodRestore: Int
        get() = 4

    override val saturationRestore: Float
        get() = 2.4f

    override fun onEaten(player: Player): Boolean {
        player.addEffect(
            get(EffectType.ABSORPTION)
                .setAmplifier(3)
                .setDuration(120 * 20)
        )
        player.addEffect(
            get(EffectType.REGENERATION).setAmplifier
                (4).setDuration
                (30 * 20)
        )
        player.addEffect(
            get(EffectType.FIRE_RESISTANCE)
                .setDuration(5 * 60 * 20)
        )
        player.addEffect(
            get(EffectType.RESISTANCE)
                .setDuration(5 * 60 * 20)
        )

        return true
    }
}