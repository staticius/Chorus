package org.chorus.entity.mob.water_animal

import org.chorus.entity.mob.EntityMob
import org.chorus.level.format.IChunk
import org.chorus.nbt.tag.CompoundTag

abstract class EntityWaterAnimal(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt)
