package org.chorus.network.protocol.types

import cn.nukkit.entity.data.Skin
import cn.nukkit.utils.ClientChainData
import lombok.Getter
import java.util.*

class XboxLivePlayerInfo(
    username: String?,
    uuid: UUID?,
    skin: Skin?,
    data: ClientChainData?,
    @Getter private val xuid: String
) : PlayerInfo(username, uuid, skin, data) {
}
