package org.chorus.network.protocol.types

import org.chorus.entity.data.Skin
import org.chorus.utils.ClientChainData
import lombok.AllArgsConstructor
import lombok.Getter
import java.util.*

open class PlayerInfo (
    val username: String,
    val uniqueId: UUID,
    val skin: Skin,
    val data: ClientChainData,
)
