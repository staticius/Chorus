/**
 * 用于存储block,entity,level,player的一些元数据的类.
 *
 *
 * These classes are used to store some metadata about block, entity, level, and player.
 */
package org.chorus.metadata

import org.chorus.math.Vector3.floorX
import org.chorus.math.Vector3.floorY
import org.chorus.math.Vector3.floorZ
import org.chorus.level.Level.getName
import org.chorus.entity.Entity.getId
import org.chorus.metadata.MetadataValue
import java.util.HashMap
import org.chorus.metadata.Metadatable
import org.chorus.metadata.MetadataValueAdapter
import org.chorus.metadata.LazyMetadataValue
import org.chorus.metadata.MetadataEvaluationException
import org.chorus.metadata.MetadataStore
import org.chorus.IPlayer
import org.chorus.utils.NumberConversions

