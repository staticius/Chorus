package org.chorus.event.level

import cn.nukkit.level.format.IChunk

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class ChunkEvent(val chunk: IChunk) : LevelEvent(chunk.provider.level)
