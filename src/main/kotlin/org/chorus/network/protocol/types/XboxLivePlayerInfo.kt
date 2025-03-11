package org.chorus.network.protocol.types

import org.chorus.entity.data.Skin
import org.chorus.utils.ClientChainData

import java.util.*

class XboxLivePlayerInfo(
    username: String,
    uuid: UUID,
    skin: Skin,
    data: ClientChainData,
    val xuid: String
) : PlayerInfo(username, uuid, skin, data) {
}
