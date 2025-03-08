package cn.nukkit.network.protocol.types

import cn.nukkit.entity.data.Skin
import cn.nukkit.utils.ClientChainData
import lombok.AllArgsConstructor
import lombok.Getter
import java.util.*

@Getter
@AllArgsConstructor
open class PlayerInfo {
    private val username: String? = null
    private val uniqueId: UUID? = null
    private val skin: Skin? = null
    private val data: ClientChainData? = null
}
