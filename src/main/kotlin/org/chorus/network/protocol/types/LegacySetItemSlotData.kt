package org.chorus.network.protocol.types

import lombok.Value

@Value
class LegacySetItemSlotData {
    private val containerId = 0
    private val slots: ByteArray
}
