package org.chorus.network.protocol.types

import org.chorus.entity.data.Skin
import org.chorus.utils.ClientChainData
import lombok.Getter
import java.util.*

class XboxLivePlayerInfo(
    username: String?,
    uuid: UUID?,
    skin: Skin?,
    data: ClientChainData?,
     private val xuid: String
) : PlayerInfo(username, uuid, skin, data) {
}
