package org.chorus.blockentity

import org.chorus.block.BlockID
import org.chorus.block.property.enums.StructureBlockType
import org.chorus.inventory.*
import org.chorus.level.format.IChunk
import org.chorus.math.BlockVector3
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.StructureBlockUpdatePacket
import org.chorus.network.protocol.types.StructureAnimationMode
import org.chorus.network.protocol.types.StructureMirror
import org.chorus.network.protocol.types.StructureRedstoneSaveMode
import org.chorus.network.protocol.types.StructureRotation
import com.google.common.base.Strings

class BlockEntityStructBlock(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt), IStructBlock,
    BlockEntityInventoryHolder {
    private var animationMode: StructureAnimationMode? = null
    private var animationSeconds = 0f
    private var data: StructureBlockType? = null
    private var dataField: String? = null
    private var ignoreEntities = false
    private var includePlayers = false
    private var integrity = 0f
    private var isPowered = false
    private var mirror: StructureMirror? = null
    private var redstoneSaveMode: StructureRedstoneSaveMode? = null
    private var removeBlocks = false
    private var rotation: StructureRotation? = null
    private var seed: Long = 0
    private var showBoundingBox = false
    private var structureName: String? = null
    private var size: BlockVector3? = null
    private var offset: BlockVector3? = null
    private val structBlockInventory = StructBlockInventory(this)

    override fun loadNBT() {
        super.loadNBT()
        if (namedTag.contains(IStructBlock.Companion.TAG_ANIMATION_MODE)) {
            this.animationMode =
                StructureAnimationMode.from(namedTag.getByte(IStructBlock.Companion.TAG_ANIMATION_MODE).toInt())
        } else {
            this.animationMode = StructureAnimationMode.from(0)
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_ANIMATION_SECONDS)) {
            this.animationSeconds = namedTag.getFloat(IStructBlock.Companion.TAG_ANIMATION_SECONDS)
        } else {
            this.animationSeconds = 0f
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_DATA)) {
            this.data = StructureBlockType.from(namedTag.getByte(IStructBlock.Companion.TAG_DATA).toInt())
        } else {
            this.data = StructureBlockType.from(1)
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_DATA_FIELD)) {
            this.dataField = namedTag.getString(IStructBlock.Companion.TAG_DATA_FIELD)
        } else {
            this.dataField = ""
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_IGNORE_ENTITIES)) {
            this.ignoreEntities = namedTag.getBoolean(IStructBlock.Companion.TAG_IGNORE_ENTITIES)
        } else {
            this.ignoreEntities = false
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_INCLUDE_PLAYERS)) {
            this.includePlayers = namedTag.getBoolean(IStructBlock.Companion.TAG_INCLUDE_PLAYERS)
        } else {
            this.includePlayers = false
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_INTEGRITY)) {
            this.integrity = namedTag.getFloat(IStructBlock.Companion.TAG_INTEGRITY)
        } else {
            this.integrity = 100f
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_IS_POWERED)) {
            this.isPowered = namedTag.getBoolean(IStructBlock.Companion.TAG_IS_POWERED)
        } else {
            this.isPowered = false
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_MIRROR)) {
            this.mirror = StructureMirror.from(namedTag.getByte(IStructBlock.Companion.TAG_MIRROR).toInt())
        } else {
            this.mirror = StructureMirror.from(0)
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_REDSTONE_SAVEMODE)) {
            this.redstoneSaveMode =
                StructureRedstoneSaveMode.from(namedTag.getByte(IStructBlock.Companion.TAG_REDSTONE_SAVEMODE).toInt())
        } else {
            this.redstoneSaveMode = StructureRedstoneSaveMode.from(0)
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_REMOVE_BLOCKS)) {
            this.removeBlocks = namedTag.getBoolean(IStructBlock.Companion.TAG_REMOVE_BLOCKS)
        } else {
            this.removeBlocks = false
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_ROTATION)) {
            this.rotation = StructureRotation.from(namedTag.getByte(IStructBlock.Companion.TAG_ROTATION).toInt())
        } else {
            this.rotation = StructureRotation.from(0)
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_SEED)) {
            this.seed = namedTag.getLong(IStructBlock.Companion.TAG_SEED)
        } else {
            this.seed = 0L
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_SHOW_BOUNDING_BOX)) {
            this.showBoundingBox = namedTag.getBoolean(IStructBlock.Companion.TAG_SHOW_BOUNDING_BOX)
        } else {
            this.showBoundingBox = true
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_STRUCTURE_NAME)) {
            this.structureName = namedTag.getString(IStructBlock.Companion.TAG_STRUCTURE_NAME)
        } else {
            this.structureName = ""
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_X_STRUCTURE_OFFSET) && namedTag.contains(IStructBlock.Companion.TAG_Y_STRUCTURE_OFFSET) && namedTag.contains(
                IStructBlock.Companion.TAG_Z_STRUCTURE_OFFSET
            )
        ) {
            this.offset = BlockVector3(
                namedTag.getInt(IStructBlock.Companion.TAG_X_STRUCTURE_OFFSET),
                namedTag.getInt(IStructBlock.Companion.TAG_Y_STRUCTURE_OFFSET),
                namedTag.getInt(IStructBlock.Companion.TAG_Z_STRUCTURE_OFFSET)
            )
        } else {
            this.offset = BlockVector3(0, -1, 0)
        }
        if (namedTag.contains(IStructBlock.Companion.TAG_X_STRUCTURE_SIZE) && namedTag.contains(IStructBlock.Companion.TAG_Y_STRUCTURE_SIZE) && namedTag.contains(
                IStructBlock.Companion.TAG_Z_STRUCTURE_SIZE
            )
        ) {
            this.size = BlockVector3(
                namedTag.getInt(IStructBlock.Companion.TAG_X_STRUCTURE_SIZE),
                namedTag.getInt(IStructBlock.Companion.TAG_Y_STRUCTURE_SIZE),
                namedTag.getInt(IStructBlock.Companion.TAG_Z_STRUCTURE_SIZE)
            )
        } else {
            this.size = BlockVector3(5, 5, 5)
        }
    }

    override val spawnCompound: CompoundTag
        get() = super.getSpawnCompound()
            .putByte(IStructBlock.Companion.TAG_ANIMATION_MODE, animationMode!!.ordinal)
            .putFloat(IStructBlock.Companion.TAG_ANIMATION_SECONDS, this.animationSeconds)
            .putInt(IStructBlock.Companion.TAG_DATA, data!!.ordinal)
            .putString(IStructBlock.Companion.TAG_DATA_FIELD, this.dataField)
            .putBoolean(IStructBlock.Companion.TAG_IGNORE_ENTITIES, ignoreEntities)
            .putBoolean(IStructBlock.Companion.TAG_INCLUDE_PLAYERS, includePlayers)
            .putFloat(IStructBlock.Companion.TAG_INTEGRITY, integrity)
            .putBoolean(IStructBlock.Companion.TAG_IS_POWERED, isPowered)
            .putByte(IStructBlock.Companion.TAG_MIRROR, mirror!!.ordinal)
            .putByte(IStructBlock.Companion.TAG_REDSTONE_SAVEMODE, redstoneSaveMode!!.ordinal)
            .putBoolean(IStructBlock.Companion.TAG_REMOVE_BLOCKS, removeBlocks)
            .putByte(IStructBlock.Companion.TAG_ROTATION, rotation!!.ordinal)
            .putLong(IStructBlock.Companion.TAG_SEED, seed)
            .putBoolean(IStructBlock.Companion.TAG_SHOW_BOUNDING_BOX, showBoundingBox)
            .putString(IStructBlock.Companion.TAG_STRUCTURE_NAME, structureName)
            .putInt(IStructBlock.Companion.TAG_X_STRUCTURE_OFFSET, offset!!.x)
            .putInt(IStructBlock.Companion.TAG_Y_STRUCTURE_OFFSET, offset!!.y)
            .putInt(IStructBlock.Companion.TAG_Z_STRUCTURE_OFFSET, offset!!.z)
            .putInt(IStructBlock.Companion.TAG_X_STRUCTURE_SIZE, size!!.x)
            .putInt(IStructBlock.Companion.TAG_Y_STRUCTURE_SIZE, size!!.y)
            .putInt(IStructBlock.Companion.TAG_Z_STRUCTURE_SIZE, size!!.z)

    override fun saveNBT() {
        super.saveNBT()
        namedTag.putByte(
            IStructBlock.Companion.TAG_ANIMATION_MODE,
            animationMode!!.ordinal
        )
            .putFloat(IStructBlock.Companion.TAG_ANIMATION_SECONDS, this.animationSeconds)
            .putInt(IStructBlock.Companion.TAG_DATA, data!!.ordinal)
            .putString(IStructBlock.Companion.TAG_DATA_FIELD, dataField!!)
            .putBoolean(IStructBlock.Companion.TAG_IGNORE_ENTITIES, ignoreEntities)
            .putBoolean(IStructBlock.Companion.TAG_INCLUDE_PLAYERS, includePlayers)
            .putFloat(IStructBlock.Companion.TAG_INTEGRITY, integrity)
            .putBoolean(IStructBlock.Companion.TAG_IS_POWERED, isPowered)
            .putByte(IStructBlock.Companion.TAG_MIRROR, mirror!!.ordinal)
            .putByte(IStructBlock.Companion.TAG_REDSTONE_SAVEMODE, redstoneSaveMode!!.ordinal)
            .putBoolean(IStructBlock.Companion.TAG_REMOVE_BLOCKS, removeBlocks)
            .putByte(IStructBlock.Companion.TAG_ROTATION, rotation!!.ordinal)
            .putLong(IStructBlock.Companion.TAG_SEED, seed)
            .putBoolean(IStructBlock.Companion.TAG_SHOW_BOUNDING_BOX, showBoundingBox)
            .putString(IStructBlock.Companion.TAG_STRUCTURE_NAME, structureName!!)
            .putInt(IStructBlock.Companion.TAG_X_STRUCTURE_OFFSET, offset!!.x)
            .putInt(IStructBlock.Companion.TAG_Y_STRUCTURE_OFFSET, offset!!.y)
            .putInt(IStructBlock.Companion.TAG_Z_STRUCTURE_OFFSET, offset!!.z)
            .putInt(IStructBlock.Companion.TAG_X_STRUCTURE_SIZE, size!!.x)
            .putInt(IStructBlock.Companion.TAG_Y_STRUCTURE_SIZE, size!!.y)
            .putInt(IStructBlock.Companion.TAG_Z_STRUCTURE_SIZE, size!!.z)
    }

    override val isBlockEntityValid: Boolean
        get() {
            val BlockID.= this.levelBlock.id
            return BlockID.=== BlockID.STRUCTURE_BLOCK
        }

    override var name: String
        get() = if (this.hasName()) namedTag.getString(IStructBlock.Companion.TAG_CUSTOM_NAME) else BlockEntityID.Companion.STRUCTURE_BLOCK
        set(name) {
            if (Strings.isNullOrEmpty(name)) {
                namedTag.remove(IStructBlock.Companion.TAG_CUSTOM_NAME)
            } else {
                namedTag.putString(IStructBlock.Companion.TAG_CUSTOM_NAME, name)
            }
        }

    override fun hasName(): Boolean {
        return namedTag.contains(IStructBlock.Companion.TAG_CUSTOM_NAME)
    }

    override fun getInventory(): Inventory {
        return structBlockInventory
    }

    override fun close() {
        if (!closed) {
            for (player in HashSet(this.inventory.viewers)) {
                player.removeWindow(this.inventory)
            }
            super.close()
        }
    }

    fun updateSetting(packet: StructureBlockUpdatePacket) {
        val editorData = packet.editorData
        this.animationMode = editorData.settings.animationMode
        this.animationSeconds = editorData.settings.animationSeconds
        this.data = editorData.type
        this.dataField = editorData.dataField
        this.ignoreEntities = editorData.settings.isIgnoringEntities
        this.includePlayers = editorData.isIncludingPlayers
        this.integrity = editorData.settings.integrityValue
        this.isPowered = packet.powered
        this.mirror = editorData.settings.mirror
        this.redstoneSaveMode = editorData.redstoneSaveMode
        this.removeBlocks = editorData.settings.isIgnoringBlocks
        this.rotation = editorData.settings.rotation
        this.seed = editorData.settings.integritySeed.toLong()
        this.showBoundingBox = editorData.isBoundingBoxVisible
        this.structureName = editorData.name
        this.offset = editorData.settings.offset
        this.size = editorData.settings.size
    }
}
