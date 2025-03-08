package cn.nukkit.level.generator

import cn.nukkit.level.Level
import cn.nukkit.level.format.IChunk
import lombok.Getter

@Getter
class ChunkGenerateContext(private val generator: Generator, private val level: Level?, private val chunk: IChunk?)
