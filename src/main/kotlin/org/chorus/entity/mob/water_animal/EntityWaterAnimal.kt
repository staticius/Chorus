package org.chorus.entity.mob.water_animal

import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag

abstract class EntityWaterAnimal(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt)
