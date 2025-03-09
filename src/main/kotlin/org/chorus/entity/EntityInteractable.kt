package org.chorus.entity

import cn.nukkit.Player


/**
 * @author Adam Matthew
 */
interface EntityInteractable {
    // Todo: Passive entity?? i18n and boat leaving text
    fun getInteractButtonText(player: Player): String

    fun canDoInteraction(): Boolean
}
