package org.chorus.block

import org.chorus.Player
import org.chorus.Server
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.event.block.ComposterEmptyEvent
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.math.BlockFace
import org.chorus.registry.Registries
import java.util.*
import kotlin.collections.HashMap

class BlockComposter @JvmOverloads constructor(blockstate: BlockState = Companion.properties.defaultState) :
    BlockSolid(blockstate) {
    override val name: String
        get() = "Composter"

    override val hardness: Double
        get() = 0.6

    override val resistance: Double
        get() = 0.6

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override fun canBeActivated(): Boolean {
        return true
    }

    override val waterloggingLevel: Int
        get() = 1

    override fun toItem(): Item {
        return ItemBlock(this, 0)
    }

    override fun hasComparatorInputOverride(): Boolean {
        return true
    }

    override val comparatorInputOverride: Int
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.COMPOSTER_FILL_LEVEL)

    fun incrementLevel(): Boolean {
        val fillLevel = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.COMPOSTER_FILL_LEVEL) + 1
        setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.COMPOSTER_FILL_LEVEL, fillLevel)
        level.setBlock(this.position, this, true, true)
        return fillLevel == 8
    }

    val isFull: Boolean
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.COMPOSTER_FILL_LEVEL) == 8

    val isEmpty: Boolean
        get() = getPropertyValue<Int, IntPropertyType>(CommonBlockProperties.COMPOSTER_FILL_LEVEL) == 0

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (isNotActivate(player)) return false
        if (item.isNothing) {
            return false
        }

        if (isFull) {
            val event: ComposterEmptyEvent = ComposterEmptyEvent(this, player, item, Item.get(ItemID.BONE_MEAL), 0)
            Server.instance.pluginManager.callEvent(event)
            if (!event.isCancelled) {
                setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.COMPOSTER_FILL_LEVEL, event.getNewLevel())
                level.setBlock(this.position, this, true, true)
                level.dropItem(position.add(0.5, 0.85, 0.5), event.getDrop(), event.motion, false, 10)
                level.addSound(position.add(0.5, 0.5, 0.5), Sound.BLOCK_COMPOSTER_EMPTY)
            }
            return true
        }

        val chance = getChance(item)
        if (chance <= 0) {
            return false
        }

        val success = Random().nextInt(100) < chance
        val event: ComposterFillEvent = ComposterFillEvent(this, player, item, chance, success)
        Server.instance.pluginManager.callEvent(event)

        if (event.isCancelled) {
            return true
        }

        if (player != null && !player.isCreative) {
            item.setCount(item.getCount() - 1)
        }

        if (event.isSuccess) {
            if (incrementLevel()) {
                level.addSound(position.add(0.5, 0.5, 0.5), Sound.BLOCK_COMPOSTER_READY)
            } else {
                level.addSound(position.add(0.5, 0.5, 0.5), Sound.BLOCK_COMPOSTER_FILL_SUCCESS)
            }
        } else {
            level.addSound(position.add(0.5, 0.5, 0.5), Sound.BLOCK_COMPOSTER_FILL)
        }

        return true
    }

    fun empty(): Item? {
        return empty(null, null)
    }

    fun empty(item: Item?, player: Player?): Item? {
        val event: ComposterEmptyEvent = ComposterEmptyEvent(this, player, item, ItemBoneMeal(), 0)
        Server.instance.pluginManager.callEvent(event)
        if (!event.isCancelled) {
            setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.COMPOSTER_FILL_LEVEL, event.getNewLevel())
            level.setBlock(this.position, this, true, true)
            if (item != null) {
                level.dropItem(position.add(0.5, 0.85, 0.5), event.getDrop(), event.motion, false, 10)
            }
            level.addSound(position.add(0.5, 0.5, 0.5), Sound.BLOCK_COMPOSTER_EMPTY)
            return event.getDrop()
        }
        return null
    }

    val outPutItem: Item
        get() = OUTPUT_ITEM.clone()

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.COMPOSTER, CommonBlockProperties.COMPOSTER_FILL_LEVEL)

        private val compostableItems: MutableMap<String, Int> = HashMap()
        private val compostableBlocks: MutableMap<BlockState, Int> = HashMap()
        val OUTPUT_ITEM: Item = ItemBoneMeal()

        fun init() {
            registerDefaults()
        }

        fun isNotActivate(player: Player?): Boolean {
            if (player == null) {
                return false
            }
            return Block.isNotActivate(player)
        }

        fun registerItem(chance: Int, itemId: String) {
            compostableItems.put(itemId, chance)
        }

        fun registerItems(chance: Int, vararg itemId: String) {
            for (minecraftItemID in itemId) {
                registerItem(chance, minecraftItemID)
            }
        }

        fun registerBlocks(chance: Int, vararg BlockID.: String)
        {
            for (BlockID. in BlockID.) {
            registerBlock(chance, BlockID.0)
        }
        }

        fun registerBlock(chance: Int, blockId: String)
        {
            val blockState = Registries.BLOCK.get(blockId)!!.blockState
            compostableBlocks.put (blockState, chance)
        }

        fun registerBlock(chance: Int, blockId: String, meta: Int)
        {
            val i = Registries.BLOCKSTATE_ITEMMETA.get(blockId)
            val blockState: BlockState
            if (i == 0) {
                val block = Registries.BLOCK.get(blockId)
                blockState = block!!.properties.defaultState
            } else {
                blockState = Registries.BLOCKSTATE.get(i)!!
            }
            compostableBlocks.put(blockState, chance)
        }

        fun getChance(item: Item): Int {
            return if (item is ItemBlock) {
                compostableBlocks.getInt(item.blockUnsafe!!.blockState)
            } else {
                compostableItems.getInt(item.id)
            }
        }

        private fun registerDefaults() {
            registerItems(
                30,
                BlockID.KELP,
                ItemID.BEETROOT_SEEDS,
                ItemID.DRIED_KELP,
                ItemID.MELON_SEEDS,
                ItemID.PUMPKIN_SEEDS,
                ItemID.SWEET_BERRIES,
                ItemID.WHEAT_SEEDS,
                ItemID.GLOW_BERRIES,
                ItemID.PITCHER_POD,
                ItemID.TORCHFLOWER_SEEDS
            )
            registerItems(50, ItemID.MELON_SLICE, ItemID.SUGAR_CANE)
            registerItems(65, ItemID.APPLE, BlockID.BEETROOT, ItemID.CARROT, ItemID.COCOA_BEANS, ItemID.POTATO)
            registerItems(85, ItemID.BAKED_POTATO, ItemID.BREAD, ItemID.COOKIE)
            registerItems(100, ItemID.PUMPKIN_PIE)

            registerBlocks(
                30,
                BlockID.GRASS_BLOCK,
                BlockID.PINK_PETALS,
                BlockID.OAK_LEAVES,
                BlockID.SPRUCE_LEAVES,
                BlockID.BIRCH_LEAVES,
                BlockID.JUNGLE_LEAVES,
                BlockID.ACACIA_LEAVES,
                BlockID.DARK_OAK_LEAVES,
                BlockID.MANGROVE_LEAVES,
                BlockID.CHERRY_LEAVES,
                BlockID.AZALEA_LEAVES,
                BlockID.PALE_OAK_LEAVES,
                BlockID.OAK_SAPLING,
                BlockID.SPRUCE_SAPLING,
                BlockID.BIRCH_SAPLING,
                BlockID.JUNGLE_SAPLING,
                BlockID.ACACIA_SAPLING,
                BlockID.DARK_OAK_SAPLING,
                BlockID.CHERRY_SAPLING,
                BlockID.PALE_OAK_SAPLING,
                BlockID.MANGROVE_ROOTS,
                BlockID.MANGROVE_PROPAGULE,
                BlockID.SEAGRASS,
                BlockID.SHORT_GRASS,
                BlockID.SWEET_BERRY_BUSH,
                BlockID.MOSS_CARPET, BlockID.HANGING_ROOTS,
                BlockID.SMALL_DRIPLEAF_BLOCK
            )
            registerBlocks(
                50, BlockID.GLOW_LICHEN, BlockID.CACTUS, BlockID.DRIED_KELP_BLOCK, BlockID.VINE, BlockID.NETHER_SPROUTS,
                BlockID.TWISTING_VINES, BlockID.WEEPING_VINES, BlockID.TALL_GRASS
            )
            registerBlocks(
                65,
                BlockID.LARGE_FERN,
                BlockID.FERN,
                BlockID.WITHER_ROSE,
                BlockID.WATERLILY,
                BlockID.MELON_BLOCK,
                BlockID.PUMPKIN,
                BlockID.CARVED_PUMPKIN,
                BlockID.PALE_MOSS_BLOCK,
                BlockID.SEA_PICKLE,
                BlockID.BROWN_MUSHROOM,
                BlockID.RED_MUSHROOM,
                BlockID.MUSHROOM_STEM,
                BlockID.CRIMSON_FUNGUS,
                BlockID.WARPED_FUNGUS,
                BlockID.WARPED_ROOTS,
                BlockID.CRIMSON_ROOTS,
                BlockID.SHROOMLIGHT,
                BlockID.NETHER_WART,
                BlockID.AZALEA,
                BlockID.BIG_DRIPLEAF,
                BlockID.MOSS_BLOCK,
                BlockID.SPORE_BLOSSOM,
                BlockID.WHEAT,
                BlockID.DANDELION,
                BlockID.POPPY,
                BlockID.BLUE_ORCHID,
                BlockID.ALLIUM,
                BlockID.AZURE_BLUET,
                BlockID.RED_TULIP,
                BlockID.ORANGE_TULIP,
                BlockID.PINK_TULIP,
                BlockID.WHITE_TULIP,
                BlockID.OXEYE_DAISY,
                BlockID.CORNFLOWER,
                BlockID.LILY_OF_THE_VALLEY,
                BlockID.CLOSED_EYEBLOSSOM,
                BlockID.OPEN_EYEBLOSSOM
            )
            registerBlocks(
                85,
                BlockID.HAY_BLOCK,
                BlockID.BROWN_MUSHROOM_BLOCK,
                BlockID.RED_MUSHROOM_BLOCK,
                BlockID.FLOWERING_AZALEA,
                BlockID.NETHER_WART_BLOCK,
                BlockID.PITCHER_PLANT,
                BlockID.TORCHFLOWER,
                BlockID.WARPED_WART_BLOCK
            )
            registerBlocks(100, BlockID.CAKE)
        }
    }
}
