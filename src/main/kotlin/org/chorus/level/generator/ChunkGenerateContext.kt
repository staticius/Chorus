package org.chorus.level.generator

import org.chorus.level.Level
import org.chorus.level.format.IChunk
import lombok.Getter

@Getter
class ChunkGenerateContext(private val generator: Generator, private val level: Level?, private val chunk: IChunk?)
