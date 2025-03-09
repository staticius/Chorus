package org.chorus.event.potion

import org.chorus.entity.Entity
import org.chorus.entity.effect.Effect
import org.chorus.entity.effect.PotionType
import org.chorus.event.Cancellable
import org.chorus.event.HandlerList

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
