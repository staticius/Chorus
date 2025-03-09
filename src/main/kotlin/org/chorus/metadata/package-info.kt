/**
 * 用于存储block,entity,level,player的一些元数据的类.
 *
 *
 * These classes are used to store some metadata about block, entity, level, and player.
 */
package org.chorus.metadata

import cn.nukkit.math.Vector3.floorX
import cn.nukkit.math.Vector3.floorY
import cn.nukkit.math.Vector3.floorZ
import cn.nukkit.level.Level.getName
import cn.nukkit.entity.Entity.getId
import cn.nukkit.metadata.MetadataValue
import java.util.HashMap
import cn.nukkit.metadata.Metadatable
import cn.nukkit.metadata.MetadataValueAdapter
import cn.nukkit.metadata.LazyMetadataValue
import cn.nukkit.metadata.MetadataEvaluationException
import cn.nukkit.metadata.MetadataStore
import cn.nukkit.IPlayer
import cn.nukkit.utils.NumberConversions

