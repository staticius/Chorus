package org.chorus

import org.chorus.metadata.Metadatable
import org.chorus.permission.ServerOperator
import java.util.*

/**
 * 用来描述一个玩家和获得这个玩家相应信息的接口。<br></br>
 * An interface to describe a player and get its information.
 *
 *
 * 这个玩家可以在线，也可以是不在线。<br></br>
 * This player can be online or offline.
 *
 * @author MagicDroidX(code) @ Nukkit Project
 * @author 粉鞋大妈(javadoc) @ Nukkit Project
 * @see org.chorus.Player
 *
 * @see org.chorus.OfflinePlayer
 *
 * @since Nukkit 1.0 | Nukkit API 1.0.0
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
