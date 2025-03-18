package org.chorus.registry

import me.sunlan.fastreflection.FastConstructor
import me.sunlan.fastreflection.FastMemberLoader
import org.chorus.block.*
import org.chorus.block.customblock.CustomBlock
import org.chorus.block.customblock.CustomBlockDefinition
import org.chorus.item.ItemBlock
import org.chorus.level.Level
import org.chorus.plugin.Plugin
import org.chorus.registry.ItemRuntimeIdRegistry.RuntimeEntry
import org.chorus.utils.Loggable
import org.jetbrains.annotations.UnmodifiableView
import java.lang.reflect.Modifier
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer
import kotlin.collections.HashMap
import kotlin.collections.set

class BlockRegistry : IRegistry<String, Block?, Class<out Block?>>, Loggable {
    override fun init() {
        if (isLoad.getAndSet(true)) return
        try {
            register(BlockID.ACACIA_BUTTON, BlockAcaciaButton::class.java)
            register(BlockID.ACACIA_DOOR, BlockAcaciaDoor::class.java)
            register(BlockID.ACACIA_DOUBLE_SLAB, BlockAcaciaDoubleSlab::class.java)
            register(BlockID.ACACIA_FENCE, BlockAcaciaFence::class.java)
            register(BlockID.ACACIA_FENCE_GATE, BlockAcaciaFenceGate::class.java)
            register(BlockID.ACACIA_HANGING_SIGN, BlockAcaciaHangingSign::class.java)
            register(BlockID.ACACIA_LEAVES, BlockAcaciaLeaves::class.java)
            register(BlockID.ACACIA_LOG, BlockAcaciaLog::class.java)
            register(BlockID.ACACIA_PLANKS, BlockAcaciaPlanks::class.java)
            register(BlockID.ACACIA_PRESSURE_PLATE, BlockAcaciaPressurePlate::class.java)
            register(BlockID.ACACIA_SAPLING, BlockAcaciaSapling::class.java)
            register(BlockID.ACACIA_SLAB, BlockAcaciaSlab::class.java)
            register(BlockID.ACACIA_STAIRS, BlockAcaciaStairs::class.java)
            register(BlockID.ACACIA_STANDING_SIGN, BlockAcaciaStandingSign::class.java)
            register(BlockID.ACACIA_TRAPDOOR, BlockAcaciaTrapdoor::class.java)
            register(BlockID.ACACIA_WALL_SIGN, BlockAcaciaWallSign::class.java)
            register(BlockID.ACACIA_WOOD, BlockAcaciaWood::class.java)
            register(BlockID.ACTIVATOR_RAIL, BlockActivatorRail::class.java)
            register(BlockID.AIR, BlockAir::class.java)
            register(BlockID.ALLIUM, BlockAllium::class.java)
            register(BlockID.ALLOW, BlockAllow::class.java)
            register(BlockID.AMETHYST_BLOCK, BlockAmethystBlock::class.java)
            register(BlockID.AMETHYST_CLUSTER, BlockAmethystCluster::class.java)
            register(BlockID.ANCIENT_DEBRIS, BlockAncientDebris::class.java)
            register(BlockID.ANDESITE, BlockAndesite::class.java)
            register(BlockID.ANDESITE_DOUBLE_SLAB, BlockAndesiteDoubleSlab::class.java)
            register(BlockID.ANDESITE_SLAB, BlockAndesiteSlab::class.java)
            register(BlockID.ANDESITE_STAIRS, BlockAndesiteStairs::class.java)
            register(BlockID.ANDESITE_WALL, BlockAndesiteWall::class.java)
            register(BlockID.ANVIL, BlockAnvil::class.java)
            register(BlockID.AZALEA, BlockAzalea::class.java)
            register(BlockID.AZALEA_LEAVES, BlockAzaleaLeaves::class.java)
            register(BlockID.AZALEA_LEAVES_FLOWERED, BlockAzaleaLeavesFlowered::class.java)
            register(BlockID.AZURE_BLUET, BlockAzureBluet::class.java)
            register(BlockID.BAMBOO, BlockBamboo::class.java)
            register(BlockID.BAMBOO_BLOCK, BlockBambooBlock::class.java)
            register(BlockID.BAMBOO_BUTTON, BlockBambooButton::class.java)
            register(BlockID.BAMBOO_DOOR, BlockBambooDoor::class.java)
            register(BlockID.BAMBOO_DOUBLE_SLAB, BlockBambooDoubleSlab::class.java)
            register(BlockID.BAMBOO_FENCE, BlockBambooFence::class.java)
            register(BlockID.BAMBOO_FENCE_GATE, BlockBambooFenceGate::class.java)
            register(BlockID.BAMBOO_HANGING_SIGN, BlockBambooHangingSign::class.java)
            register(BlockID.BAMBOO_MOSAIC, BlockBambooMosaic::class.java)
            register(BlockID.BAMBOO_MOSAIC_DOUBLE_SLAB, BlockBambooMosaicDoubleSlab::class.java)
            register(BlockID.BAMBOO_MOSAIC_SLAB, BlockBambooMosaicSlab::class.java)
            register(BlockID.BAMBOO_MOSAIC_STAIRS, BlockBambooMosaicStairs::class.java)
            register(BlockID.BAMBOO_PLANKS, BlockBambooPlanks::class.java)
            register(BlockID.BAMBOO_PRESSURE_PLATE, BlockBambooPressurePlate::class.java)
            register(BlockID.BAMBOO_SAPLING, BlockBambooSapling::class.java)
            register(BlockID.BAMBOO_SLAB, BlockBambooSlab::class.java)
            register(BlockID.BAMBOO_STAIRS, BlockBambooStairs::class.java)
            register(BlockID.BAMBOO_STANDING_SIGN, BlockBambooStandingSign::class.java)
            register(BlockID.BAMBOO_TRAPDOOR, BlockBambooTrapdoor::class.java)
            register(BlockID.BAMBOO_WALL_SIGN, BlockBambooWallSign::class.java)
            register(BlockID.BARREL, BlockBarrel::class.java)
            register(BlockID.BARRIER, BlockBarrier::class.java)
            register(BlockID.BASALT, BlockBasalt::class.java)
            register(BlockID.BEACON, BlockBeacon::class.java)
            register(BlockID.BED, BlockBed::class.java)
            register(BlockID.BEDROCK, BlockBedrock::class.java)
            register(BlockID.BEE_NEST, BlockBeeNest::class.java)
            register(BlockID.BEEHIVE, BlockBeehive::class.java)
            register(BlockID.BEETROOT, BlockBeetroot::class.java)
            register(BlockID.BELL, BlockBell::class.java)
            register(BlockID.BIG_DRIPLEAF, BlockBigDripleaf::class.java)
            register(BlockID.BIRCH_BUTTON, BlockBirchButton::class.java)
            register(BlockID.BIRCH_DOOR, BlockBirchDoor::class.java)
            register(BlockID.BIRCH_DOUBLE_SLAB, BlockBirchDoubleSlab::class.java)
            register(BlockID.BIRCH_FENCE, BlockBirchFence::class.java)
            register(BlockID.BIRCH_FENCE_GATE, BlockBirchFenceGate::class.java)
            register(BlockID.BIRCH_HANGING_SIGN, BlockBirchHangingSign::class.java)
            register(BlockID.BIRCH_LEAVES, BlockBirchLeaves::class.java)
            register(BlockID.BIRCH_LOG, BlockBirchLog::class.java)
            register(BlockID.BIRCH_PLANKS, BlockBirchPlanks::class.java)
            register(BlockID.BIRCH_PRESSURE_PLATE, BlockBirchPressurePlate::class.java)
            register(BlockID.BIRCH_SAPLING, BlockBirchSapling::class.java)
            register(BlockID.BIRCH_SLAB, BlockBirchSlab::class.java)
            register(BlockID.BIRCH_STAIRS, BlockBirchStairs::class.java)
            register(BlockID.BIRCH_STANDING_SIGN, BlockBirchStandingSign::class.java)
            register(BlockID.BIRCH_TRAPDOOR, BlockBirchTrapdoor::class.java)
            register(BlockID.BIRCH_WALL_SIGN, BlockBirchWallSign::class.java)
            register(BlockID.BIRCH_WOOD, BlockBirchWood::class.java)
            register(BlockID.BLACK_CANDLE, BlockBlackCandle::class.java)
            register(BlockID.BLACK_CANDLE_CAKE, BlockBlackCandleCake::class.java)
            register(BlockID.BLACK_CARPET, BlockBlackCarpet::class.java)
            register(BlockID.BLACK_CONCRETE, BlockBlackConcrete::class.java)
            register(BlockID.BLACK_CONCRETE_POWDER, BlockBlackConcretePowder::class.java)
            register(BlockID.BLACK_GLAZED_TERRACOTTA, BlockBlackGlazedTerracotta::class.java)
            register(BlockID.BLACK_SHULKER_BOX, BlockBlackShulkerBox::class.java)
            register(BlockID.BLACK_STAINED_GLASS, BlockBlackStainedGlass::class.java)
            register(BlockID.BLACK_STAINED_GLASS_PANE, BlockBlackStainedGlassPane::class.java)
            register(BlockID.BLACK_TERRACOTTA, BlockBlackTerracotta::class.java)
            register(BlockID.BLACK_WOOL, BlockBlackWool::class.java)
            register(BlockID.BLACKSTONE, BlockBlackstone::class.java)
            register(BlockID.BLACKSTONE_DOUBLE_SLAB, BlockBlackstoneDoubleSlab::class.java)
            register(BlockID.BLACKSTONE_SLAB, BlockBlackstoneSlab::class.java)
            register(BlockID.BLACKSTONE_STAIRS, BlockBlackstoneStairs::class.java)
            register(BlockID.BLACKSTONE_WALL, BlockBlackstoneWall::class.java)
            register(BlockID.BLAST_FURNACE, BlockBlastFurnace::class.java)
            register(BlockID.BLUE_CANDLE, BlockBlueCandle::class.java)
            register(BlockID.BLUE_CANDLE_CAKE, BlockBlueCandleCake::class.java)
            register(BlockID.BLUE_CARPET, BlockBlueCarpet::class.java)
            register(BlockID.BLUE_CONCRETE, BlockBlueConcrete::class.java)
            register(BlockID.BLUE_CONCRETE_POWDER, BlockBlueConcretePowder::class.java)
            register(BlockID.BLUE_GLAZED_TERRACOTTA, BlockBlueGlazedTerracotta::class.java)
            register(BlockID.BLUE_ICE, BlockBlueIce::class.java)
            register(BlockID.BLUE_ORCHID, BlockBlueOrchid::class.java)
            register(BlockID.BLUE_SHULKER_BOX, BlockBlueShulkerBox::class.java)
            register(BlockID.BLUE_STAINED_GLASS, BlockBlueStainedGlass::class.java)
            register(BlockID.BLUE_STAINED_GLASS_PANE, BlockBlueStainedGlassPane::class.java)
            register(BlockID.BLUE_TERRACOTTA, BlockBlueTerracotta::class.java)
            register(BlockID.BLUE_WOOL, BlockBlueWool::class.java)
            register(BlockID.BONE_BLOCK, BlockBoneBlock::class.java)
            register(BlockID.BOOKSHELF, BlockBookshelf::class.java)
            register(BlockID.BORDER_BLOCK, BlockBorderBlock::class.java)
            register(BlockID.BRAIN_CORAL, BlockBrainCoral::class.java)
            register(BlockID.BRAIN_CORAL_BLOCK, BlockBrainCoralBlock::class.java)
            register(BlockID.BRAIN_CORAL_FAN, BlockBrainCoralFan::class.java)
            register(BlockID.BRAIN_CORAL_WALL_FAN, BlockBrainCoralWallFan::class.java)
            register(BlockID.BREWING_STAND, BlockBrewingStand::class.java)
            register(BlockID.BRICK_BLOCK, BlockBrickBlock::class.java)
            register(BlockID.BRICK_DOUBLE_SLAB, BlockBrickDoubleSlab::class.java)
            register(BlockID.BRICK_SLAB, BlockBrickSlab::class.java)
            register(BlockID.BRICK_STAIRS, BlockBrickStairs::class.java)
            register(BlockID.BRICK_WALL, BlockBrickWall::class.java)
            register(BlockID.BROWN_CANDLE, BlockBrownCandle::class.java)
            register(BlockID.BROWN_CANDLE_CAKE, BlockBrownCandleCake::class.java)
            register(BlockID.BROWN_CARPET, BlockBrownCarpet::class.java)
            register(BlockID.BROWN_CONCRETE, BlockBrownConcrete::class.java)
            register(BlockID.BROWN_CONCRETE_POWDER, BlockBrownConcretePowder::class.java)
            register(BlockID.BROWN_GLAZED_TERRACOTTA, BlockBrownGlazedTerracotta::class.java)
            register(BlockID.BROWN_MUSHROOM, BlockBrownMushroom::class.java)
            register(BlockID.BROWN_MUSHROOM_BLOCK, BlockBrownMushroomBlock::class.java)
            register(BlockID.BROWN_SHULKER_BOX, BlockBrownShulkerBox::class.java)
            register(BlockID.BROWN_STAINED_GLASS, BlockBrownStainedGlass::class.java)
            register(BlockID.BROWN_STAINED_GLASS_PANE, BlockBrownStainedGlassPane::class.java)
            register(BlockID.BROWN_TERRACOTTA, BlockBrownTerracotta::class.java)
            register(BlockID.BROWN_WOOL, BlockBrownWool::class.java)
            register(BlockID.BUBBLE_COLUMN, BlockBubbleColumn::class.java)
            register(BlockID.BUBBLE_CORAL, BlockBubbleCoral::class.java)
            register(BlockID.BUBBLE_CORAL_BLOCK, BlockBubbleCoralBlock::class.java)
            register(BlockID.BUBBLE_CORAL_FAN, BlockBubbleCoralFan::class.java)
            register(BlockID.BUBBLE_CORAL_WALL_FAN, BlockBubbleCoralWallFan::class.java)
            register(BlockID.BUDDING_AMETHYST, BlockBuddingAmethyst::class.java)
            register(BlockID.CACTUS, BlockCactus::class.java)
            register(BlockID.CAKE, BlockCake::class.java)
            register(BlockID.CALCITE, BlockCalcite::class.java)
            register(BlockID.CALIBRATED_SCULK_SENSOR, BlockCalibratedSculkSensor::class.java)
            register(BlockID.CAMPFIRE, BlockCampfire::class.java)
            register(BlockID.CANDLE, BlockCandle::class.java)
            register(BlockID.CANDLE_CAKE, BlockCandleCake::class.java)
            register(BlockID.CARROTS, BlockCarrots::class.java)
            register(BlockID.CARTOGRAPHY_TABLE, BlockCartographyTable::class.java)
            register(BlockID.CARVED_PUMPKIN, BlockCarvedPumpkin::class.java)
            register(BlockID.CAULDRON, BlockCauldron::class.java)
            register(BlockID.CAVE_VINES, BlockCaveVines::class.java)
            register(BlockID.CAVE_VINES_BODY_WITH_BERRIES, BlockCaveVinesBodyWithBerries::class.java)
            register(BlockID.CAVE_VINES_HEAD_WITH_BERRIES, BlockCaveVinesHeadWithBerries::class.java)
            register(BlockID.CHAIN, BlockChain::class.java)
            register(BlockID.CHAIN_COMMAND_BLOCK, BlockChainCommandBlock::class.java)
            register(BlockID.CHERRY_BUTTON, BlockCherryButton::class.java)
            register(BlockID.CHERRY_DOOR, BlockCherryDoor::class.java)
            register(BlockID.CHERRY_DOUBLE_SLAB, BlockCherryDoubleSlab::class.java)
            register(BlockID.CHERRY_FENCE, BlockCherryFence::class.java)
            register(BlockID.CHERRY_FENCE_GATE, BlockCherryFenceGate::class.java)
            register(BlockID.CHERRY_HANGING_SIGN, BlockCherryHangingSign::class.java)
            register(BlockID.CHERRY_LEAVES, BlockCherryLeaves::class.java)
            register(BlockID.CHERRY_LOG, BlockCherryLog::class.java)
            register(BlockID.CHERRY_PLANKS, BlockCherryPlanks::class.java)
            register(BlockID.CHERRY_PRESSURE_PLATE, BlockCherryPressurePlate::class.java)
            register(BlockID.CHERRY_SAPLING, BlockCherrySapling::class.java)
            register(BlockID.CHERRY_SLAB, BlockCherrySlab::class.java)
            register(BlockID.CHERRY_STAIRS, BlockCherryStairs::class.java)
            register(BlockID.CHERRY_STANDING_SIGN, BlockCherryStandingSign::class.java)
            register(BlockID.CHERRY_TRAPDOOR, BlockCherryTrapdoor::class.java)
            register(BlockID.CHERRY_WALL_SIGN, BlockCherryWallSign::class.java)
            register(BlockID.CHERRY_WOOD, BlockCherryWood::class.java)
            register(BlockID.CHEST, BlockChest::class.java)
            register(BlockID.CHIPPED_ANVIL, BlockChippedAnvil::class.java)
            register(BlockID.CHISELED_BOOKSHELF, BlockChiseledBookshelf::class.java)
            register(BlockID.CHISELED_COPPER, BlockChiseledCopper::class.java)
            register(BlockID.CHISELED_DEEPSLATE, BlockChiseledDeepslate::class.java)
            register(BlockID.CHISELED_NETHER_BRICKS, BlockChiseledNetherBricks::class.java)
            register(BlockID.CHISELED_POLISHED_BLACKSTONE, BlockChiseledPolishedBlackstone::class.java)
            register(BlockID.CHISELED_QUARTZ_BLOCK, BlockChiseledQuartzBlock::class.java)
            register(BlockID.CHISELED_RED_SANDSTONE, BlockChiseledRedSandstone::class.java)
            register(BlockID.CHISELED_RESIN_BRICKS, BlockChiseledResinBricks::class.java)
            register(BlockID.CHISELED_SANDSTONE, BlockChiseledSandstone::class.java)
            register(BlockID.CHISELED_STONE_BRICKS, BlockChiseledStoneBricks::class.java)
            register(BlockID.CHISELED_TUFF, BlockChiseledTuff::class.java)
            register(BlockID.CHISELED_TUFF_BRICKS, BlockChiseledTuffBricks::class.java)
            register(BlockID.CHORUS_FLOWER, BlockChorusFlower::class.java)
            register(BlockID.CHORUS_PLANT, BlockChorusPlant::class.java)
            register(BlockID.CLAY, BlockClay::class.java)
            register(BlockID.CLIENT_REQUEST_PLACEHOLDER_BLOCK, BlockClientRequestPlaceholderBlock::class.java)
            register(BlockID.CLOSED_EYEBLOSSOM, BlockClosedEyeblossom::class.java)
            register(BlockID.COAL_BLOCK, BlockCoalBlock::class.java)
            register(BlockID.COAL_ORE, BlockCoalOre::class.java)
            register(BlockID.COARSE_DIRT, BlockCoarseDirt::class.java)
            register(BlockID.COBBLED_DEEPSLATE, BlockCobbledDeepslate::class.java)
            register(BlockID.COBBLED_DEEPSLATE_DOUBLE_SLAB, BlockCobbledDeepslateDoubleSlab::class.java)
            register(BlockID.COBBLED_DEEPSLATE_SLAB, BlockCobbledDeepslateSlab::class.java)
            register(BlockID.COBBLED_DEEPSLATE_STAIRS, BlockCobbledDeepslateStairs::class.java)
            register(BlockID.COBBLED_DEEPSLATE_WALL, BlockCobbledDeepslateWall::class.java)
            register(BlockID.COBBLESTONE, BlockCobblestone::class.java)
            register(BlockID.COBBLESTONE_DOUBLE_SLAB, BlockCobblestoneDoubleSlab::class.java)
            register(BlockID.COBBLESTONE_SLAB, BlockCobblestoneSlab::class.java)
            register(BlockID.COBBLESTONE_WALL, BlockCobblestoneWall::class.java)
            register(BlockID.COCOA, BlockCocoa::class.java)
            register(BlockID.COMMAND_BLOCK, BlockCommandBlock::class.java)
            register(BlockID.COMPOSTER, BlockComposter::class.java)
            register(BlockID.CONDUIT, BlockConduit::class.java)
            register(BlockID.COPPER_BLOCK, BlockCopperBlock::class.java)
            register(BlockID.COPPER_BULB, BlockCopperBulb::class.java)
            register(BlockID.COPPER_DOOR, BlockCopperDoor::class.java)
            register(BlockID.COPPER_GRATE, BlockCopperGrate::class.java)
            register(BlockID.COPPER_ORE, BlockCopperOre::class.java)
            register(BlockID.COPPER_TRAPDOOR, BlockCopperTrapdoor::class.java)
            register(BlockID.CORNFLOWER, BlockCornflower::class.java)
            register(BlockID.CRACKED_DEEPSLATE_BRICKS, BlockCrackedDeepslateBricks::class.java)
            register(BlockID.CRACKED_DEEPSLATE_TILES, BlockCrackedDeepslateTiles::class.java)
            register(BlockID.CRACKED_NETHER_BRICKS, BlockCrackedNetherBricks::class.java)
            register(BlockID.CRACKED_POLISHED_BLACKSTONE_BRICKS, BlockCrackedPolishedBlackstoneBricks::class.java)
            register(BlockID.CRACKED_STONE_BRICKS, BlockCrackedStoneBricks::class.java)
            register(BlockID.CRAFTER, BlockCrafter::class.java)
            register(BlockID.CRAFTING_TABLE, BlockCraftingTable::class.java)
            register(BlockID.CREAKING_HEART, BlockCreakingHeart::class.java)
            register(BlockID.CREEPER_HEAD, BlockCreeperHead::class.java)
            register(BlockID.CRIMSON_BUTTON, BlockCrimsonButton::class.java)
            register(BlockID.CRIMSON_DOOR, BlockCrimsonDoor::class.java)
            register(BlockID.CRIMSON_DOUBLE_SLAB, BlockCrimsonDoubleSlab::class.java)
            register(BlockID.CRIMSON_FENCE, BlockCrimsonFence::class.java)
            register(BlockID.CRIMSON_FENCE_GATE, BlockCrimsonFenceGate::class.java)
            register(BlockID.CRIMSON_FUNGUS, BlockCrimsonFungus::class.java)
            register(BlockID.CRIMSON_HANGING_SIGN, BlockCrimsonHangingSign::class.java)
            register(BlockID.CRIMSON_HYPHAE, BlockCrimsonHyphae::class.java)
            register(BlockID.CRIMSON_NYLIUM, BlockCrimsonNylium::class.java)
            register(BlockID.CRIMSON_PLANKS, BlockCrimsonPlanks::class.java)
            register(BlockID.CRIMSON_PRESSURE_PLATE, BlockCrimsonPressurePlate::class.java)
            register(BlockID.CRIMSON_ROOTS, BlockCrimsonRoots::class.java)
            register(BlockID.CRIMSON_SLAB, BlockCrimsonSlab::class.java)
            register(BlockID.CRIMSON_STAIRS, BlockCrimsonStairs::class.java)
            register(BlockID.CRIMSON_STANDING_SIGN, BlockCrimsonStandingSign::class.java)
            register(BlockID.CRIMSON_STEM, BlockCrimsonStem::class.java)
            register(BlockID.CRIMSON_TRAPDOOR, BlockCrimsonTrapdoor::class.java)
            register(BlockID.CRIMSON_WALL_SIGN, BlockCrimsonWallSign::class.java)
            register(BlockID.CRYING_OBSIDIAN, BlockCryingObsidian::class.java)
            register(BlockID.CUT_COPPER, BlockCutCopper::class.java)
            register(BlockID.CUT_COPPER_SLAB, BlockCutCopperSlab::class.java)
            register(BlockID.CUT_COPPER_STAIRS, BlockCutCopperStairs::class.java)
            register(BlockID.CUT_RED_SANDSTONE, BlockCutRedSandstone::class.java)
            register(BlockID.CUT_RED_SANDSTONE_DOUBLE_SLAB, BlockCutRedSandstoneDoubleSlab::class.java)
            register(BlockID.CUT_RED_SANDSTONE_SLAB, BlockCutRedSandstoneSlab::class.java)
            register(BlockID.CUT_SANDSTONE, BlockCutSandstone::class.java)
            register(BlockID.CUT_SANDSTONE_DOUBLE_SLAB, BlockCutSandstoneDoubleSlab::class.java)
            register(BlockID.CUT_SANDSTONE_SLAB, BlockCutSandstoneSlab::class.java)
            register(BlockID.CYAN_CANDLE, BlockCyanCandle::class.java)
            register(BlockID.CYAN_CANDLE_CAKE, BlockCyanCandleCake::class.java)
            register(BlockID.CYAN_CARPET, BlockCyanCarpet::class.java)
            register(BlockID.CYAN_CONCRETE, BlockCyanConcrete::class.java)
            register(BlockID.CYAN_CONCRETE_POWDER, BlockCyanConcretePowder::class.java)
            register(BlockID.CYAN_GLAZED_TERRACOTTA, BlockCyanGlazedTerracotta::class.java)
            register(BlockID.CYAN_SHULKER_BOX, BlockCyanShulkerBox::class.java)
            register(BlockID.CYAN_STAINED_GLASS, BlockCyanStainedGlass::class.java)
            register(BlockID.CYAN_STAINED_GLASS_PANE, BlockCyanStainedGlassPane::class.java)
            register(BlockID.CYAN_TERRACOTTA, BlockCyanTerracotta::class.java)
            register(BlockID.CYAN_WOOL, BlockCyanWool::class.java)
            register(BlockID.DAMAGED_ANVIL, BlockDamagedAnvil::class.java)
            register(BlockID.DARK_OAK_BUTTON, BlockDarkOakButton::class.java)
            register(BlockID.DARK_OAK_DOOR, BlockDarkOakDoor::class.java)
            register(BlockID.DARK_OAK_DOUBLE_SLAB, BlockDarkOakDoubleSlab::class.java)
            register(BlockID.DARK_OAK_FENCE, BlockDarkOakFence::class.java)
            register(BlockID.DARK_OAK_FENCE_GATE, BlockDarkOakFenceGate::class.java)
            register(BlockID.DARK_OAK_HANGING_SIGN, BlockDarkOakHangingSign::class.java)
            register(BlockID.DARK_OAK_LEAVES, BlockDarkOakLeaves::class.java)
            register(BlockID.DARK_OAK_LOG, BlockDarkOakLog::class.java)
            register(BlockID.DARK_OAK_PLANKS, BlockDarkOakPlanks::class.java)
            register(BlockID.DARK_OAK_PRESSURE_PLATE, BlockDarkOakPressurePlate::class.java)
            register(BlockID.DARK_OAK_SAPLING, BlockDarkOakSapling::class.java)
            register(BlockID.DARK_OAK_SLAB, BlockDarkOakSlab::class.java)
            register(BlockID.DARK_OAK_STAIRS, BlockDarkOakStairs::class.java)
            register(BlockID.DARK_OAK_TRAPDOOR, BlockDarkOakTrapdoor::class.java)
            register(BlockID.DARK_OAK_WOOD, BlockDarkOakWood::class.java)
            register(BlockID.DARK_PRISMARINE, BlockDarkPrismarine::class.java)
            register(BlockID.DARK_PRISMARINE_DOUBLE_SLAB, BlockDarkPrismarineDoubleSlab::class.java)
            register(BlockID.DARK_PRISMARINE_SLAB, BlockDarkPrismarineSlab::class.java)
            register(BlockID.DARK_PRISMARINE_STAIRS, BlockDarkPrismarineStairs::class.java)
            register(BlockID.DARKOAK_STANDING_SIGN, BlockDarkoakStandingSign::class.java)
            register(BlockID.DARKOAK_WALL_SIGN, BlockDarkoakWallSign::class.java)
            register(BlockID.DAYLIGHT_DETECTOR, BlockDaylightDetector::class.java)
            register(BlockID.DAYLIGHT_DETECTOR_INVERTED, BlockDaylightDetectorInverted::class.java)
            register(BlockID.DEAD_BRAIN_CORAL, BlockDeadBrainCoral::class.java)
            register(BlockID.DEAD_BRAIN_CORAL_BLOCK, BlockDeadBrainCoralBlock::class.java)
            register(BlockID.DEAD_BRAIN_CORAL_FAN, BlockDeadBrainCoralFan::class.java)
            register(BlockID.DEAD_BRAIN_CORAL_WALL_FAN, BlockDeadBrainCoralWallFan::class.java)
            register(BlockID.DEAD_BUBBLE_CORAL, BlockDeadBubbleCoral::class.java)
            register(BlockID.DEAD_BUBBLE_CORAL_BLOCK, BlockDeadBubbleCoralBlock::class.java)
            register(BlockID.DEAD_BUBBLE_CORAL_FAN, BlockDeadBubbleCoralFan::class.java)
            register(BlockID.DEAD_BUBBLE_CORAL_WALL_FAN, BlockDeadBubbleCoralWallFan::class.java)
            register(BlockID.DEAD_FIRE_CORAL, BlockDeadFireCoral::class.java)
            register(BlockID.DEAD_FIRE_CORAL_BLOCK, BlockDeadFireCoralBlock::class.java)
            register(BlockID.DEAD_FIRE_CORAL_FAN, BlockDeadFireCoralFan::class.java)
            register(BlockID.DEAD_FIRE_CORAL_WALL_FAN, BlockDeadFireCoralWallFan::class.java)
            register(BlockID.DEAD_HORN_CORAL, BlockDeadHornCoral::class.java)
            register(BlockID.DEAD_HORN_CORAL_BLOCK, BlockDeadHornCoralBlock::class.java)
            register(BlockID.DEAD_HORN_CORAL_FAN, BlockDeadHornCoralFan::class.java)
            register(BlockID.DEAD_HORN_CORAL_WALL_FAN, BlockDeadHornCoralWallFan::class.java)
            register(BlockID.DEAD_TUBE_CORAL, BlockDeadTubeCoral::class.java)
            register(BlockID.DEAD_TUBE_CORAL_BLOCK, BlockDeadTubeCoralBlock::class.java)
            register(BlockID.DEAD_TUBE_CORAL_FAN, BlockDeadTubeCoralFan::class.java)
            register(BlockID.DEAD_TUBE_CORAL_WALL_FAN, BlockDeadTubeCoralWallFan::class.java)
            register(BlockID.DEADBUSH, BlockDeadbush::class.java)
            register(BlockID.DECORATED_POT, BlockDecoratedPot::class.java)
            register(BlockID.DEEPSLATE, BlockDeepslate::class.java)
            register(BlockID.DEEPSLATE_BRICK_DOUBLE_SLAB, BlockDeepslateBrickDoubleSlab::class.java)
            register(BlockID.DEEPSLATE_BRICK_SLAB, BlockDeepslateBrickSlab::class.java)
            register(BlockID.DEEPSLATE_BRICK_STAIRS, BlockDeepslateBrickStairs::class.java)
            register(BlockID.DEEPSLATE_BRICK_WALL, BlockDeepslateBrickWall::class.java)
            register(BlockID.DEEPSLATE_BRICKS, BlockDeepslateBricks::class.java)
            register(BlockID.DEEPSLATE_COAL_ORE, BlockDeepslateCoalOre::class.java)
            register(BlockID.DEEPSLATE_COPPER_ORE, BlockDeepslateCopperOre::class.java)
            register(BlockID.DEEPSLATE_DIAMOND_ORE, BlockDeepslateDiamondOre::class.java)
            register(BlockID.DEEPSLATE_EMERALD_ORE, BlockDeepslateEmeraldOre::class.java)
            register(BlockID.DEEPSLATE_GOLD_ORE, BlockDeepslateGoldOre::class.java)
            register(BlockID.DEEPSLATE_IRON_ORE, BlockDeepslateIronOre::class.java)
            register(BlockID.DEEPSLATE_LAPIS_ORE, BlockDeepslateLapisOre::class.java)
            register(BlockID.DEEPSLATE_REDSTONE_ORE, BlockDeepslateRedstoneOre::class.java)
            register(BlockID.DEEPSLATE_TILE_DOUBLE_SLAB, BlockDeepslateTileDoubleSlab::class.java)
            register(BlockID.DEEPSLATE_TILE_SLAB, BlockDeepslateTileSlab::class.java)
            register(BlockID.DEEPSLATE_TILE_STAIRS, BlockDeepslateTileStairs::class.java)
            register(BlockID.DEEPSLATE_TILE_WALL, BlockDeepslateTileWall::class.java)
            register(BlockID.DEEPSLATE_TILES, BlockDeepslateTiles::class.java)
            register(BlockID.DENY, BlockDeny::class.java)
            register(BlockID.DETECTOR_RAIL, BlockDetectorRail::class.java)
            register(BlockID.DIAMOND_BLOCK, BlockDiamondBlock::class.java)
            register(BlockID.DIAMOND_ORE, BlockDiamondOre::class.java)
            register(BlockID.DIORITE, BlockDiorite::class.java)
            register(BlockID.DIORITE_DOUBLE_SLAB, BlockDioriteDoubleSlab::class.java)
            register(BlockID.DIORITE_SLAB, BlockDioriteSlab::class.java)
            register(BlockID.DIORITE_STAIRS, BlockDioriteStairs::class.java)
            register(BlockID.DIORITE_WALL, BlockDioriteWall::class.java)
            register(BlockID.DIRT, BlockDirt::class.java)
            register(BlockID.DIRT_WITH_ROOTS, BlockDirtWithRoots::class.java)
            register(BlockID.DISPENSER, BlockDispenser::class.java)
            register(BlockID.DOUBLE_CUT_COPPER_SLAB, BlockDoubleCutCopperSlab::class.java)
            register(BlockID.DRAGON_EGG, BlockDragonEgg::class.java)
            register(BlockID.DRAGON_HEAD, BlockDragonHead::class.java)
            register(BlockID.DRIED_KELP_BLOCK, BlockDriedKelpBlock::class.java)
            register(BlockID.DRIPSTONE_BLOCK, BlockDripstoneBlock::class.java)
            register(BlockID.DROPPER, BlockDropper::class.java)
            register(BlockID.EMERALD_BLOCK, BlockEmeraldBlock::class.java)
            register(BlockID.EMERALD_ORE, BlockEmeraldOre::class.java)
            register(BlockID.ENCHANTING_TABLE, BlockEnchantingTable::class.java)
            register(BlockID.END_BRICK_STAIRS, BlockEndBrickStairs::class.java)
            register(BlockID.END_BRICKS, BlockEndBricks::class.java)
            register(BlockID.END_GATEWAY, BlockEndGateway::class.java)
            register(BlockID.END_PORTAL, BlockEndPortal::class.java)
            register(BlockID.END_PORTAL_FRAME, BlockEndPortalFrame::class.java)
            register(BlockID.END_ROD, BlockEndRod::class.java)
            register(BlockID.END_STONE, BlockEndStone::class.java)
            register(BlockID.END_STONE_BRICK_DOUBLE_SLAB, BlockEndStoneBrickDoubleSlab::class.java)
            register(BlockID.END_STONE_BRICK_SLAB, BlockEndStoneBrickSlab::class.java)
            register(BlockID.END_STONE_BRICK_WALL, BlockEndStoneBrickWall::class.java)
            register(BlockID.ENDER_CHEST, BlockEnderChest::class.java)
            register(BlockID.EXPOSED_CHISELED_COPPER, BlockExposedChiseledCopper::class.java)
            register(BlockID.EXPOSED_COPPER, BlockExposedCopper::class.java)
            register(BlockID.EXPOSED_COPPER_BULB, BlockExposedCopperBulb::class.java)
            register(BlockID.EXPOSED_COPPER_DOOR, BlockExposedCopperDoor::class.java)
            register(BlockID.EXPOSED_COPPER_GRATE, BlockExposedCopperGrate::class.java)
            register(BlockID.EXPOSED_COPPER_TRAPDOOR, BlockExposedCopperTrapdoor::class.java)
            register(BlockID.EXPOSED_CUT_COPPER, BlockExposedCutCopper::class.java)
            register(BlockID.EXPOSED_CUT_COPPER_SLAB, BlockExposedCutCopperSlab::class.java)
            register(BlockID.EXPOSED_CUT_COPPER_STAIRS, BlockExposedCutCopperStairs::class.java)
            register(BlockID.EXPOSED_DOUBLE_CUT_COPPER_SLAB, BlockExposedDoubleCutCopperSlab::class.java)
            register(BlockID.FARMLAND, BlockFarmland::class.java)
            register(BlockID.FENCE_GATE, BlockFenceGate::class.java)
            register(BlockID.FERN, BlockFern::class.java)
            register(BlockID.FIRE, BlockFire::class.java)
            register(BlockID.FIRE_CORAL, BlockFireCoral::class.java)
            register(BlockID.FIRE_CORAL_BLOCK, BlockFireCoralBlock::class.java)
            register(BlockID.FIRE_CORAL_FAN, BlockFireCoralFan::class.java)
            register(BlockID.FIRE_CORAL_WALL_FAN, BlockFireCoralWallFan::class.java)
            register(BlockID.FLETCHING_TABLE, BlockFletchingTable::class.java)
            register(BlockID.FLOWER_POT, BlockFlowerPot::class.java)
            register(BlockID.FLOWERING_AZALEA, BlockFloweringAzalea::class.java)
            register(BlockID.FLOWING_LAVA, BlockFlowingLava::class.java)
            register(BlockID.FLOWING_WATER, BlockFlowingWater::class.java)
            register(BlockID.FRAME, BlockFrame::class.java)
            register(BlockID.FROG_SPAWN, BlockFrogSpawn::class.java)
            register(BlockID.FROSTED_ICE, BlockFrostedIce::class.java)
            register(BlockID.FURNACE, BlockFurnace::class.java)
            register(BlockID.GILDED_BLACKSTONE, BlockGildedBlackstone::class.java)
            register(BlockID.GLASS, BlockGlass::class.java)
            register(BlockID.GLASS_PANE, BlockGlassPane::class.java)
            register(BlockID.GLOW_FRAME, BlockGlowFrame::class.java)
            register(BlockID.GLOW_LICHEN, BlockGlowLichen::class.java)
            register(BlockID.GLOWINGOBSIDIAN, BlockGlowingobsidian::class.java)
            register(BlockID.GLOWSTONE, BlockGlowstone::class.java)
            register(BlockID.GOLD_BLOCK, BlockGoldBlock::class.java)
            register(BlockID.GOLD_ORE, BlockGoldOre::class.java)
            register(BlockID.GOLDEN_RAIL, BlockGoldenRail::class.java)
            register(BlockID.GRANITE, BlockGranite::class.java)
            register(BlockID.GRANITE_DOUBLE_SLAB, BlockGraniteDoubleSlab::class.java)
            register(BlockID.GRANITE_SLAB, BlockGraniteSlab::class.java)
            register(BlockID.GRANITE_STAIRS, BlockGraniteStairs::class.java)
            register(BlockID.GRANITE_WALL, BlockGraniteWall::class.java)
            register(BlockID.GRASS_BLOCK, BlockGrassBlock::class.java)
            register(BlockID.GRASS_PATH, BlockGrassPath::class.java)
            register(BlockID.GRAVEL, BlockGravel::class.java)
            register(BlockID.GRAY_CANDLE, BlockGrayCandle::class.java)
            register(BlockID.GRAY_CANDLE_CAKE, BlockGrayCandleCake::class.java)
            register(BlockID.GRAY_CARPET, BlockGrayCarpet::class.java)
            register(BlockID.GRAY_CONCRETE, BlockGrayConcrete::class.java)
            register(BlockID.GRAY_CONCRETE_POWDER, BlockGrayConcretePowder::class.java)
            register(BlockID.GRAY_GLAZED_TERRACOTTA, BlockGrayGlazedTerracotta::class.java)
            register(BlockID.GRAY_SHULKER_BOX, BlockGrayShulkerBox::class.java)
            register(BlockID.GRAY_STAINED_GLASS, BlockGrayStainedGlass::class.java)
            register(BlockID.GRAY_STAINED_GLASS_PANE, BlockGrayStainedGlassPane::class.java)
            register(BlockID.GRAY_TERRACOTTA, BlockGrayTerracotta::class.java)
            register(BlockID.GRAY_WOOL, BlockGrayWool::class.java)
            register(BlockID.GREEN_CANDLE, BlockGreenCandle::class.java)
            register(BlockID.GREEN_CANDLE_CAKE, BlockGreenCandleCake::class.java)
            register(BlockID.GREEN_CARPET, BlockGreenCarpet::class.java)
            register(BlockID.GREEN_CONCRETE, BlockGreenConcrete::class.java)
            register(BlockID.GREEN_CONCRETE_POWDER, BlockGreenConcretePowder::class.java)
            register(BlockID.GREEN_GLAZED_TERRACOTTA, BlockGreenGlazedTerracotta::class.java)
            register(BlockID.GREEN_SHULKER_BOX, BlockGreenShulkerBox::class.java)
            register(BlockID.GREEN_STAINED_GLASS, BlockGreenStainedGlass::class.java)
            register(BlockID.GREEN_STAINED_GLASS_PANE, BlockGreenStainedGlassPane::class.java)
            register(BlockID.GREEN_TERRACOTTA, BlockGreenTerracotta::class.java)
            register(BlockID.GREEN_WOOL, BlockGreenWool::class.java)
            register(BlockID.GRINDSTONE, BlockGrindstone::class.java)
            register(BlockID.HANGING_ROOTS, BlockHangingRoots::class.java)
            register(BlockID.HARDENED_CLAY, BlockHardenedClay::class.java)
            register(BlockID.HAY_BLOCK, BlockHayBlock::class.java)
            register(BlockID.HEAVY_CORE, BlockHeavyCore::class.java)
            register(BlockID.HEAVY_WEIGHTED_PRESSURE_PLATE, BlockHeavyWeightedPressurePlate::class.java)
            register(BlockID.HONEY_BLOCK, BlockHoneyBlock::class.java)
            register(BlockID.HONEYCOMB_BLOCK, BlockHoneycombBlock::class.java)
            register(BlockID.HOPPER, BlockHopper::class.java)
            register(BlockID.HORN_CORAL, BlockHornCoral::class.java)
            register(BlockID.HORN_CORAL_BLOCK, BlockHornCoralBlock::class.java)
            register(BlockID.HORN_CORAL_FAN, BlockHornCoralFan::class.java)
            register(BlockID.HORN_CORAL_WALL_FAN, BlockHornCoralWallFan::class.java)
            register(BlockID.ICE, BlockIce::class.java)
            register(BlockID.INFESTED_CHISELED_STONE_BRICKS, BlockInfestedChiseledStoneBricks::class.java)
            register(BlockID.INFESTED_COBBLESTONE, BlockInfestedCobblestone::class.java)
            register(BlockID.INFESTED_CRACKED_STONE_BRICKS, BlockInfestedCrackedStoneBricks::class.java)
            register(BlockID.INFESTED_DEEPSLATE, BlockInfestedDeepslate::class.java)
            register(BlockID.INFESTED_MOSSY_STONE_BRICKS, BlockInfestedMossyStoneBricks::class.java)
            register(BlockID.INFESTED_STONE, BlockInfestedStone::class.java)
            register(BlockID.INFESTED_STONE_BRICKS, BlockInfestedStoneBricks::class.java)
            register(BlockID.INFO_UPDATE, BlockInfoUpdate::class.java)
            register(BlockID.INFO_UPDATE2, BlockInfoUpdate2::class.java)
            register(BlockID.INVISIBLE_BEDROCK, BlockInvisibleBedrock::class.java)
            register(BlockID.IRON_BARS, BlockIronBars::class.java)
            register(BlockID.IRON_BLOCK, BlockIronBlock::class.java)
            register(BlockID.IRON_DOOR, BlockIronDoor::class.java)
            register(BlockID.IRON_ORE, BlockIronOre::class.java)
            register(BlockID.IRON_TRAPDOOR, BlockIronTrapdoor::class.java)
            register(BlockID.JIGSAW, BlockJigsaw::class.java)
            register(BlockID.JUKEBOX, BlockJukebox::class.java)
            register(BlockID.JUNGLE_BUTTON, BlockJungleButton::class.java)
            register(BlockID.JUNGLE_DOOR, BlockJungleDoor::class.java)
            register(BlockID.JUNGLE_DOUBLE_SLAB, BlockJungleDoubleSlab::class.java)
            register(BlockID.JUNGLE_FENCE, BlockJungleFence::class.java)
            register(BlockID.JUNGLE_FENCE_GATE, BlockJungleFenceGate::class.java)
            register(BlockID.JUNGLE_HANGING_SIGN, BlockJungleHangingSign::class.java)
            register(BlockID.JUNGLE_LEAVES, BlockJungleLeaves::class.java)
            register(BlockID.JUNGLE_LOG, BlockJungleLog::class.java)
            register(BlockID.JUNGLE_PLANKS, BlockJunglePlanks::class.java)
            register(BlockID.JUNGLE_PRESSURE_PLATE, BlockJunglePressurePlate::class.java)
            register(BlockID.JUNGLE_SAPLING, BlockJungleSapling::class.java)
            register(BlockID.JUNGLE_SLAB, BlockJungleSlab::class.java)
            register(BlockID.JUNGLE_STAIRS, BlockJungleStairs::class.java)
            register(BlockID.JUNGLE_STANDING_SIGN, BlockJungleStandingSign::class.java)
            register(BlockID.JUNGLE_TRAPDOOR, BlockJungleTrapdoor::class.java)
            register(BlockID.JUNGLE_WALL_SIGN, BlockJungleWallSign::class.java)
            register(BlockID.JUNGLE_WOOD, BlockJungleWood::class.java)
            register(BlockID.KELP, BlockKelp::class.java)
            register(BlockID.LADDER, BlockLadder::class.java)
            register(BlockID.LANTERN, BlockLantern::class.java)
            register(BlockID.LAPIS_BLOCK, BlockLapisBlock::class.java)
            register(BlockID.LAPIS_ORE, BlockLapisOre::class.java)
            register(BlockID.LARGE_AMETHYST_BUD, BlockLargeAmethystBud::class.java)
            register(BlockID.LARGE_FERN, BlockLargeFern::class.java)
            register(BlockID.LAVA, BlockLava::class.java)
            register(BlockID.LECTERN, BlockLectern::class.java)
            register(BlockID.LEVER, BlockLever::class.java)
            register(BlockID.LIGHT_BLOCK_0, BlockLightBlock0::class.java)
            register(BlockID.LIGHT_BLOCK_1, BlockLightBlock1::class.java)
            register(BlockID.LIGHT_BLOCK_2, BlockLightBlock2::class.java)
            register(BlockID.LIGHT_BLOCK_3, BlockLightBlock3::class.java)
            register(BlockID.LIGHT_BLOCK_4, BlockLightBlock4::class.java)
            register(BlockID.LIGHT_BLOCK_5, BlockLightBlock5::class.java)
            register(BlockID.LIGHT_BLOCK_6, BlockLightBlock6::class.java)
            register(BlockID.LIGHT_BLOCK_7, BlockLightBlock7::class.java)
            register(BlockID.LIGHT_BLOCK_8, BlockLightBlock8::class.java)
            register(BlockID.LIGHT_BLOCK_9, BlockLightBlock9::class.java)
            register(BlockID.LIGHT_BLOCK_10, BlockLightBlock10::class.java)
            register(BlockID.LIGHT_BLOCK_11, BlockLightBlock11::class.java)
            register(BlockID.LIGHT_BLOCK_12, BlockLightBlock12::class.java)
            register(BlockID.LIGHT_BLOCK_13, BlockLightBlock13::class.java)
            register(BlockID.LIGHT_BLOCK_14, BlockLightBlock14::class.java)
            register(BlockID.LIGHT_BLOCK_15, BlockLightBlock15::class.java)
            register(BlockID.LIGHT_BLUE_CANDLE, BlockLightBlueCandle::class.java)
            register(BlockID.LIGHT_BLUE_CANDLE_CAKE, BlockLightBlueCandleCake::class.java)
            register(BlockID.LIGHT_BLUE_CARPET, BlockLightBlueCarpet::class.java)
            register(BlockID.LIGHT_BLUE_CONCRETE, BlockLightBlueConcrete::class.java)
            register(BlockID.LIGHT_BLUE_CONCRETE_POWDER, BlockLightBlueConcretePowder::class.java)
            register(BlockID.LIGHT_BLUE_GLAZED_TERRACOTTA, BlockLightBlueGlazedTerracotta::class.java)
            register(BlockID.LIGHT_BLUE_SHULKER_BOX, BlockLightBlueShulkerBox::class.java)
            register(BlockID.LIGHT_BLUE_STAINED_GLASS, BlockLightBlueStainedGlass::class.java)
            register(BlockID.LIGHT_BLUE_STAINED_GLASS_PANE, BlockLightBlueStainedGlassPane::class.java)
            register(BlockID.LIGHT_BLUE_TERRACOTTA, BlockLightBlueTerracotta::class.java)
            register(BlockID.LIGHT_BLUE_WOOL, BlockLightBlueWool::class.java)
            register(BlockID.LIGHT_GRAY_CANDLE, BlockLightGrayCandle::class.java)
            register(BlockID.LIGHT_GRAY_CANDLE_CAKE, BlockLightGrayCandleCake::class.java)
            register(BlockID.LIGHT_GRAY_CARPET, BlockLightGrayCarpet::class.java)
            register(BlockID.LIGHT_GRAY_CONCRETE, BlockLightGrayConcrete::class.java)
            register(BlockID.LIGHT_GRAY_CONCRETE_POWDER, BlockLightGrayConcretePowder::class.java)
            register(BlockID.LIGHT_GRAY_SHULKER_BOX, BlockLightGrayShulkerBox::class.java)
            register(BlockID.LIGHT_GRAY_STAINED_GLASS, BlockLightGrayStainedGlass::class.java)
            register(BlockID.LIGHT_GRAY_STAINED_GLASS_PANE, BlockLightGrayStainedGlassPane::class.java)
            register(BlockID.LIGHT_GRAY_TERRACOTTA, BlockLightGrayTerracotta::class.java)
            register(BlockID.LIGHT_GRAY_WOOL, BlockLightGrayWool::class.java)
            register(BlockID.LIGHT_WEIGHTED_PRESSURE_PLATE, BlockLightWeightedPressurePlate::class.java)
            register(BlockID.LIGHTNING_ROD, BlockLightningRod::class.java)
            register(BlockID.LILAC, BlockLilac::class.java)
            register(BlockID.LILY_OF_THE_VALLEY, BlockLilyOfTheValley::class.java)
            register(BlockID.LIME_CANDLE, BlockLimeCandle::class.java)
            register(BlockID.LIME_CANDLE_CAKE, BlockLimeCandleCake::class.java)
            register(BlockID.LIME_CARPET, BlockLimeCarpet::class.java)
            register(BlockID.LIME_CONCRETE, BlockLimeConcrete::class.java)
            register(BlockID.LIME_CONCRETE_POWDER, BlockLimeConcretePowder::class.java)
            register(BlockID.LIME_GLAZED_TERRACOTTA, BlockLimeGlazedTerracotta::class.java)
            register(BlockID.LIME_SHULKER_BOX, BlockLimeShulkerBox::class.java)
            register(BlockID.LIME_STAINED_GLASS, BlockLimeStainedGlass::class.java)
            register(BlockID.LIME_STAINED_GLASS_PANE, BlockLimeStainedGlassPane::class.java)
            register(BlockID.LIME_TERRACOTTA, BlockLimeTerracotta::class.java)
            register(BlockID.LIME_WOOL, BlockLimeWool::class.java)
            register(BlockID.LIT_BLAST_FURNACE, BlockLitBlastFurnace::class.java)
            register(BlockID.LIT_DEEPSLATE_REDSTONE_ORE, BlockLitDeepslateRedstoneOre::class.java)
            register(BlockID.LIT_FURNACE, BlockLitFurnace::class.java)
            register(BlockID.LIT_PUMPKIN, BlockLitPumpkin::class.java)
            register(BlockID.LIT_REDSTONE_LAMP, BlockLitRedstoneLamp::class.java)
            register(BlockID.LIT_REDSTONE_ORE, BlockLitRedstoneOre::class.java)
            register(BlockID.LIT_SMOKER, BlockLitSmoker::class.java)
            register(BlockID.LODESTONE, BlockLodestone::class.java)
            register(BlockID.LOOM, BlockLoom::class.java)
            register(BlockID.MAGENTA_CANDLE, BlockMagentaCandle::class.java)
            register(BlockID.MAGENTA_CANDLE_CAKE, BlockMagentaCandleCake::class.java)
            register(BlockID.MAGENTA_CARPET, BlockMagentaCarpet::class.java)
            register(BlockID.MAGENTA_CONCRETE, BlockMagentaConcrete::class.java)
            register(BlockID.MAGENTA_CONCRETE_POWDER, BlockMagentaConcretePowder::class.java)
            register(BlockID.MAGENTA_GLAZED_TERRACOTTA, BlockMagentaGlazedTerracotta::class.java)
            register(BlockID.MAGENTA_SHULKER_BOX, BlockMagentaShulkerBox::class.java)
            register(BlockID.MAGENTA_STAINED_GLASS, BlockMagentaStainedGlass::class.java)
            register(BlockID.MAGENTA_STAINED_GLASS_PANE, BlockMagentaStainedGlassPane::class.java)
            register(BlockID.MAGENTA_TERRACOTTA, BlockMagentaTerracotta::class.java)
            register(BlockID.MAGENTA_WOOL, BlockMagentaWool::class.java)
            register(BlockID.MAGMA, BlockMagma::class.java)
            register(BlockID.MANGROVE_BUTTON, BlockMangroveButton::class.java)
            register(BlockID.MANGROVE_DOOR, BlockMangroveDoor::class.java)
            register(BlockID.MANGROVE_DOUBLE_SLAB, BlockMangroveDoubleSlab::class.java)
            register(BlockID.MANGROVE_FENCE, BlockMangroveFence::class.java)
            register(BlockID.MANGROVE_FENCE_GATE, BlockMangroveFenceGate::class.java)
            register(BlockID.MANGROVE_HANGING_SIGN, BlockMangroveHangingSign::class.java)
            register(BlockID.MANGROVE_LEAVES, BlockMangroveLeaves::class.java)
            register(BlockID.MANGROVE_LOG, BlockMangroveLog::class.java)
            register(BlockID.MANGROVE_PLANKS, BlockMangrovePlanks::class.java)
            register(BlockID.MANGROVE_PRESSURE_PLATE, BlockMangrovePressurePlate::class.java)
            register(BlockID.MANGROVE_PROPAGULE, BlockMangrovePropagule::class.java)
            register(BlockID.MANGROVE_ROOTS, BlockMangroveRoots::class.java)
            register(BlockID.MANGROVE_SLAB, BlockMangroveSlab::class.java)
            register(BlockID.MANGROVE_STAIRS, BlockMangroveStairs::class.java)
            register(BlockID.MANGROVE_STANDING_SIGN, BlockMangroveStandingSign::class.java)
            register(BlockID.MANGROVE_TRAPDOOR, BlockMangroveTrapdoor::class.java)
            register(BlockID.MANGROVE_WALL_SIGN, BlockMangroveWallSign::class.java)
            register(BlockID.MANGROVE_WOOD, BlockMangroveWood::class.java)
            register(BlockID.MEDIUM_AMETHYST_BUD, BlockMediumAmethystBud::class.java)
            register(BlockID.MELON_BLOCK, BlockMelonBlock::class.java)
            register(BlockID.MELON_STEM, BlockMelonStem::class.java)
            register(BlockID.MOB_SPAWNER, BlockMobSpawner::class.java)
            //register(MONSTER_EGG, BlockMonsterEgg.class);
            register(BlockID.MOSS_BLOCK, BlockMossBlock::class.java)
            register(BlockID.MOSS_CARPET, BlockMossCarpet::class.java)
            register(BlockID.MOSSY_COBBLESTONE, BlockMossyCobblestone::class.java)
            register(BlockID.MOSSY_COBBLESTONE_DOUBLE_SLAB, BlockMossyCobblestoneDoubleSlab::class.java)
            register(BlockID.MOSSY_COBBLESTONE_SLAB, BlockMossyCobblestoneSlab::class.java)
            register(BlockID.MOSSY_COBBLESTONE_STAIRS, BlockMossyCobblestoneStairs::class.java)
            register(BlockID.MOSSY_COBBLESTONE_WALL, BlockMossyCobblestoneWall::class.java)
            register(BlockID.MOSSY_STONE_BRICK_DOUBLE_SLAB, BlockMossyStoneBrickDoubleSlab::class.java)
            register(BlockID.MOSSY_STONE_BRICK_SLAB, BlockMossyStoneBrickSlab::class.java)
            register(BlockID.MOSSY_STONE_BRICKS, BlockMossyStoneBricks::class.java)
            register(BlockID.MOSSY_STONE_BRICK_STAIRS, BlockMossyStoneBrickStairs::class.java)
            register(BlockID.MOSSY_STONE_BRICK_WALL, BlockMossyStoneBrickWall::class.java)
            register(BlockID.MOVING_BLOCK, BlockMovingBlock::class.java)
            register(BlockID.MUD, BlockMud::class.java)
            register(BlockID.MUD_BRICK_DOUBLE_SLAB, BlockMudBrickDoubleSlab::class.java)
            register(BlockID.MUD_BRICK_SLAB, BlockMudBrickSlab::class.java)
            register(BlockID.MUD_BRICK_STAIRS, BlockMudBrickStairs::class.java)
            register(BlockID.MUD_BRICK_WALL, BlockMudBrickWall::class.java)
            register(BlockID.MUD_BRICKS, BlockMudBricks::class.java)
            register(BlockID.MUDDY_MANGROVE_ROOTS, BlockMuddyMangroveRoots::class.java)
            register(BlockID.MUSHROOM_STEM, BlockMushroomStem::class.java)
            register(BlockID.MYCELIUM, BlockMycelium::class.java)
            register(BlockID.NETHER_BRICK, BlockNetherBrick::class.java)
            register(BlockID.NETHER_BRICK_DOUBLE_SLAB, BlockNetherBrickDoubleSlab::class.java)
            register(BlockID.NETHER_BRICK_FENCE, BlockNetherBrickFence::class.java)
            register(BlockID.NETHER_BRICK_SLAB, BlockNetherBrickSlab::class.java)
            register(BlockID.NETHER_BRICK_STAIRS, BlockNetherBrickStairs::class.java)
            register(BlockID.NETHER_BRICK_WALL, BlockNetherBrickWall::class.java)
            register(BlockID.NETHER_GOLD_ORE, BlockNetherGoldOre::class.java)
            register(BlockID.NETHER_SPROUTS, BlockNetherSprouts::class.java)
            register(BlockID.NETHER_WART, BlockNetherWart::class.java)
            register(BlockID.NETHER_WART_BLOCK, BlockNetherWartBlock::class.java)
            register(BlockID.NETHERITE_BLOCK, BlockNetheriteBlock::class.java)
            register(BlockID.NETHERRACK, BlockNetherrack::class.java)
            register(BlockID.NETHERREACTOR, BlockNetherreactor::class.java)
            register(BlockID.NORMAL_STONE_SLAB, BlockNormalStoneSlab::class.java)
            register(BlockID.NORMAL_STONE_DOUBLE_SLAB, BlockNormalStoneDoubleSlab::class.java)
            register(BlockID.NORMAL_STONE_STAIRS, BlockNormalStoneStairs::class.java)
            register(BlockID.NOTEBLOCK, BlockNoteblock::class.java)
            register(BlockID.OAK_DOUBLE_SLAB, BlockOakDoubleSlab::class.java)
            register(BlockID.OAK_FENCE, BlockOakFence::class.java)
            register(BlockID.OAK_HANGING_SIGN, BlockOakHangingSign::class.java)
            register(BlockID.OAK_LEAVES, BlockOakLeaves::class.java)
            register(BlockID.OAK_LOG, BlockOakLog::class.java)
            register(BlockID.OAK_PLANKS, BlockOakPlanks::class.java)
            register(BlockID.OAK_SAPLING, BlockOakSapling::class.java)
            register(BlockID.OAK_SLAB, BlockOakSlab::class.java)
            register(BlockID.OAK_STAIRS, BlockOakStairs::class.java)
            register(BlockID.OAK_WOOD, BlockOakWood::class.java)
            register(BlockID.OBSERVER, BlockObserver::class.java)
            register(BlockID.OBSIDIAN, BlockObsidian::class.java)
            register(BlockID.OCHRE_FROGLIGHT, BlockOchreFroglight::class.java)
            register(BlockID.OPEN_EYEBLOSSOM, BlockOpenEyeblossom::class.java)
            register(BlockID.ORANGE_CANDLE, BlockOrangeCandle::class.java)
            register(BlockID.ORANGE_CANDLE_CAKE, BlockOrangeCandleCake::class.java)
            register(BlockID.ORANGE_CARPET, BlockOrangeCarpet::class.java)
            register(BlockID.ORANGE_CONCRETE, BlockOrangeConcrete::class.java)
            register(BlockID.ORANGE_CONCRETE_POWDER, BlockOrangeConcretePowder::class.java)
            register(BlockID.ORANGE_GLAZED_TERRACOTTA, BlockOrangeGlazedTerracotta::class.java)
            register(BlockID.ORANGE_SHULKER_BOX, BlockOrangeShulkerBox::class.java)
            register(BlockID.ORANGE_STAINED_GLASS, BlockOrangeStainedGlass::class.java)
            register(BlockID.ORANGE_STAINED_GLASS_PANE, BlockOrangeStainedGlassPane::class.java)
            register(BlockID.ORANGE_TERRACOTTA, BlockOrangeTerracotta::class.java)
            register(BlockID.ORANGE_TULIP, BlockOrangeTulip::class.java)
            register(BlockID.ORANGE_WOOL, BlockOrangeWool::class.java)
            register(BlockID.OXEYE_DAISY, BlockOxeyeDaisy::class.java)
            register(BlockID.OXIDIZED_CHISELED_COPPER, BlockOxidizedChiseledCopper::class.java)
            register(BlockID.OXIDIZED_COPPER, BlockOxidizedCopper::class.java)
            register(BlockID.OXIDIZED_COPPER_BULB, BlockOxidizedCopperBulb::class.java)
            register(BlockID.OXIDIZED_COPPER_DOOR, BlockOxidizedCopperDoor::class.java)
            register(BlockID.OXIDIZED_COPPER_GRATE, BlockOxidizedCopperGrate::class.java)
            register(BlockID.OXIDIZED_COPPER_TRAPDOOR, BlockOxidizedCopperTrapdoor::class.java)
            register(BlockID.OXIDIZED_CUT_COPPER, BlockOxidizedCutCopper::class.java)
            register(BlockID.OXIDIZED_CUT_COPPER_SLAB, BlockOxidizedCutCopperSlab::class.java)
            register(BlockID.OXIDIZED_CUT_COPPER_STAIRS, BlockOxidizedCutCopperStairs::class.java)
            register(BlockID.OXIDIZED_DOUBLE_CUT_COPPER_SLAB, BlockOxidizedDoubleCutCopperSlab::class.java)
            register(BlockID.PACKED_ICE, BlockPackedIce::class.java)
            register(BlockID.PACKED_MUD, BlockPackedMud::class.java)
            register(BlockID.PALE_HANGING_MOSS, BlockPaleHangingMoss::class.java)
            register(BlockID.PALE_MOSS_BLOCK, BlockPaleMossBlock::class.java)
            register(BlockID.PALE_MOSS_CARPET, BlockPaleMossCarpet::class.java)
            register(BlockID.PALE_OAK_BUTTON, BlockPaleOakButton::class.java)
            register(BlockID.PALE_OAK_DOOR, BlockPaleOakDoor::class.java)
            register(BlockID.PALE_OAK_FENCE, BlockPaleOakFence::class.java)
            register(BlockID.PALE_OAK_FENCE_GATE, BlockPaleOakFenceGate::class.java)
            register(BlockID.PALE_OAK_HANGING_SIGN, BlockPaleOakHangingSign::class.java)
            register(BlockID.PALE_OAK_LEAVES, BlockPaleOakLeaves::class.java)
            register(BlockID.PALE_OAK_LOG, BlockPaleOakLog::class.java)
            register(BlockID.PALE_OAK_DOUBLE_SLAB, BlockPaleOakDoubleSlab::class.java)
            register(BlockID.PALE_OAK_PLANKS, BlockPaleOakPlanks::class.java)
            register(BlockID.PALE_OAK_PRESSURE_PLATE, BlockPaleOakPressurePlate::class.java)
            register(BlockID.PALE_OAK_SAPLING, BlockPaleOakSapling::class.java)
            register(BlockID.PALE_OAK_SLAB, BlockPaleOakSlab::class.java)
            register(BlockID.PALE_OAK_STAIRS, BlockPaleOakStairs::class.java)
            register(BlockID.PALE_OAK_STANDING_SIGN, BlockPaleOakStandingSign::class.java)
            register(BlockID.PALE_OAK_TRAPDOOR, BlockPaleOakTrapdoor::class.java)
            register(BlockID.PALE_OAK_WALL_SIGN, BlockPaleOakWallSign::class.java)
            register(BlockID.PALE_OAK_WOOD, BlockPaleOakWood::class.java)
            register(BlockID.PEARLESCENT_FROGLIGHT, BlockPearlescentFroglight::class.java)
            register(BlockID.PEONY, BlockPeony::class.java)
            register(BlockID.PETRIFIED_OAK_DOUBLE_SLAB, BlockPetrifiedOakDoubleSlab::class.java)
            register(BlockID.PETRIFIED_OAK_SLAB, BlockPetrifiedOakSlab::class.java)
            register(BlockID.PIGLIN_HEAD, BlockPiglinHead::class.java)
            register(BlockID.PINK_CANDLE, BlockPinkCandle::class.java)
            register(BlockID.PINK_CANDLE_CAKE, BlockPinkCandleCake::class.java)
            register(BlockID.PINK_CARPET, BlockPinkCarpet::class.java)
            register(BlockID.PINK_CONCRETE, BlockPinkConcrete::class.java)
            register(BlockID.PINK_CONCRETE_POWDER, BlockPinkConcretePowder::class.java)
            register(BlockID.PINK_GLAZED_TERRACOTTA, BlockPinkGlazedTerracotta::class.java)
            register(BlockID.PINK_PETALS, BlockPinkPetals::class.java)
            register(BlockID.PINK_SHULKER_BOX, BlockPinkShulkerBox::class.java)
            register(BlockID.PINK_STAINED_GLASS, BlockPinkStainedGlass::class.java)
            register(BlockID.PINK_STAINED_GLASS_PANE, BlockPinkStainedGlassPane::class.java)
            register(BlockID.PINK_TERRACOTTA, BlockPinkTerracotta::class.java)
            register(BlockID.PINK_TULIP, BlockPinkTulip::class.java)
            register(BlockID.PINK_WOOL, BlockPinkWool::class.java)
            register(BlockID.PISTON, BlockPiston::class.java)
            register(BlockID.PISTON_ARM_COLLISION, BlockPistonArmCollision::class.java)
            register(BlockID.PITCHER_CROP, BlockPitcherCrop::class.java)
            register(BlockID.PITCHER_PLANT, BlockPitcherPlant::class.java)
            register(BlockID.PLAYER_HEAD, BlockPlayerHead::class.java)
            register(BlockID.PODZOL, BlockPodzol::class.java)
            register(BlockID.POINTED_DRIPSTONE, BlockPointedDripstone::class.java)
            register(BlockID.POLISHED_ANDESITE, BlockPolishedAndesite::class.java)
            register(BlockID.POLISHED_ANDESITE_DOUBLE_SLAB, BlockPolishedAndesiteDoubleSlab::class.java)
            register(BlockID.POLISHED_ANDESITE_SLAB, BlockPolishedAndesiteSlab::class.java)
            register(BlockID.POLISHED_ANDESITE_STAIRS, BlockPolishedAndesiteStairs::class.java)
            register(BlockID.POLISHED_BASALT, BlockPolishedBasalt::class.java)
            register(BlockID.POLISHED_BLACKSTONE, BlockPolishedBlackstone::class.java)
            register(BlockID.POLISHED_BLACKSTONE_BRICK_DOUBLE_SLAB, BlockPolishedBlackstoneBrickDoubleSlab::class.java)
            register(BlockID.POLISHED_BLACKSTONE_BRICK_SLAB, BlockPolishedBlackstoneBrickSlab::class.java)
            register(BlockID.POLISHED_BLACKSTONE_BRICK_STAIRS, BlockPolishedBlackstoneBrickStairs::class.java)
            register(BlockID.POLISHED_BLACKSTONE_BRICK_WALL, BlockPolishedBlackstoneBrickWall::class.java)
            register(BlockID.POLISHED_BLACKSTONE_BRICKS, BlockPolishedBlackstoneBricks::class.java)
            register(BlockID.POLISHED_BLACKSTONE_BUTTON, BlockPolishedBlackstoneButton::class.java)
            register(BlockID.POLISHED_BLACKSTONE_DOUBLE_SLAB, BlockPolishedBlackstoneDoubleSlab::class.java)
            register(BlockID.POLISHED_BLACKSTONE_PRESSURE_PLATE, BlockPolishedBlackstonePressurePlate::class.java)
            register(BlockID.POLISHED_BLACKSTONE_SLAB, BlockPolishedBlackstoneSlab::class.java)
            register(BlockID.POLISHED_BLACKSTONE_STAIRS, BlockPolishedBlackstoneStairs::class.java)
            register(BlockID.POLISHED_BLACKSTONE_WALL, BlockPolishedBlackstoneWall::class.java)
            register(BlockID.POLISHED_DEEPSLATE, BlockPolishedDeepslate::class.java)
            register(BlockID.POLISHED_DEEPSLATE_DOUBLE_SLAB, BlockPolishedDeepslateDoubleSlab::class.java)
            register(BlockID.POLISHED_DEEPSLATE_SLAB, BlockPolishedDeepslateSlab::class.java)
            register(BlockID.POLISHED_DEEPSLATE_STAIRS, BlockPolishedDeepslateStairs::class.java)
            register(BlockID.POLISHED_DEEPSLATE_WALL, BlockPolishedDeepslateWall::class.java)
            register(BlockID.POLISHED_DIORITE, BlockPolishedDiorite::class.java)
            register(BlockID.POLISHED_DIORITE_DOUBLE_SLAB, BlockPolishedDioriteDoubleSlab::class.java)
            register(BlockID.POLISHED_DIORITE_SLAB, BlockPolishedDioriteSlab::class.java)
            register(BlockID.POLISHED_DIORITE_STAIRS, BlockPolishedDioriteStairs::class.java)
            register(BlockID.POLISHED_GRANITE, BlockPolishedGranite::class.java)
            register(BlockID.POLISHED_GRANITE_DOUBLE_SLAB, BlockPolishedGraniteDoubleSlab::class.java)
            register(BlockID.POLISHED_GRANITE_SLAB, BlockPolishedGraniteSlab::class.java)
            register(BlockID.POLISHED_GRANITE_STAIRS, BlockPolishedGraniteStairs::class.java)
            register(BlockID.POLISHED_TUFF, BlockPolishedTuff::class.java)
            register(BlockID.POLISHED_TUFF_DOUBLE_SLAB, BlockPolishedTuffDoubleSlab::class.java)
            register(BlockID.POLISHED_TUFF_SLAB, BlockPolishedTuffSlab::class.java)
            register(BlockID.POLISHED_TUFF_STAIRS, BlockPolishedTuffStairs::class.java)
            register(BlockID.POLISHED_TUFF_WALL, BlockPolishedTuffWall::class.java)
            register(BlockID.POPPY, BlockPoppy::class.java)
            register(BlockID.PORTAL, BlockPortal::class.java)
            register(BlockID.POTATOES, BlockPotatoes::class.java)
            register(BlockID.POWDER_SNOW, BlockPowderSnow::class.java)
            register(BlockID.POWERED_COMPARATOR, BlockPoweredComparator::class.java)
            register(BlockID.POWERED_REPEATER, BlockPoweredRepeater::class.java)
            register(BlockID.PRISMARINE, BlockPrismarine::class.java)
            register(BlockID.PRISMARINE_BRICK_DOUBLE_SLAB, BlockPrismarineBrickDoubleSlab::class.java)
            register(BlockID.PRISMARINE_BRICK_SLAB, BlockPrismarineBrickSlab::class.java)
            register(BlockID.PRISMARINE_BRICKS, BlockPrismarineBricks::class.java)
            register(BlockID.PRISMARINE_BRICKS_STAIRS, BlockPrismarineBricksStairs::class.java)
            register(BlockID.PRISMARINE_DOUBLE_SLAB, BlockPrismarineDoubleSlab::class.java)
            register(BlockID.PRISMARINE_SLAB, BlockPrismarineSlab::class.java)
            register(BlockID.PRISMARINE_STAIRS, BlockPrismarineStairs::class.java)
            register(BlockID.PRISMARINE_WALL, BlockPrismarineWall::class.java)
            register(BlockID.PUMPKIN, BlockPumpkin::class.java)
            register(BlockID.PUMPKIN_STEM, BlockPumpkinStem::class.java)
            register(BlockID.PURPLE_CANDLE, BlockPurpleCandle::class.java)
            register(BlockID.PURPLE_CANDLE_CAKE, BlockPurpleCandleCake::class.java)
            register(BlockID.PURPLE_CARPET, BlockPurpleCarpet::class.java)
            register(BlockID.PURPLE_CONCRETE, BlockPurpleConcrete::class.java)
            register(BlockID.PURPLE_CONCRETE_POWDER, BlockPurpleConcretePowder::class.java)
            register(BlockID.PURPLE_GLAZED_TERRACOTTA, BlockPurpleGlazedTerracotta::class.java)
            register(BlockID.PURPLE_SHULKER_BOX, BlockPurpleShulkerBox::class.java)
            register(BlockID.PURPLE_STAINED_GLASS, BlockPurpleStainedGlass::class.java)
            register(BlockID.PURPLE_STAINED_GLASS_PANE, BlockPurpleStainedGlassPane::class.java)
            register(BlockID.PURPLE_TERRACOTTA, BlockPurpleTerracotta::class.java)
            register(BlockID.PURPLE_WOOL, BlockPurpleWool::class.java)
            register(BlockID.PURPUR_BLOCK, BlockPurpurBlock::class.java)
            register(BlockID.PURPUR_DOUBLE_SLAB, BlockPurpurDoubleSlab::class.java)
            register(BlockID.PURPUR_PILLAR, BlockPurpurPillar::class.java)
            register(BlockID.PURPUR_SLAB, BlockPurpurSlab::class.java)
            register(BlockID.PURPUR_STAIRS, BlockPurpurStairs::class.java)
            register(BlockID.QUARTZ_BLOCK, BlockQuartzBlock::class.java)
            register(BlockID.QUARTZ_BRICKS, BlockQuartzBricks::class.java)
            register(BlockID.QUARTZ_DOUBLE_SLAB, BlockQuartzDoubleSlab::class.java)
            register(BlockID.QUARTZ_ORE, BlockQuartzOre::class.java)
            register(BlockID.QUARTZ_PILLAR, BlockQuartzPillar::class.java)
            register(BlockID.QUARTZ_SLAB, BlockQuartzSlab::class.java)
            register(BlockID.QUARTZ_STAIRS, BlockQuartzStairs::class.java)
            register(BlockID.RAIL, BlockRail::class.java)
            register(BlockID.RAW_COPPER_BLOCK, BlockRawCopperBlock::class.java)
            register(BlockID.RAW_GOLD_BLOCK, BlockRawGoldBlock::class.java)
            register(BlockID.RAW_IRON_BLOCK, BlockRawIronBlock::class.java)
            register(BlockID.RED_CANDLE, BlockRedCandle::class.java)
            register(BlockID.RED_CANDLE_CAKE, BlockRedCandleCake::class.java)
            register(BlockID.RED_CARPET, BlockRedCarpet::class.java)
            register(BlockID.RED_CONCRETE, BlockRedConcrete::class.java)
            register(BlockID.RED_CONCRETE_POWDER, BlockRedConcretePowder::class.java)
            register(BlockID.RED_GLAZED_TERRACOTTA, BlockRedGlazedTerracotta::class.java)
            register(BlockID.RED_MUSHROOM, BlockRedMushroom::class.java)
            register(BlockID.RED_MUSHROOM_BLOCK, BlockRedMushroomBlock::class.java)
            register(BlockID.RED_NETHER_BRICK, BlockRedNetherBrick::class.java)
            register(BlockID.RED_NETHER_BRICK_DOUBLE_SLAB, BlockRedNetherBrickDoubleSlab::class.java)
            register(BlockID.RED_NETHER_BRICK_SLAB, BlockRedNetherBrickSlab::class.java)
            register(BlockID.RED_NETHER_BRICK_STAIRS, BlockRedNetherBrickStairs::class.java)
            register(BlockID.RED_NETHER_BRICK_WALL, BlockRedNetherBrickWall::class.java)
            register(BlockID.RED_SAND, BlockRedSand::class.java)
            register(BlockID.RED_SANDSTONE, BlockRedSandstone::class.java)
            register(BlockID.RED_SANDSTONE_DOUBLE_SLAB, BlockRedSandstoneDoubleSlab::class.java)
            register(BlockID.RED_SANDSTONE_SLAB, BlockRedSandstoneSlab::class.java)
            register(BlockID.RED_SANDSTONE_STAIRS, BlockRedSandstoneStairs::class.java)
            register(BlockID.RED_SANDSTONE_WALL, BlockRedSandstoneWall::class.java)
            register(BlockID.RED_SHULKER_BOX, BlockRedShulkerBox::class.java)
            register(BlockID.RED_STAINED_GLASS, BlockRedStainedGlass::class.java)
            register(BlockID.RED_STAINED_GLASS_PANE, BlockRedStainedGlassPane::class.java)
            register(BlockID.RED_TERRACOTTA, BlockRedTerracotta::class.java)
            register(BlockID.RED_TULIP, BlockRedTulip::class.java)
            register(BlockID.RED_WOOL, BlockRedWool::class.java)
            register(BlockID.REDSTONE_BLOCK, BlockRedstoneBlock::class.java)
            register(BlockID.REDSTONE_LAMP, BlockRedstoneLamp::class.java)
            register(BlockID.REDSTONE_ORE, BlockRedstoneOre::class.java)
            register(BlockID.REDSTONE_TORCH, BlockRedstoneTorch::class.java)
            register(BlockID.REDSTONE_WIRE, BlockRedstoneWire::class.java)
            register(BlockID.REEDS, BlockReeds::class.java)
            register(BlockID.REINFORCED_DEEPSLATE, BlockReinforcedDeepslate::class.java)
            register(BlockID.REPEATING_COMMAND_BLOCK, BlockRepeatingCommandBlock::class.java)
            register(BlockID.RESERVED6, BlockReserved6::class.java)
            register(BlockID.RESIN_BLOCK, BlockResin::class.java)
            register(BlockID.RESIN_BRICKS, BlockResinBricks::class.java)
            register(BlockID.RESIN_BRICK_DOUBLE_SLAB, BlockResinBrickDoubleSlab::class.java)
            register(BlockID.RESIN_BRICK_SLAB, BlockResinBrickSlab::class.java)
            register(BlockID.RESIN_BRICK_STAIRS, BlockResinBrickStairs::class.java)
            register(BlockID.RESIN_BRICK_WALL, BlockResinBrickWall::class.java)
            register(BlockID.RESIN_CLUMP, BlockResinClump::class.java)
            register(BlockID.RESPAWN_ANCHOR, BlockRespawnAnchor::class.java)
            register(BlockID.ROSE_BUSH, BlockRoseBush::class.java)
            register(BlockID.SAND, BlockSand::class.java)
            register(BlockID.SANDSTONE, BlockSandstone::class.java)
            register(BlockID.SANDSTONE_DOUBLE_SLAB, BlockSandstoneDoubleSlab::class.java)
            register(BlockID.SANDSTONE_SLAB, BlockSandstoneSlab::class.java)
            register(BlockID.SANDSTONE_STAIRS, BlockSandstoneStairs::class.java)
            register(BlockID.SANDSTONE_WALL, BlockSandstoneWall::class.java)
            register(BlockID.SCAFFOLDING, BlockScaffolding::class.java)
            register(BlockID.SCULK, BlockSculk::class.java)
            register(BlockID.SCULK_CATALYST, BlockSculkCatalyst::class.java)
            register(BlockID.SCULK_SENSOR, BlockSculkSensor::class.java)
            register(BlockID.SCULK_SHRIEKER, BlockSculkShrieker::class.java)
            register(BlockID.SCULK_VEIN, BlockSculkVein::class.java)
            register(BlockID.SEA_LANTERN, BlockSeaLantern::class.java)
            register(BlockID.SEA_PICKLE, BlockSeaPickle::class.java)
            register(BlockID.SEAGRASS, BlockSeagrass::class.java)
            register(BlockID.SHORT_GRASS, BlockShortGrass::class.java)
            register(BlockID.SHROOMLIGHT, BlockShroomlight::class.java)
            register(BlockID.SILVER_GLAZED_TERRACOTTA, BlockSilverGlazedTerracotta::class.java)
            register(BlockID.SKELETON_SKULL, BlockSkeletonSkull::class.java)
            register(BlockID.SLIME, BlockSlime::class.java)
            register(BlockID.SMALL_AMETHYST_BUD, BlockSmallAmethystBud::class.java)
            register(BlockID.SMALL_DRIPLEAF_BLOCK, BlockSmallDripleafBlock::class.java)
            register(BlockID.SMITHING_TABLE, BlockSmithingTable::class.java)
            register(BlockID.SMOKER, BlockSmoker::class.java)
            register(BlockID.SMOOTH_BASALT, BlockSmoothBasalt::class.java)
            register(BlockID.SMOOTH_QUARTZ, BlockSmoothQuartz::class.java)
            register(BlockID.SMOOTH_QUARTZ_DOUBLE_SLAB, BlockSmoothQuartzDoubleSlab::class.java)
            register(BlockID.SMOOTH_QUARTZ_SLAB, BlockSmoothQuartzSlab::class.java)
            register(BlockID.SMOOTH_QUARTZ_STAIRS, BlockSmoothQuartzStairs::class.java)
            register(BlockID.SMOOTH_RED_SANDSTONE, BlockSmoothRedSandstone::class.java)
            register(BlockID.SMOOTH_RED_SANDSTONE_DOUBLE_SLAB, BlockSmoothRedSandstoneDoubleSlab::class.java)
            register(BlockID.SMOOTH_RED_SANDSTONE_SLAB, BlockSmoothRedSandstoneSlab::class.java)
            register(BlockID.SMOOTH_RED_SANDSTONE_STAIRS, BlockSmoothRedSandstoneStairs::class.java)
            register(BlockID.SMOOTH_SANDSTONE, BlockSmoothSandstone::class.java)
            register(BlockID.SMOOTH_SANDSTONE_DOUBLE_SLAB, BlockSmoothSandstoneDoubleSlab::class.java)
            register(BlockID.SMOOTH_SANDSTONE_SLAB, BlockSmoothSandstoneSlab::class.java)
            register(BlockID.SMOOTH_SANDSTONE_STAIRS, BlockSmoothSandstoneStairs::class.java)
            register(BlockID.SMOOTH_STONE, BlockSmoothStone::class.java)
            register(BlockID.SMOOTH_STONE_DOUBLE_SLAB, BlockSmoothStoneDoubleSlab::class.java)
            register(BlockID.SMOOTH_STONE_SLAB, BlockSmoothStoneSlab::class.java)
            register(BlockID.SNIFFER_EGG, BlockSnifferEgg::class.java)
            register(BlockID.SNOW, BlockSnow::class.java)
            register(BlockID.SNOW_LAYER, BlockSnowLayer::class.java)
            register(BlockID.SOUL_CAMPFIRE, BlockSoulCampfire::class.java)
            register(BlockID.SOUL_FIRE, BlockSoulFire::class.java)
            register(BlockID.SOUL_LANTERN, BlockSoulLantern::class.java)
            register(BlockID.SOUL_SAND, BlockSoulSand::class.java)
            register(BlockID.SOUL_SOIL, BlockSoulSoil::class.java)
            register(BlockID.SOUL_TORCH, BlockSoulTorch::class.java)
            register(BlockID.SPONGE, BlockSponge::class.java)
            register(BlockID.SPORE_BLOSSOM, BlockSporeBlossom::class.java)
            register(BlockID.SPRUCE_BUTTON, BlockSpruceButton::class.java)
            register(BlockID.SPRUCE_DOOR, BlockSpruceDoor::class.java)
            register(BlockID.SPRUCE_DOUBLE_SLAB, BlockSpruceDoubleSlab::class.java)
            register(BlockID.SPRUCE_FENCE, BlockSpruceFence::class.java)
            register(BlockID.SPRUCE_FENCE_GATE, BlockSpruceFenceGate::class.java)
            register(BlockID.SPRUCE_HANGING_SIGN, BlockSpruceHangingSign::class.java)
            register(BlockID.SPRUCE_LEAVES, BlockSpruceLeaves::class.java)
            register(BlockID.SPRUCE_LOG, BlockSpruceLog::class.java)
            register(BlockID.SPRUCE_PLANKS, BlockSprucePlanks::class.java)
            register(BlockID.SPRUCE_PRESSURE_PLATE, BlockSprucePressurePlate::class.java)
            register(BlockID.SPRUCE_SAPLING, BlockSpruceSapling::class.java)
            register(BlockID.SPRUCE_SLAB, BlockSpruceSlab::class.java)
            register(BlockID.SPRUCE_STAIRS, BlockSpruceStairs::class.java)
            register(BlockID.SPRUCE_STANDING_SIGN, BlockSpruceStandingSign::class.java)
            register(BlockID.SPRUCE_TRAPDOOR, BlockSpruceTrapdoor::class.java)
            register(BlockID.SPRUCE_WALL_SIGN, BlockSpruceWallSign::class.java)
            register(BlockID.SPRUCE_WOOD, BlockSpruceWood::class.java)
            register(BlockID.STANDING_BANNER, BlockStandingBanner::class.java)
            register(BlockID.STANDING_SIGN, BlockStandingSign::class.java)
            register(BlockID.STICKY_PISTON, BlockStickyPiston::class.java)
            register(BlockID.STICKY_PISTON_ARM_COLLISION, BlockStickyPistonArmCollision::class.java)
            register(BlockID.STONE, BlockStone::class.java)
            register(BlockID.STONE_BRICK_DOUBLE_SLAB, BlockStoneBrickDoubleSlab::class.java)
            register(BlockID.STONE_BRICK_SLAB, BlockStoneBrickSlab::class.java)
            register(BlockID.STONE_BRICK_STAIRS, BlockStoneBrickStairs::class.java)
            register(BlockID.STONE_BRICK_WALL, BlockStoneBrickWall::class.java)
            register(BlockID.STONE_BUTTON, BlockStoneButton::class.java)
            register(BlockID.STONE_PRESSURE_PLATE, BlockStonePressurePlate::class.java)
            register(BlockID.STONE_STAIRS, BlockStoneStairs::class.java)
            register(BlockID.STONE_BRICKS, BlockStoneBricks::class.java)
            register(BlockID.STONECUTTER, BlockStonecutter::class.java)
            register(BlockID.STONECUTTER_BLOCK, BlockStonecutterBlock::class.java)
            register(BlockID.STRIPPED_ACACIA_LOG, BlockStrippedAcaciaLog::class.java)
            register(BlockID.STRIPPED_ACACIA_WOOD, BlockStrippedAcaciaWood::class.java)
            register(BlockID.STRIPPED_BAMBOO_BLOCK, BlockStrippedBambooBlock::class.java)
            register(BlockID.STRIPPED_BIRCH_LOG, BlockStrippedBirchLog::class.java)
            register(BlockID.STRIPPED_BIRCH_WOOD, BlockStrippedBirchWood::class.java)
            register(BlockID.STRIPPED_CHERRY_LOG, BlockStrippedCherryLog::class.java)
            register(BlockID.STRIPPED_CHERRY_WOOD, BlockStrippedCherryWood::class.java)
            register(BlockID.STRIPPED_CRIMSON_HYPHAE, BlockStrippedCrimsonHyphae::class.java)
            register(BlockID.STRIPPED_CRIMSON_STEM, BlockStrippedCrimsonStem::class.java)
            register(BlockID.STRIPPED_DARK_OAK_LOG, BlockStrippedDarkOakLog::class.java)
            register(BlockID.STRIPPED_DARK_OAK_WOOD, BlockStrippedDarkOakWood::class.java)
            register(BlockID.STRIPPED_JUNGLE_LOG, BlockStrippedJungleLog::class.java)
            register(BlockID.STRIPPED_JUNGLE_WOOD, BlockStrippedJungleWood::class.java)
            register(BlockID.STRIPPED_MANGROVE_LOG, BlockStrippedMangroveLog::class.java)
            register(BlockID.STRIPPED_MANGROVE_WOOD, BlockStrippedMangroveWood::class.java)
            register(BlockID.STRIPPED_OAK_LOG, BlockStrippedOakLog::class.java)
            register(BlockID.STRIPPED_OAK_WOOD, BlockStrippedOakWood::class.java)
            register(BlockID.STRIPPED_PALE_OAK_LOG, BlockStrippedPaleOakLog::class.java)
            register(BlockID.STRIPPED_PALE_OAK_WOOD, BlockStrippedPaleOakWood::class.java)
            register(BlockID.STRIPPED_SPRUCE_LOG, BlockStrippedSpruceLog::class.java)
            register(BlockID.STRIPPED_SPRUCE_WOOD, BlockStrippedSpruceWood::class.java)
            register(BlockID.STRIPPED_WARPED_HYPHAE, BlockStrippedWarpedHyphae::class.java)
            register(BlockID.STRIPPED_WARPED_STEM, BlockStrippedWarpedStem::class.java)
            register(BlockID.STRUCTURE_BLOCK, BlockStructureBlock::class.java)
            register(BlockID.STRUCTURE_VOID, BlockStructureVoid::class.java)
            register(BlockID.SUNFLOWER, BlockSunflower::class.java)
            register(BlockID.SUSPICIOUS_GRAVEL, BlockSuspiciousGravel::class.java)
            register(BlockID.SUSPICIOUS_SAND, BlockSuspiciousSand::class.java)
            register(BlockID.SWEET_BERRY_BUSH, BlockSweetBerryBush::class.java)
            register(BlockID.TALL_GRASS, BlockTallGrass::class.java)
            register(BlockID.TARGET, BlockTarget::class.java)
            register(BlockID.TINTED_GLASS, BlockTintedGlass::class.java)
            register(BlockID.TNT, BlockTNT::class.java)
            register(BlockID.TORCH, BlockTorch::class.java)
            register(BlockID.TORCHFLOWER, BlockTorchflower::class.java)
            register(BlockID.TORCHFLOWER_CROP, BlockTorchflowerCrop::class.java)
            register(BlockID.TRAPDOOR, BlockTrapdoor::class.java)
            register(BlockID.TRAPPED_CHEST, BlockTrappedChest::class.java)
            register(BlockID.TRIAL_SPAWNER, BlockTrialSpawner::class.java)
            register(BlockID.TRIP_WIRE, BlockTripWire::class.java)
            register(BlockID.TRIPWIRE_HOOK, BlockTripwireHook::class.java)
            register(BlockID.TUBE_CORAL, BlockTubeCoral::class.java)
            register(BlockID.TUBE_CORAL_BLOCK, BlockTubeCoralBlock::class.java)
            register(BlockID.TUBE_CORAL_FAN, BlockTubeCoralFan::class.java)
            register(BlockID.TUBE_CORAL_WALL_FAN, BlockTubeCoralWallFan::class.java)
            register(BlockID.TUFF, BlockTuff::class.java)
            register(BlockID.TUFF_BRICK_DOUBLE_SLAB, BlockTuffBrickDoubleSlab::class.java)
            register(BlockID.TUFF_BRICK_SLAB, BlockTuffBrickSlab::class.java)
            register(BlockID.TUFF_BRICK_STAIRS, BlockTuffBrickStairs::class.java)
            register(BlockID.TUFF_BRICK_WALL, BlockTuffBrickWall::class.java)
            register(BlockID.TUFF_BRICKS, BlockTuffBricks::class.java)
            register(BlockID.TUFF_DOUBLE_SLAB, BlockTuffDoubleSlab::class.java)
            register(BlockID.TUFF_SLAB, BlockTuffSlab::class.java)
            register(BlockID.TUFF_STAIRS, BlockTuffStairs::class.java)
            register(BlockID.TUFF_WALL, BlockTuffWall::class.java)
            register(BlockID.TURTLE_EGG, BlockTurtleEgg::class.java)
            register(BlockID.TWISTING_VINES, BlockTwistingVines::class.java)
            register(BlockID.UNDYED_SHULKER_BOX, BlockUndyedShulkerBox::class.java)
            register(BlockID.UNKNOWN, BlockUnknown::class.java)
            register(BlockID.UNLIT_REDSTONE_TORCH, BlockUnlitRedstoneTorch::class.java)
            register(BlockID.UNPOWERED_COMPARATOR, BlockUnpoweredComparator::class.java)
            register(BlockID.UNPOWERED_REPEATER, BlockUnpoweredRepeater::class.java)
            register(BlockID.VAULT, BlockVault::class.java)
            register(BlockID.VERDANT_FROGLIGHT, BlockVerdantFroglight::class.java)
            register(BlockID.VINE, BlockVine::class.java)
            register(BlockID.WALL_BANNER, BlockWallBanner::class.java)
            register(BlockID.WALL_SIGN, BlockWallSign::class.java)
            register(BlockID.WARPED_BUTTON, BlockWarpedButton::class.java)
            register(BlockID.WARPED_DOOR, BlockWarpedDoor::class.java)
            register(BlockID.WARPED_DOUBLE_SLAB, BlockWarpedDoubleSlab::class.java)
            register(BlockID.WARPED_FENCE, BlockWarpedFence::class.java)
            register(BlockID.WARPED_FENCE_GATE, BlockWarpedFenceGate::class.java)
            register(BlockID.WARPED_FUNGUS, BlockWarpedFungus::class.java)
            register(BlockID.WARPED_HANGING_SIGN, BlockWarpedHangingSign::class.java)
            register(BlockID.WARPED_HYPHAE, BlockWarpedHyphae::class.java)
            register(BlockID.WARPED_NYLIUM, BlockWarpedNylium::class.java)
            register(BlockID.WARPED_PLANKS, BlockWarpedPlanks::class.java)
            register(BlockID.WARPED_PRESSURE_PLATE, BlockWarpedPressurePlate::class.java)
            register(BlockID.WARPED_ROOTS, BlockWarpedRoots::class.java)
            register(BlockID.WARPED_SLAB, BlockWarpedSlab::class.java)
            register(BlockID.WARPED_STAIRS, BlockWarpedStairs::class.java)
            register(BlockID.WARPED_STANDING_SIGN, BlockWarpedStandingSign::class.java)
            register(BlockID.WARPED_STEM, BlockWarpedStem::class.java)
            register(BlockID.WARPED_TRAPDOOR, BlockWarpedTrapdoor::class.java)
            register(BlockID.WARPED_WALL_SIGN, BlockWarpedWallSign::class.java)
            register(BlockID.WARPED_WART_BLOCK, BlockWarpedWartBlock::class.java)
            register(BlockID.WATER, BlockWater::class.java)
            register(BlockID.WATERLILY, BlockWaterlily::class.java)
            register(BlockID.WAXED_CHISELED_COPPER, BlockWaxedChiseledCopper::class.java)
            register(BlockID.WAXED_COPPER, BlockWaxedCopper::class.java)
            register(BlockID.WAXED_COPPER_BULB, BlockWaxedCopperBulb::class.java)
            register(BlockID.WAXED_COPPER_DOOR, BlockWaxedCopperDoor::class.java)
            register(BlockID.WAXED_COPPER_GRATE, BlockWaxedCopperGrate::class.java)
            register(BlockID.WAXED_COPPER_TRAPDOOR, BlockWaxedCopperTrapdoor::class.java)
            register(BlockID.WAXED_CUT_COPPER, BlockWaxedCutCopper::class.java)
            register(BlockID.WAXED_CUT_COPPER_SLAB, BlockWaxedCutCopperSlab::class.java)
            register(BlockID.WAXED_CUT_COPPER_STAIRS, BlockWaxedCutCopperStairs::class.java)
            register(BlockID.WAXED_DOUBLE_CUT_COPPER_SLAB, BlockWaxedDoubleCutCopperSlab::class.java)
            register(BlockID.WAXED_EXPOSED_CHISELED_COPPER, BlockWaxedExposedChiseledCopper::class.java)
            register(BlockID.WAXED_EXPOSED_COPPER, BlockWaxedExposedCopper::class.java)
            register(BlockID.WAXED_EXPOSED_COPPER_BULB, BlockWaxedExposedCopperBulb::class.java)
            register(BlockID.WAXED_EXPOSED_COPPER_DOOR, BlockWaxedExposedCopperDoor::class.java)
            register(BlockID.WAXED_EXPOSED_COPPER_GRATE, BlockWaxedExposedCopperGrate::class.java)
            register(BlockID.WAXED_EXPOSED_COPPER_TRAPDOOR, BlockWaxedExposedCopperTrapdoor::class.java)
            register(BlockID.WAXED_EXPOSED_CUT_COPPER, BlockWaxedExposedCutCopper::class.java)
            register(BlockID.WAXED_EXPOSED_CUT_COPPER_SLAB, BlockWaxedExposedCutCopperSlab::class.java)
            register(BlockID.WAXED_EXPOSED_CUT_COPPER_STAIRS, BlockWaxedExposedCutCopperStairs::class.java)
            register(BlockID.WAXED_EXPOSED_DOUBLE_CUT_COPPER_SLAB, BlockWaxedExposedDoubleCutCopperSlab::class.java)
            register(BlockID.WAXED_OXIDIZED_CHISELED_COPPER, BlockWaxedOxidizedChiseledCopper::class.java)
            register(BlockID.WAXED_OXIDIZED_COPPER, BlockWaxedOxidizedCopper::class.java)
            register(BlockID.WAXED_OXIDIZED_COPPER_BULB, BlockWaxedOxidizedCopperBulb::class.java)
            register(BlockID.WAXED_OXIDIZED_COPPER_DOOR, BlockWaxedOxidizedCopperDoor::class.java)
            register(BlockID.WAXED_OXIDIZED_COPPER_GRATE, BlockWaxedOxidizedCopperGrate::class.java)
            register(BlockID.WAXED_OXIDIZED_COPPER_TRAPDOOR, BlockWaxedOxidizedCopperTrapdoor::class.java)
            register(BlockID.WAXED_OXIDIZED_CUT_COPPER, BlockWaxedOxidizedCutCopper::class.java)
            register(BlockID.WAXED_OXIDIZED_CUT_COPPER_SLAB, BlockWaxedOxidizedCutCopperSlab::class.java)
            register(BlockID.WAXED_OXIDIZED_CUT_COPPER_STAIRS, BlockWaxedOxidizedCutCopperStairs::class.java)
            register(BlockID.WAXED_OXIDIZED_DOUBLE_CUT_COPPER_SLAB, BlockWaxedOxidizedDoubleCutCopperSlab::class.java)
            register(BlockID.WAXED_WEATHERED_CHISELED_COPPER, BlockWaxedWeatheredChiseledCopper::class.java)
            register(BlockID.WAXED_WEATHERED_COPPER, BlockWaxedWeatheredCopper::class.java)
            register(BlockID.WAXED_WEATHERED_COPPER_BULB, BlockWaxedWeatheredCopperBulb::class.java)
            register(BlockID.WAXED_WEATHERED_COPPER_DOOR, BlockWaxedWeatheredCopperDoor::class.java)
            register(BlockID.WAXED_WEATHERED_COPPER_GRATE, BlockWaxedWeatheredCopperGrate::class.java)
            register(BlockID.WAXED_WEATHERED_COPPER_TRAPDOOR, BlockWaxedWeatheredCopperTrapdoor::class.java)
            register(BlockID.WAXED_WEATHERED_CUT_COPPER, BlockWaxedWeatheredCutCopper::class.java)
            register(BlockID.WAXED_WEATHERED_CUT_COPPER_SLAB, BlockWaxedWeatheredCutCopperSlab::class.java)
            register(BlockID.WAXED_WEATHERED_CUT_COPPER_STAIRS, BlockWaxedWeatheredCutCopperStairs::class.java)
            register(BlockID.WAXED_WEATHERED_DOUBLE_CUT_COPPER_SLAB, BlockWaxedWeatheredDoubleCutCopperSlab::class.java)
            register(BlockID.WEATHERED_CHISELED_COPPER, BlockWeatheredChiseledCopper::class.java)
            register(BlockID.WEATHERED_COPPER, BlockWeatheredCopper::class.java)
            register(BlockID.WEATHERED_COPPER_BULB, BlockWeatheredCopperBulb::class.java)
            register(BlockID.WEATHERED_COPPER_DOOR, BlockWeatheredCopperDoor::class.java)
            register(BlockID.WEATHERED_COPPER_GRATE, BlockWeatheredCopperGrate::class.java)
            register(BlockID.WEATHERED_COPPER_TRAPDOOR, BlockWeatheredCopperTrapdoor::class.java)
            register(BlockID.WEATHERED_CUT_COPPER, BlockWeatheredCutCopper::class.java)
            register(BlockID.WEATHERED_CUT_COPPER_SLAB, BlockWeatheredCutCopperSlab::class.java)
            register(BlockID.WEATHERED_CUT_COPPER_STAIRS, BlockWeatheredCutCopperStairs::class.java)
            register(BlockID.WEATHERED_DOUBLE_CUT_COPPER_SLAB, BlockWeatheredDoubleCutCopperSlab::class.java)
            register(BlockID.WEB, BlockWeb::class.java)
            register(BlockID.WEEPING_VINES, BlockWeepingVines::class.java)
            register(BlockID.WET_SPONGE, BlockWetSponge::class.java)
            register(BlockID.WHEAT, BlockWheat::class.java)
            register(BlockID.WHITE_CANDLE, BlockWhiteCandle::class.java)
            register(BlockID.WHITE_CANDLE_CAKE, BlockWhiteCandleCake::class.java)
            register(BlockID.WHITE_CARPET, BlockWhiteCarpet::class.java)
            register(BlockID.WHITE_CONCRETE, BlockWhiteConcrete::class.java)
            register(BlockID.WHITE_CONCRETE_POWDER, BlockWhiteConcretePowder::class.java)
            register(BlockID.WHITE_GLAZED_TERRACOTTA, BlockWhiteGlazedTerracotta::class.java)
            register(BlockID.WHITE_SHULKER_BOX, BlockWhiteShulkerBox::class.java)
            register(BlockID.WHITE_STAINED_GLASS, BlockWhiteStainedGlass::class.java)
            register(BlockID.WHITE_STAINED_GLASS_PANE, BlockWhiteStainedGlassPane::class.java)
            register(BlockID.WHITE_TERRACOTTA, BlockWhiteTerracotta::class.java)
            register(BlockID.WHITE_TULIP, BlockWhiteTulip::class.java)
            register(BlockID.WHITE_WOOL, BlockWhiteWool::class.java)
            register(BlockID.WITHER_ROSE, BlockWitherRose::class.java)
            register(BlockID.WITHER_SKELETON_SKULL, BlockWitherSkeletonSkull::class.java)
            register(BlockID.WOODEN_BUTTON, BlockWoodenButton::class.java)
            register(BlockID.WOODEN_DOOR, BlockWoodenDoor::class.java)
            register(BlockID.WOODEN_PRESSURE_PLATE, BlockWoodenPressurePlate::class.java)
            register(BlockID.YELLOW_CANDLE, BlockYellowCandle::class.java)
            register(BlockID.YELLOW_CANDLE_CAKE, BlockYellowCandleCake::class.java)
            register(BlockID.YELLOW_CARPET, BlockYellowCarpet::class.java)
            register(BlockID.YELLOW_CONCRETE, BlockYellowConcrete::class.java)
            register(BlockID.YELLOW_CONCRETE_POWDER, BlockYellowConcretePowder::class.java)
            register(BlockID.DANDELION, BlockDandelion::class.java)
            register(BlockID.YELLOW_GLAZED_TERRACOTTA, BlockYellowGlazedTerracotta::class.java)
            register(BlockID.YELLOW_SHULKER_BOX, BlockYellowShulkerBox::class.java)
            register(BlockID.YELLOW_STAINED_GLASS, BlockYellowStainedGlass::class.java)
            register(BlockID.YELLOW_STAINED_GLASS_PANE, BlockYellowStainedGlassPane::class.java)
            register(BlockID.YELLOW_TERRACOTTA, BlockYellowTerracotta::class.java)
            register(BlockID.YELLOW_WOOL, BlockYellowWool::class.java)
            register(BlockID.ZOMBIE_HEAD, BlockZombieHead::class.java)
        } catch (ignore: RegisterException) {
        }
    }

    val keySet: @UnmodifiableView MutableSet<String>
        get() = Collections.unmodifiableSet(KEYSET)

    @Throws(RegisterException::class)
    override fun register(key: String, value: Class<out Block?>) {
        if (skipBlockSet.contains(key)) return  // skip for experimental or educational blocks

        if (Modifier.isAbstract(value.modifiers)) {
            throw RegisterException("You can't register a abstract block class!")
        }
        try {
            val properties = value.getDeclaredField("PROPERTIES")
            properties.isAccessible = true
            val modifiers = properties.modifiers

            try {
                require(value.getMethod("getProperties").declaringClass == value)
            } catch (noSuchMethodException: Exception) {
                throw RegisterException(
                    """
                    Block class: ${value.simpleName} must override method:
                    
                    @Override    
                    public @NotNull BlockProperties getProperties() {
                        return PROPERTIES;
                    }
                    """.trimIndent()
                )
            }
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && properties.type == BlockProperties::class.java) {
                val blockProperties = properties[value] as BlockProperties
                val c = FastConstructor.create(value.getConstructor(BlockState::class.java))
                if (CACHE_CONSTRUCTORS.putIfAbsent(blockProperties.identifier, c) != null) {
                    throw RegisterException("This block has already been registered with the identifier: " + blockProperties.identifier)
                } else {
                    KEYSET.add(key)
                    PROPERTIES[key] = blockProperties
                    blockProperties.specialValueMap.values.forEach(Consumer { v: BlockState ->
                        Registries.BLOCKSTATE.registerInternal(v)
                    })
                }
            } else {
                throw RegisterException(
                    "Block: $key must define a field `public static final BlockProperties PROPERTIES`!"
                )
            }
        } catch (e: NoSuchFieldException) {
            throw RegisterException(e)
        } catch (e: IllegalAccessException) {
            throw RegisterException(e)
        } catch (e: NoSuchMethodException) {
            throw RegisterException(e)
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    /**
     * register custom item
     */
    @SafeVarargs
    @Throws(RegisterException::class)
    fun registerCustomBlock(plugin: Plugin, vararg values: Class<out Block?>) {
        for (c in values) {
            registerCustomBlock(plugin, c)
        }
    }

    /**
     * register custom block
     */
    @Throws(RegisterException::class)
    fun registerCustomBlock(plugin: Plugin, value: Class<out Block?>) {
        if (Modifier.isAbstract(value.modifiers)) {
            throw RegisterException("You can't register a abstract block class!")
        }
        try {
            val properties = value.getDeclaredField("PROPERTIES")
            properties.isAccessible = true
            val modifiers = properties.modifiers
            val blockProperties: BlockProperties
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && properties.type == BlockProperties::class.java) {
                blockProperties = properties[value] as BlockProperties
            } else {
                throw RegisterException(
                    "Block: ${value.simpleName} must define a field `public static final BlockProperties PROPERTIES`!"
                )
            }
            try {
                require(value.getMethod("getProperties").declaringClass == value)
            } catch (noSuchMethodException: Exception) {
                throw RegisterException(
                    """
                    Custom block class: ${value.simpleName} must override method:
                    
                    @Override    
                    public @NotNull BlockProperties getProperties() {
                        return PROPERTIES;
                    }
                    """.trimIndent()
                )
            }
            val key = blockProperties.identifier
            val memberLoader: FastMemberLoader =
                IRegistry.fastMemberLoaderCache.computeIfAbsent(plugin.name) {
                    FastMemberLoader(plugin.pluginClassLoader)
                }
            val c = FastConstructor.create(value.getConstructor(BlockState::class.java), memberLoader, false)
            if (CACHE_CONSTRUCTORS.putIfAbsent(key, c) == null) {
                if (CustomBlock::class.java.isAssignableFrom(value)) {
                    val customBlock = c.invoke(null as Any?) as CustomBlock
                    val customBlockDefinitions =
                        CUSTOM_BLOCK_DEFINITIONS.computeIfAbsent(plugin) { ArrayList() }
                    customBlockDefinitions.add(customBlock.definition)
                    val rid = 255 - CustomBlockDefinition.getRuntimeId(customBlock.id)
                    Registries.ITEM_RUNTIMEID.registerCustomRuntimeItem(RuntimeEntry(customBlock.id, rid, false))
                    if (customBlock.shouldBeRegisteredInCreative()) {
                        val itemBlock = ItemBlock(customBlock.toBlock())
                        itemBlock.setNetId(null)
                        Registries.CREATIVE.addCreativeItem(itemBlock)
                    }
                    KEYSET.add(key)
                    PROPERTIES[key] = blockProperties
                    blockProperties.specialValueMap.values.forEach(Consumer { v: BlockState ->
                        Registries.BLOCKSTATE.registerInternal(v)
                    })
                } else {
                    throw RegisterException("Register Error,must implement the CustomBlock interface!")
                }
            } else {
                throw RegisterException("There custom block has already been registered with the identifier: $key")
            }
        } catch (e: NoSuchFieldException) {
            throw RegisterException(e)
        } catch (e: IllegalAccessException) {
            throw RegisterException(e)
        } catch (e: NoSuchMethodException) {
            throw RegisterException(e)
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    private fun register0(key: String, value: Class<out Block?>) {
        try {
            register(key, value)
        } catch (e: Exception) {
            log.error("", e)
        }
    }

    val customBlockDefinitionList: @UnmodifiableView MutableList<CustomBlockDefinition?>
        get() = CUSTOM_BLOCK_DEFINITIONS.values.stream().flatMap { obj: List<CustomBlockDefinition?> -> obj.stream() }
            .toList()

    override fun reload() {
        isLoad.set(false)
        KEYSET.clear()
        CACHE_CONSTRUCTORS.clear()
        PROPERTIES.clear()
        CUSTOM_BLOCK_DEFINITIONS.clear()
        init()
    }

    fun getBlockProperties(identifier: String): BlockProperties {
        val properties = PROPERTIES[identifier]
        requireNotNull(properties != null) { "Get the Block State from a unknown id: $identifier" }
        return properties!!
    }

    override operator fun get(key: String): Block? {
        val constructor = CACHE_CONSTRUCTORS[key] ?: return null
        try {
            return constructor.invoke(null as Any?) as Block
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    fun get(identifier: String, x: Int, y: Int, z: Int): Block? {
        val constructor = CACHE_CONSTRUCTORS[identifier] ?: return null
        try {
            val b = constructor.invoke(null as Any?) as Block
            b.position.x = x.toDouble()
            b.position.y = y.toDouble()
            b.position.z = z.toDouble()
            return b
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    fun get(identifier: String, x: Int, y: Int, z: Int, level: Level): Block? {
        val constructor = CACHE_CONSTRUCTORS[identifier] ?: return null
        try {
            val b = constructor.invoke(null as Any?) as Block
            b.position.x = x.toDouble()
            b.position.y = y.toDouble()
            b.position.z = z.toDouble()
            b.level = level
            return b
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    fun get(identifier: String, x: Int, y: Int, z: Int, layer: Int, level: Level): Block? {
        val constructor = CACHE_CONSTRUCTORS[identifier] ?: return null
        try {
            val b = constructor.invoke(null as Any?) as Block
            b.position.x = x.toDouble()
            b.position.y = y.toDouble()
            b.position.z = z.toDouble()
            b.level = level
            b.layer = layer
            return b
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    operator fun get(blockState: BlockState?): Block? {
        if (blockState == null) return null

        val constructor = CACHE_CONSTRUCTORS[blockState.identifier] ?: return null

        try {
            return constructor.invoke(blockState) as Block
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    fun get(blockState: BlockState, x: Int, y: Int, z: Int): Block? {
        val constructor = CACHE_CONSTRUCTORS[blockState.identifier] ?: return null
        try {
            val b = constructor.invoke(blockState) as Block
            b.position.x = x.toDouble()
            b.position.y = y.toDouble()
            b.position.z = z.toDouble()
            return b
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    operator fun get(blockState: BlockState, x: Int, y: Int, z: Int, level: Level): Block? {
        val constructor = CACHE_CONSTRUCTORS[blockState.identifier] ?: return null
        try {
            val b = constructor.invoke(blockState) as Block
            b.position.x = x.toDouble()
            b.position.y = y.toDouble()
            b.position.z = z.toDouble()
            b.level = level
            return b
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    operator fun get(blockState: BlockState, x: Int, y: Int, z: Int, layer: Int, level: Level): Block? {
        val constructor = CACHE_CONSTRUCTORS[blockState.identifier] ?: return null
        try {
            val b = constructor.invoke(blockState) as Block
            b.position.x = x.toDouble()
            b.position.y = y.toDouble()
            b.position.z = z.toDouble()
            b.level = level
            b.layer = layer
            return b
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private val isLoad = AtomicBoolean(false)
        private val KEYSET: MutableSet<String> = HashSet()
        private val CACHE_CONSTRUCTORS = HashMap<String, FastConstructor<out Block>>()
        private val PROPERTIES = HashMap<String, BlockProperties>()
        private val CUSTOM_BLOCK_DEFINITIONS: MutableMap<Plugin, MutableList<CustomBlockDefinition>> = LinkedHashMap()

        val skipBlockSet: Set<String> = setOf(
            "minecraft:camera",
            "minecraft:chalkboard",
            "minecraft:chemical_heat",
            "minecraft:compound_creator",
            "minecraft:element_constructor",
            "minecraft:lab_table",
            "minecraft:material_reducer",
            "minecraft:colored_torch_purple",
            "minecraft:colored_torch_blue",
            "minecraft:colored_torch_red",
            "minecraft:colored_torch_green",
            "minecraft:deprecated_anvil",
            "minecraft:deprecated_purpur_block_1",
            "minecraft:deprecated_purpur_block_2",
            "minecraft:element_0",
            "minecraft:element_1",
            "minecraft:element_10",
            "minecraft:element_100",
            "minecraft:element_101",
            "minecraft:element_102",
            "minecraft:element_103",
            "minecraft:element_104",
            "minecraft:element_105",
            "minecraft:element_106",
            "minecraft:element_107",
            "minecraft:element_108",
            "minecraft:element_109",
            "minecraft:element_11",
            "minecraft:element_110",
            "minecraft:element_111",
            "minecraft:element_112",
            "minecraft:element_113",
            "minecraft:element_114",
            "minecraft:element_115",
            "minecraft:element_116",
            "minecraft:element_117",
            "minecraft:element_118",
            "minecraft:element_12",
            "minecraft:element_13",
            "minecraft:element_14",
            "minecraft:element_15",
            "minecraft:element_16",
            "minecraft:element_17",
            "minecraft:element_18",
            "minecraft:element_19",
            "minecraft:element_2",
            "minecraft:element_20",
            "minecraft:element_21",
            "minecraft:element_22",
            "minecraft:element_23",
            "minecraft:element_24",
            "minecraft:element_25",
            "minecraft:element_26",
            "minecraft:element_27",
            "minecraft:element_28",
            "minecraft:element_29",
            "minecraft:element_3",
            "minecraft:element_30",
            "minecraft:element_31",
            "minecraft:element_32",
            "minecraft:element_33",
            "minecraft:element_34",
            "minecraft:element_35",
            "minecraft:element_36",
            "minecraft:element_37",
            "minecraft:element_38",
            "minecraft:element_39",
            "minecraft:element_4",
            "minecraft:element_40",
            "minecraft:element_41",
            "minecraft:element_42",
            "minecraft:element_43",
            "minecraft:element_44",
            "minecraft:element_45",
            "minecraft:element_46",
            "minecraft:element_47",
            "minecraft:element_48",
            "minecraft:element_49",
            "minecraft:element_5",
            "minecraft:element_50",
            "minecraft:element_51",
            "minecraft:element_52",
            "minecraft:element_53",
            "minecraft:element_54",
            "minecraft:element_55",
            "minecraft:element_56",
            "minecraft:element_57",
            "minecraft:element_58",
            "minecraft:element_59",
            "minecraft:element_6",
            "minecraft:element_60",
            "minecraft:element_61",
            "minecraft:element_62",
            "minecraft:element_63",
            "minecraft:element_64",
            "minecraft:element_65",
            "minecraft:element_66",
            "minecraft:element_67",
            "minecraft:element_68",
            "minecraft:element_69",
            "minecraft:element_7",
            "minecraft:element_70",
            "minecraft:element_71",
            "minecraft:element_72",
            "minecraft:element_73",
            "minecraft:element_74",
            "minecraft:element_75",
            "minecraft:element_76",
            "minecraft:element_77",
            "minecraft:element_78",
            "minecraft:element_79",
            "minecraft:element_8",
            "minecraft:element_80",
            "minecraft:element_81",
            "minecraft:element_82",
            "minecraft:element_83",
            "minecraft:element_84",
            "minecraft:element_85",
            "minecraft:element_86",
            "minecraft:element_87",
            "minecraft:element_88",
            "minecraft:element_89",
            "minecraft:element_9",
            "minecraft:element_90",
            "minecraft:element_91",
            "minecraft:element_92",
            "minecraft:element_93",
            "minecraft:element_94",
            "minecraft:element_95",
            "minecraft:element_96",
            "minecraft:element_97",
            "minecraft:element_98",
            "minecraft:element_99",
            "minecraft:hard_black_stained_glass",
            "minecraft:hard_black_stained_glass_pane",
            "minecraft:hard_blue_stained_glass",
            "minecraft:hard_blue_stained_glass_pane",
            "minecraft:hard_brown_stained_glass",
            "minecraft:hard_brown_stained_glass_pane",
            "minecraft:hard_cyan_stained_glass",
            "minecraft:hard_cyan_stained_glass_pane",
            "minecraft:hard_glass",
            "minecraft:hard_glass_pane",
            "minecraft:hard_gray_stained_glass",
            "minecraft:hard_gray_stained_glass_pane",
            "minecraft:hard_green_stained_glass",
            "minecraft:hard_green_stained_glass_pane",
            "minecraft:hard_light_blue_stained_glass",
            "minecraft:hard_light_blue_stained_glass_pane",
            "minecraft:hard_light_gray_stained_glass",
            "minecraft:hard_light_gray_stained_glass_pane",
            "minecraft:hard_lime_stained_glass",
            "minecraft:hard_lime_stained_glass_pane",
            "minecraft:hard_magenta_stained_glass",
            "minecraft:hard_magenta_stained_glass_pane",
            "minecraft:hard_orange_stained_glass",
            "minecraft:hard_orange_stained_glass_pane",
            "minecraft:hard_pink_stained_glass",
            "minecraft:hard_pink_stained_glass_pane",
            "minecraft:hard_purple_stained_glass",
            "minecraft:hard_purple_stained_glass_pane",
            "minecraft:hard_red_stained_glass",
            "minecraft:hard_red_stained_glass_pane",
            "minecraft:hard_white_stained_glass",
            "minecraft:hard_white_stained_glass_pane",
            "minecraft:hard_yellow_stained_glass",
            "minecraft:hard_yellow_stained_glass_pane",
            "minecraft:underwater_torch",
            "minecraft:underwater_tnt"
        )
    }
}
