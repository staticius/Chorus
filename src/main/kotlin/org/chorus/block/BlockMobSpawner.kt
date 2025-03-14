package org.chorus.block

import org.chorus.Player
import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntityMobSpawner.spawnEntityType
import org.chorus.blockentity.BlockEntitySpawnable.spawnToAll
import org.chorus.item.Item
import org.chorus.item.ItemSpawnEgg.entityNetworkId
import org.chorus.item.ItemTool
import org.chorus.math.BlockFace
import org.chorus.nbt.tag.CompoundTag.putInt
import org.chorus.nbt.tag.CompoundTag.putString
import org.chorus.registry.Registries

class BlockMobSpawner @JvmOverloads constructor(blockState: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockState) {
    override val name: String
        get() = "Monster Spawner"

    override val toolType: Int
        get() = ItemTool.TYPE_PICKAXE

    override val toolTier: Int
        get() = ItemTool.TIER_WOODEN

    override val hardness: Double
        get() = 5.0

    override val resistance: Double
        get() = 25.0

    override fun getDrops(item: Item): Array<Item?> {
        return Item.EMPTY_ARRAY
    }

    override fun canBePushed(): Boolean {
        return false
    }

    override val waterloggingLevel: Int
        get() = 1

    override fun canBePulled(): Boolean {
        return false
    }

    override fun canHarvestWithHand(): Boolean {
        return false
    }

    override fun canBeActivated(): Boolean {
        return true
    }

    fun setType(networkId: Int): Boolean {
        val identifier = Registries.ENTITY.getEntityIdentifier(networkId)

        val blockEntity = level.getBlockEntity(this.position)
        if (blockEntity is BlockEntityMobSpawner) {
            blockEntity.spawnEntityType = identifier
        } else {
            blockEntity?.close()
            val nbt: CompoundTag = CompoundTag()
                .putString(BlockEntityMobSpawner.TAG_ID, BlockEntity.MOB_SPAWNER)
                .putString(BlockEntityMobSpawner.TAG_ENTITY_IDENTIFIER, identifier)
                .putInt(BlockEntityMobSpawner.TAG_X, position.floorX)
                .putInt(BlockEntityMobSpawner.TAG_Y, position.floorY)
                .putInt(BlockEntityMobSpawner.TAG_Z, position.floorZ)

            val entitySpawner: BlockEntityMobSpawner = BlockEntityMobSpawner(
                level.getChunk(
                    position.chunkX,
                    position.chunkZ
                ), nbt
            )
            entitySpawner.spawnToAll()
        }
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (item !is ItemSpawnEgg) return false
        if (player == null) return false
        if (player.isAdventure) return false
        if (setType(item.entityNetworkId)) {
            if (!player.isCreative) {
                player.getInventory().decreaseCount(player.getInventory().heldItemIndex)
            }
            return true
        }
        return false
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.MOB_SPAWNER)

    }
}
