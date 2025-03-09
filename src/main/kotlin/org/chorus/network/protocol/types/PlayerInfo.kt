package org.chorus.network.protocol.types

import org.chorus.entity.data.Skin
import org.chorus.utils.ClientChainData
import lombok.AllArgsConstructor
import lombok.Getter
import java.util.*



open class PlayerInfo {
    private val username: String? = null
    private val uniqueId: UUID? = null
    private val skin: Skin? = null
    private val data: ClientChainData? = null
}
