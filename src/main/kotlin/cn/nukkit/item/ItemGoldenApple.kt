package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.entity.effect.Effect.Companion.get
import cn.nukkit.entity.effect.EffectType
import cn.nukkit.math.*

class ItemGoldenApple : ItemFood(ItemID.Companion.GOLDEN_APPLE) {
    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        return true
    }

    override val foodRestore: Int
        get() = 4

    override val saturationRestore: Float
        get() = 9.6f

    override fun onEaten(player: Player): Boolean {
        player.addEffect(get(EffectType.REGENERATION).setAmplifier(1).setDuration(5 * 20))
        player.addEffect(get(EffectType.ABSORPTION).setAmplifier(0).setDuration(120 * 20))
        return true
    }
}