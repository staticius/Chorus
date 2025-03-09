package org.chorus.entity.effect

import java.awt.Color

class EffectDarkness :
    Effect(EffectType.Companion.DARKNESS, "%effect.darkness", Color(41, 39, 33), true) {
    init {
        this.setVisible(false)
    }
}
