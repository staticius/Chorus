package org.chorus.network.process.processor

import cn.nukkit.network.protocol.ProtocolInfo

class LevelSoundEventProcessorV1 : LevelSoundEventProcessor() {
    override val packetId: Int
        get() = ProtocolInfo.LEVEL_SOUND_EVENT_PACKET_V1
}
