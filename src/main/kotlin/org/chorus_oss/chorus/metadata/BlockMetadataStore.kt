package org.chorus_oss.chorus.metadata

import org.chorus_oss.chorus.block.Block
import org.chorus_oss.chorus.level.Level
import org.chorus_oss.chorus.plugin.Plugin
import java.util.*


class BlockMetadataStore(private val owningLevel: Level) : MetadataStore() {
    override fun disambiguate(block: Metadatable, metadataKey: String): String {
        require(block is Block) { "Argument must be a Block instance" }
        return block.position.floorX.toString() + ":" + block.position.floorY + ":" + block.position.floorZ + ":" + metadataKey
    }

    override fun getMetadata(block: Metadatable, metadataKey: String): List<MetadataValue> {
        require(block is Block) { "Object must be a Block" }
        if (block.level == this.owningLevel) {
            return super.getMetadata(block, metadataKey)
        } else {
            throw IllegalStateException("Block does not belong to world " + owningLevel.getLevelName())
        }
    }

    override fun getMetadata(block: Metadatable, metadataKey: String, plugin: Plugin): MetadataValue? {
        require(block is Block) { "Object must be a Block" }
        if (block.level == this.owningLevel) {
            return super.getMetadata(block, metadataKey, plugin)
        } else {
            throw IllegalStateException("Block does not belong to world " + owningLevel.getLevelName())
        }
    }

    override fun hasMetadata(block: Metadatable, metadataKey: String): Boolean {
        require(block is Block) { "Object must be a Block" }
        if (block.level == this.owningLevel) {
            return super.hasMetadata(block, metadataKey)
        } else {
            throw IllegalStateException("Block does not belong to world " + owningLevel.getLevelName())
        }
    }

    override fun hasMetadata(block: Metadatable, metadataKey: String, plugin: Plugin): Boolean {
        require(block is Block) { "Object must be a Block" }
        if (block.level == this.owningLevel) {
            return super.hasMetadata(block, metadataKey, plugin)
        } else {
            throw IllegalStateException("Block does not belong to world " + owningLevel.getLevelName())
        }
    }

    override fun removeMetadata(block: Metadatable, metadataKey: String, owningPlugin: Plugin) {
        require(block is Block) { "Object must be a Block" }
        if (block.level == this.owningLevel) {
            super.removeMetadata(block, metadataKey, owningPlugin)
        } else {
            throw IllegalStateException("Block does not belong to world " + owningLevel.getLevelName())
        }
    }

    override fun setMetadata(block: Metadatable, metadataKey: String, newMetadataValue: MetadataValue) {
        require(block is Block) { "Object must be a Block" }
        if (block.level == this.owningLevel) {
            super.setMetadata(block, metadataKey, newMetadataValue)
        } else {
            throw IllegalStateException("Block does not belong to world " + owningLevel.getLevelName())
        }
    }

    val blockMetadataMap: Map<String, Map<Plugin, MetadataValue>>
        get() = Collections.unmodifiableMap(
            metadataMap
        )
}
