package org.chorus.event.level

import org.chorus.level.format.IChunk

/**
 * @author MagicDroidX (Nukkit Project)
 */
abstract class ChunkEvent(val chunk: IChunk) : LevelEvent(chunk.provider.level)
