package org.chorus.block

import org.chorus.Player
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.property.type.IntPropertyType
import org.chorus.event.Event.isCancelled
import org.chorus.event.block.ComposterEmptyEvent.getDrop
import org.chorus.event.block.ComposterEmptyEvent.getNewLevel
import org.chorus.event.block.ComposterFillEvent.isSuccess
import org.chorus.item.*
import org.chorus.level.Sound
import org.chorus.math.BlockFace
import org.chorus.registry.Registries
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import java.util.*

class BlockComposter @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
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

    override fun toItem(): Item? {
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
        player: Player,
        blockFace: BlockFace?,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (isNotActivate(player)) return false
        if (item.isNull) {
            return false
        }

        if (isFull) {
            val event: ComposterEmptyEvent = ComposterEmptyEvent(this, player, item, Item.get(ItemID.BONE_MEAL), 0)
            Server.instance.pluginManager.callEvent(event)
            if (!event.isCancelled) {
                setPropertyValue<Int, IntPropertyType>(CommonBlockProperties.COMPOSTER_FILL_LEVEL, event.getNewLevel())
                level.setBlock(this.position, this, true, true)
                level.dropItem(position.add(0.5, 0.85, 0.5)!!, event.getDrop(), event.motion, false, 10)
                level.addSound(position.add(0.5, 0.5, 0.5)!!, Sound.BLOCK_COMPOSTER_EMPTY)
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
                level.addSound(position.add(0.5, 0.5, 0.5)!!, Sound.BLOCK_COMPOSTER_READY)
            } else {
                level.addSound(position.add(0.5, 0.5, 0.5)!!, Sound.BLOCK_COMPOSTER_FILL_SUCCESS)
            }
        } else {
            level.addSound(position.add(0.5, 0.5, 0.5)!!, Sound.BLOCK_COMPOSTER_FILL)
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
                level.dropItem(position.add(0.5, 0.85, 0.5)!!, event.getDrop(), event.motion, false, 10)
            }
            level.addSound(position.add(0.5, 0.5, 0.5)!!, Sound.BLOCK_COMPOSTER_EMPTY)
            return event.getDrop()
        }
        return null
    }

    val outPutItem: Item
        get() = OUTPUT_ITEM.clone()

    companion object {
        val properties: BlockProperties = BlockProperties(COMPOSTER, CommonBlockProperties.COMPOSTER_FILL_LEVEL)
            get() = Companion.field
        private val compostableItems: Object2IntMap<String> = Object2IntOpenHashMap()
        private val compostableBlocks: Object2IntMap<BlockState?> = Object2IntOpenHashMap()
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

        fun registerBlocks(chance: Int, vararg blockIds: String) {
            for (blockId in blockIds) {
                registerBlock(chance, blockId, 0)
            }
        }

        fun registerBlock(chance: Int, blockId: String) {
            val blockState = Registries.BLOCK.get(blockId)!!.blockState
            compostableBlocks.put(blockState, chance)
        }

        fun registerBlock(chance: Int, blockId: String, meta: Int) {
            val i = Registries.BLOCKSTATE_ITEMMETA.get(blockId, meta)
            val blockState: BlockState
            if (i == 0) {
                val block = Registries.BLOCK.get(blockId)
                blockState = block!!.properties.defaultState
            } else {
                blockState = Registries.BLOCKSTATE.get(i)
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
                KELP,
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
            registerItems(65, ItemID.APPLE, BEETROOT, ItemID.CARROT, ItemID.COCOA_BEANS, ItemID.POTATO)
            registerItems(85, ItemID.BAKED_POTATO, ItemID.BREAD, ItemID.COOKIE)
            registerItems(100, ItemID.PUMPKIN_PIE)

            registerBlocks(
                30,
                GRASS_BLOCK,
                PINK_PETALS,
                OAK_LEAVES,
                SPRUCE_LEAVES,
                BIRCH_LEAVES,
                JUNGLE_LEAVES,
                ACACIA_LEAVES,
                DARK_OAK_LEAVES,
                MANGROVE_LEAVES,
                CHERRY_LEAVES,
                AZALEA_LEAVES,
                PALE_OAK_LEAVES,
                OAK_SAPLING,
                SPRUCE_SAPLING,
                BIRCH_SAPLING,
                JUNGLE_SAPLING,
                ACACIA_SAPLING,
                DARK_OAK_SAPLING,
                CHERRY_SAPLING,
                PALE_OAK_SAPLING,
                MANGROVE_ROOTS,
                MANGROVE_PROPAGULE,
                SEAGRASS,
                SHORT_GRASS,
                SWEET_BERRY_BUSH,
                MOSS_CARPET, HANGING_ROOTS,
                SMALL_DRIPLEAF_BLOCK
            )
            registerBlocks(
                50, GLOW_LICHEN, CACTUS, DRIED_KELP_BLOCK, VINE, NETHER_SPROUTS,
                TWISTING_VINES, WEEPING_VINES, TALL_GRASS
            )
            registerBlocks(
                65,
                LARGE_FERN,
                FERN,
                WITHER_ROSE,
                WATERLILY,
                MELON_BLOCK,
                PUMPKIN,
                CARVED_PUMPKIN,
                PALE_MOSS_BLOCK,
                SEA_PICKLE,
                BROWN_MUSHROOM,
                RED_MUSHROOM,
                MUSHROOM_STEM,
                CRIMSON_FUNGUS,
                WARPED_FUNGUS,
                WARPED_ROOTS,
                CRIMSON_ROOTS,
                SHROOMLIGHT,
                NETHER_WART,
                AZALEA,
                BIG_DRIPLEAF,
                MOSS_BLOCK,
                SPORE_BLOSSOM,
                WHEAT,
                DANDELION,
                POPPY,
                BLUE_ORCHID,
                ALLIUM,
                AZURE_BLUET,
                RED_TULIP,
                ORANGE_TULIP,
                PINK_TULIP,
                WHITE_TULIP,
                OXEYE_DAISY,
                CORNFLOWER,
                LILY_OF_THE_VALLEY,
                CLOSED_EYEBLOSSOM,
                OPEN_EYEBLOSSOM
            )
            registerBlocks(
                85,
                HAY_BLOCK,
                BROWN_MUSHROOM_BLOCK,
                RED_MUSHROOM_BLOCK,
                FLOWERING_AZALEA,
                NETHER_WART_BLOCK,
                PITCHER_PLANT,
                TORCHFLOWER,
                WARPED_WART_BLOCK
            )
            registerBlocks(100, CAKE)
        }
    }
}
