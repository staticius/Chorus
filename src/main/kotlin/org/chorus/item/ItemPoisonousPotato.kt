package org.chorus.item

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.effect.Effect.Companion.get
import cn.nukkit.entity.effect.EffectType

class ItemPoisonousPotato @JvmOverloads constructor(meta: Int = 0, count: Int = 1) :
    ItemPotato(ItemID.Companion.POISONOUS_POTATO, meta, count, "Poisonous Potato") {
    init {
        this.block = Block.get(BlockID.POTATOES)
    }

    override fun onEaten(player: Player): Boolean {
        if (0.6f >= Math.random()) {
            player.addEffect(get(EffectType.POISON).setDuration(80))
        }
        return true
    }

    override val foodRestore: Int
        get() = 2

    override val saturationRestore: Float
        get() = 1.2f
}