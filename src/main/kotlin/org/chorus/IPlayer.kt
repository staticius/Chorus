package org.chorus

import org.chorus.metadata.Metadatable
import org.chorus.permission.ServerOperator
import java.util.*

/**
 * An interface to describe a player and get its information.
 *
 * This player can be online or offline.
 *
 * @see org.chorus.Player
 * @see org.chorus.OfflinePlayer
 */
interface IPlayer : ServerOperator, Metadatable {
    val isOnline: Boolean

    val name: String?

    val uuid: UUID?

    var isBanned: Boolean

    var isWhitelisted: Boolean

    val player: Player

    val firstPlayed: Long?

    val lastPlayed: Long?

    fun hasPlayedBefore(): Boolean
}
