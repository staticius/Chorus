package org.chorus.network.process.processor

import org.chorus.network.protocol.ProtocolInfo

class LevelSoundEventProcessorV2 : LevelSoundEventProcessor() {
    override val packetId: Int
        get() = ProtocolInfo.LEVEL_SOUND_EVENT_PACKET_V1
}
