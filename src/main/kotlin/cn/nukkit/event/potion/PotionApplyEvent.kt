package cn.nukkit.event.potion

import cn.nukkit.entity.Entity
import cn.nukkit.entity.effect.Effect
import cn.nukkit.entity.effect.PotionType
import cn.nukkit.event.Cancellable
import cn.nukkit.event.HandlerList

/**
 * @author Snake1999
 * @since 2016/1/12
 */
class PotionApplyEvent(potion: PotionType, var applyEffects: List<Effect>, val entity: Entity) :
    PotionEvent(potion), Cancellable {
    fun setApplyEffect(applyEffects: List<Effect>) {
        this.applyEffects = applyEffects
    }

    companion object {
        val handlers: HandlerList = HandlerList()
    }
}
