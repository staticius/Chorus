package org.chorus.entity.effect

import org.chorus.entity.*
import java.awt.Color

class EffectAbsorption :
    Effect(EffectType.Companion.ABSORPTION, "%potion.absorption", Color(37, 82, 165)) {
    override fun add(entity: Entity) {
        val absorption: Int = (4 * this.getLevel())
        if (absorption > entity.getAbsorption()) {
            entity.setAbsorption(absorption.toFloat())
        }
    }

    override fun remove(entity: Entity) {
        entity.setAbsorption(0f)
    }
}
