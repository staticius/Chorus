package org.chorus.entity

import org.chorus.Player

interface EntityInteractable {
    // Todo: Passive entity?? i18n and boat leaving text
    fun getInteractButtonText(player: Player): String

    fun canDoInteraction(): Boolean
}
