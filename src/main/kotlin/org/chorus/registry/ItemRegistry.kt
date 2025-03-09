package org.chorus.registry

import org.chorus.block.BlockID
import org.chorus.item.*
import org.chorus.item.customitem.CustomItem
import org.chorus.item.customitem.CustomItemDefinition
import org.chorus.nbt.NBTIO.readCompressed
import org.chorus.nbt.tag.CompoundTag
import org.chorus.plugin.Plugin
import org.chorus.registry.ItemRuntimeIdRegistry.RuntimeEntry
import org.chorus.registry.RegisterException
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectSet
import lombok.Getter
import lombok.extern.slf4j.Slf4j
import me.sunlan.fastreflection.FastConstructor
import me.sunlan.fastreflection.FastMemberLoader
import org.jetbrains.annotations.UnmodifiableView
import java.io.IOException
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.HashMap
import kotlin.collections.MutableMap
import kotlin.collections.set

/**
 * @author Cool_Loong
 */

class ItemRegistry : ItemID, IRegistry<String, Item?, Class<out Item?>> {
    val customItemDefinition: @UnmodifiableView MutableMap<String?, CustomItemDefinition?>
        get() = Collections.unmodifiableMap(CUSTOM_ITEM_DEFINITIONS)

    override fun init() {
        if (isLoad.getAndSet(true)) return
        try {
            register(ItemID.ACACIA_BOAT, ItemAcaciaBoat::class.java)
            register(ItemID.ACACIA_CHEST_BOAT, ItemAcaciaChestBoat::class.java)
            register(ItemID.ACACIA_SIGN, ItemAcaciaSign::class.java)
            register(ItemID.AGENT_SPAWN_EGG, ItemAgentSpawnEgg::class.java)
            register(ItemID.ALLAY_SPAWN_EGG, ItemAllaySpawnEgg::class.java)
            register(ItemID.AMETHYST_SHARD, ItemAmethystShard::class.java)
            register(ItemID.ANGLER_POTTERY_SHERD, ItemAnglerPotterySherd::class.java)
            register(ItemID.ANVIL, ItemAnvil::class.java)
            register(ItemID.APPLE, ItemApple::class.java)
            register(ItemID.ARCHER_POTTERY_SHERD, ItemArcherPotterySherd::class.java)
            register(ItemID.ARMADILLO_SCUTE, ItemArmadilloScute::class.java)
            register(ItemID.ARMADILLO_SPAWN_EGG, ItemArmadilloSpawnEgg::class.java)
            register(ItemID.ARMOR_STAND, ItemArmorStand::class.java)
            register(ItemID.ARMS_UP_POTTERY_SHERD, ItemArmsUpPotterySherd::class.java)
            register(ItemID.ARROW, ItemArrow::class.java)
            register(ItemID.AXOLOTL_BUCKET, ItemAxolotlBucket::class.java)
            register(ItemID.AXOLOTL_SPAWN_EGG, ItemAxolotlSpawnEgg::class.java)
            register(ItemID.BAKED_POTATO, ItemBakedPotato::class.java)
            register(ItemID.BALLOON, ItemBalloon::class.java)
            register(ItemID.BAMBOO_CHEST_RAFT, ItemBambooChestRaft::class.java)
            register(ItemID.BAMBOO_RAFT, ItemBambooRaft::class.java)
            register(ItemID.BAMBOO_SIGN, ItemBambooSign::class.java)
            register(ItemID.BANNER, ItemBanner::class.java)
            register(ItemID.BANNER_PATTERN, ItemBannerPattern::class.java)
            register(ItemID.BAT_SPAWN_EGG, ItemBatSpawnEgg::class.java)
            register(ItemID.BEE_SPAWN_EGG, ItemBeeSpawnEgg::class.java)
            register(ItemID.BEEF, ItemBeef::class.java)
            register(ItemID.BEETROOT_SEEDS, ItemBeetrootSeeds::class.java)
            register(ItemID.BEETROOT_SOUP, ItemBeetrootSoup::class.java)
            register(ItemID.BIRCH_BOAT, ItemBirchBoat::class.java)
            register(ItemID.BIRCH_CHEST_BOAT, ItemBirchChestBoat::class.java)
            register(ItemID.BIRCH_SIGN, ItemBirchSign::class.java)
            register(ItemID.BLACK_BUNDLE, ItemBlackBundle::class.java)
            register(ItemID.BLACK_DYE, ItemBlackDye::class.java)
            register(ItemID.BLADE_POTTERY_SHERD, ItemBladePotterySherd::class.java)
            register(ItemID.BLAZE_POWDER, ItemBlazePowder::class.java)
            register(ItemID.BLAZE_ROD, ItemBlazeRod::class.java)
            register(ItemID.BLAZE_SPAWN_EGG, ItemBlazeSpawnEgg::class.java)
            register(ItemID.BLEACH, ItemBleach::class.java)
            register(ItemID.BLUE_BUNDLE, ItemBlueBundle::class.java)
            register(ItemID.BLUE_DYE, ItemBlueDye::class.java)
            register(ItemID.BOAT, ItemBoat::class.java)
            register(ItemID.BOGGED_SPAWN_EGG, ItemBoggedSpawnEgg::class.java)
            register(ItemID.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, ItemBoltArmorTrimSmithingTemplate::class.java)
            register(ItemID.BONE, ItemBone::class.java)
            register(ItemID.BONE_MEAL, ItemBoneMeal::class.java)
            register(ItemID.BOOK, ItemBook::class.java)
            register(ItemID.BORDURE_INDENTED_BANNER_PATTERN, ItemBordureIndentedBannerPattern::class.java)
            register(ItemID.BOW, ItemBow::class.java)
            register(ItemID.BOWL, ItemBowl::class.java)
            register(ItemID.BREAD, ItemBread::class.java)
            register(ItemID.BREEZE_ROD, ItemBreezeRod::class.java)
            register(ItemID.BREEZE_SPAWN_EGG, ItemBreezeSpawnEgg::class.java)
            register(ItemID.BREWER_POTTERY_SHERD, ItemBrewerPotterySherd::class.java)
            register(ItemID.BRICK, ItemBrick::class.java)
            register(ItemID.BROWN_BUNDLE, ItemBrownBundle::class.java)
            register(ItemID.BROWN_DYE, ItemBrownDye::class.java)
            register(ItemID.BRUSH, ItemBrush::class.java)
            register(ItemID.BUCKET, ItemBucket::class.java)
            register(ItemID.BUNDLE, ItemBundle::class.java)
            register(ItemID.BURN_POTTERY_SHERD, ItemBurnPotterySherd::class.java)
            register(ItemID.CAMEL_SPAWN_EGG, ItemCamelSpawnEgg::class.java)
            register(ItemID.CARPET, ItemCarpet::class.java)
            register(ItemID.CARROT, ItemCarrot::class.java)
            register(ItemID.CARROT_ON_A_STICK, ItemCarrotOnAStick::class.java)
            register(ItemID.CAT_SPAWN_EGG, ItemCatSpawnEgg::class.java)
            register(ItemID.CAVE_SPIDER_SPAWN_EGG, ItemCaveSpiderSpawnEgg::class.java)
            register(ItemID.CHAINMAIL_BOOTS, ItemChainmailBoots::class.java)
            register(ItemID.CHAINMAIL_CHESTPLATE, ItemChainmailChestplate::class.java)
            register(ItemID.CHAINMAIL_HELMET, ItemChainmailHelmet::class.java)
            register(ItemID.CHAINMAIL_LEGGINGS, ItemChainmailLeggings::class.java)
            register(ItemID.CHARCOAL, ItemCharcoal::class.java)
            register(ItemID.CHERRY_BOAT, ItemCherryBoat::class.java)
            register(ItemID.CHERRY_CHEST_BOAT, ItemCherryChestBoat::class.java)
            register(ItemID.CHERRY_SIGN, ItemCherrySign::class.java)
            register(ItemID.CHEST_BOAT, ItemChestBoat::class.java)
            register(ItemID.CHEST_MINECART, ItemChestMinecart::class.java)
            register(ItemID.CHICKEN, ItemChicken::class.java)
            register(ItemID.CHICKEN_SPAWN_EGG, ItemChickenSpawnEgg::class.java)
            register(ItemID.CHORUS_FRUIT, ItemChorusFruit::class.java)
            register(ItemID.CLAY_BALL, ItemClayBall::class.java)
            register(ItemID.CLOCK, ItemClock::class.java)
            register(ItemID.COAL, ItemCoal::class.java)
            register(ItemID.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, ItemCoastArmorTrimSmithingTemplate::class.java)
            register(ItemID.COBBLESTONE_WALL, ItemCobblestoneWall::class.java)
            register(ItemID.COCOA_BEANS, ItemCocoaBeans::class.java)
            register(ItemID.COD, ItemCod::class.java)
            register(ItemID.COD_BUCKET, ItemCodBucket::class.java)
            register(ItemID.COD_SPAWN_EGG, ItemCodSpawnEgg::class.java)
            register(ItemID.COMMAND_BLOCK_MINECART, ItemCommandBlockMinecart::class.java)
            register(ItemID.COMPARATOR, ItemComparator::class.java)
            register(ItemID.COMPASS, ItemCompass::class.java)
            register(ItemID.COMPOUND, ItemCompound::class.java)
            register(ItemID.CONCRETE, ItemConcrete::class.java)
            register(ItemID.CONCRETE_POWDER, ItemConcretePowder::class.java)
            register(ItemID.COOKED_BEEF, ItemCookedBeef::class.java)
            register(ItemID.COOKED_CHICKEN, ItemCookedChicken::class.java)
            register(ItemID.COOKED_COD, ItemCookedCod::class.java)
            register(ItemID.COOKED_MUTTON, ItemCookedMutton::class.java)
            register(ItemID.COOKED_PORKCHOP, ItemCookedPorkchop::class.java)
            register(ItemID.COOKED_RABBIT, ItemCookedRabbit::class.java)
            register(ItemID.COOKED_SALMON, ItemCookedSalmon::class.java)
            register(ItemID.COOKIE, ItemCookie::class.java)
            register(ItemID.COPPER_INGOT, ItemCopperIngot::class.java)
            register(ItemID.CORAL, ItemCoral::class.java)
            register(ItemID.CORAL_BLOCK, ItemCoralBlock::class.java)
            register(ItemID.CORAL_FAN, ItemCoralFan::class.java)
            register(ItemID.CORAL_FAN_DEAD, ItemCoralFanDead::class.java)
            register(ItemID.COW_SPAWN_EGG, ItemCowSpawnEgg::class.java)
            register(ItemID.CREAKING_SPAWN_EGG, ItemCreakingSpawnEgg::class.java)
            register(ItemID.CREEPER_BANNER_PATTERN, ItemCreeperBannerPattern::class.java)
            register(ItemID.CREEPER_HEAD, ItemCreeperHead::class.java)
            register(ItemID.CREEPER_SPAWN_EGG, ItemCreeperSpawnEgg::class.java)
            register(ItemID.CRIMSON_SIGN, ItemCrimsonSign::class.java)
            register(ItemID.CROSSBOW, ItemCrossbow::class.java)
            register(ItemID.CYAN_BUNDLE, ItemCyanBundle::class.java)
            register(ItemID.CYAN_DYE, ItemCyanDye::class.java)
            register(ItemID.DANGER_POTTERY_SHERD, ItemDangerPotterySherd::class.java)
            register(ItemID.DARK_OAK_BOAT, ItemDarkOakBoat::class.java)
            register(ItemID.DARK_OAK_CHEST_BOAT, ItemDarkOakChestBoat::class.java)
            register(ItemID.DARK_OAK_SIGN, ItemDarkOakSign::class.java)
            register(ItemID.DIAMOND, ItemDiamond::class.java)
            register(ItemID.DIAMOND_AXE, ItemDiamondAxe::class.java)
            register(ItemID.DIAMOND_BOOTS, ItemDiamondBoots::class.java)
            register(ItemID.DIAMOND_CHESTPLATE, ItemDiamondChestplate::class.java)
            register(ItemID.DIAMOND_HELMET, ItemDiamondHelmet::class.java)
            register(ItemID.DIAMOND_HOE, ItemDiamondHoe::class.java)
            register(ItemID.DIAMOND_HORSE_ARMOR, ItemDiamondHorseArmor::class.java)
            register(ItemID.DIAMOND_LEGGINGS, ItemDiamondLeggings::class.java)
            register(ItemID.DIAMOND_PICKAXE, ItemDiamondPickaxe::class.java)
            register(ItemID.DIAMOND_SHOVEL, ItemDiamondShovel::class.java)
            register(ItemID.DIAMOND_SWORD, ItemDiamondSword::class.java)
            register(ItemID.DIRT, ItemDirt::class.java)
            register(ItemID.DISC_FRAGMENT_5, ItemDiscFragment5::class.java)
            register(ItemID.DOLPHIN_SPAWN_EGG, ItemDolphinSpawnEgg::class.java)
            register(ItemID.DONKEY_SPAWN_EGG, ItemDonkeySpawnEgg::class.java)
            register(ItemID.DOUBLE_PLANT, ItemDoublePlant::class.java)
            register(ItemID.DRAGON_BREATH, ItemDragonBreath::class.java)
            register(ItemID.DRAGON_HEAD, ItemDragonHead::class.java)
            register(ItemID.DRIED_KELP, ItemDriedKelp::class.java)
            register(ItemID.DROWNED_SPAWN_EGG, ItemDrownedSpawnEgg::class.java)
            register(ItemID.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, ItemDuneArmorTrimSmithingTemplate::class.java)
            register(ItemID.DYE, ItemDye::class.java)
            register(ItemID.ECHO_SHARD, ItemEchoShard::class.java)
            register(ItemID.EGG, ItemEgg::class.java)
            register(ItemID.ELDER_GUARDIAN_SPAWN_EGG, ItemElderGuardianSpawnEgg::class.java)
            register(ItemID.ELYTRA, ItemElytra::class.java)
            register(ItemID.EMERALD, ItemEmerald::class.java)
            register(ItemID.EMPTY_MAP, ItemEmptyMap::class.java)
            register(ItemID.ENCHANTED_BOOK, ItemEnchantedBook::class.java)
            register(ItemID.ENCHANTED_GOLDEN_APPLE, ItemEnchantedGoldenApple::class.java)
            register(ItemID.END_CRYSTAL, ItemEndCrystal::class.java)
            register(ItemID.ENDER_DRAGON_SPAWN_EGG, ItemEnderDragonSpawnEgg::class.java)
            register(ItemID.ENDER_EYE, ItemEnderEye::class.java)
            register(ItemID.ENDER_PEARL, ItemEnderPearl::class.java)
            register(ItemID.ENDERMAN_SPAWN_EGG, ItemEndermanSpawnEgg::class.java)
            register(ItemID.ENDERMITE_SPAWN_EGG, ItemEndermiteSpawnEgg::class.java)
            register(ItemID.EVOKER_SPAWN_EGG, ItemEvokerSpawnEgg::class.java)
            register(ItemID.EXPERIENCE_BOTTLE, ItemExperienceBottle::class.java)
            register(ItemID.EXPLORER_POTTERY_SHERD, ItemExplorerPotterySherd::class.java)
            register(ItemID.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, ItemEyeArmorTrimSmithingTemplate::class.java)
            register(ItemID.FEATHER, ItemFeather::class.java)
            register(ItemID.FENCE, ItemFence::class.java)
            register(ItemID.FERMENTED_SPIDER_EYE, ItemFermentedSpiderEye::class.java)
            register(ItemID.FIELD_MASONED_BANNER_PATTERN, ItemFieldMasonedBannerPattern::class.java)
            register(ItemID.FILLED_MAP, ItemFilledMap::class.java)
            register(ItemID.FIRE_CHARGE, ItemFireCharge::class.java)
            register(ItemID.FIREWORK_ROCKET, ItemFireworkRocket::class.java)
            register(ItemID.FIREWORK_STAR, ItemFireworkStar::class.java)
            register(ItemID.FISHING_ROD, ItemFishingRod::class.java)
            register(ItemID.FLINT, ItemFlint::class.java)
            register(ItemID.FLINT_AND_STEEL, ItemFlintAndSteel::class.java)
            register(ItemID.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, ItemFlowArmorTrimSmithingTemplate::class.java)
            register(ItemID.FLOW_BANNER_PATTERN, ItemFlowBannerPattern::class.java)
            register(ItemID.FLOW_POTTERY_SHERD, ItemFlowPotterySherd::class.java)
            register(ItemID.FLOWER_BANNER_PATTERN, ItemFlowerBannerPattern::class.java)
            register(ItemID.FOX_SPAWN_EGG, ItemFoxSpawnEgg::class.java)
            register(ItemID.FRIEND_POTTERY_SHERD, ItemFriendPotterySherd::class.java)
            register(ItemID.FROG_SPAWN_EGG, ItemFrogSpawnEgg::class.java)
            register(ItemID.GHAST_SPAWN_EGG, ItemGhastSpawnEgg::class.java)
            register(ItemID.GHAST_TEAR, ItemGhastTear::class.java)
            register(ItemID.GLASS_BOTTLE, ItemGlassBottle::class.java)
            register(ItemID.GLISTERING_MELON_SLICE, ItemGlisteringMelonSlice::class.java)
            register(ItemID.GLOBE_BANNER_PATTERN, ItemGlobeBannerPattern::class.java)
            register(ItemID.GLOW_BERRIES, ItemGlowBerries::class.java)
            register(ItemID.GLOW_INK_SAC, ItemGlowInkSac::class.java)
            register(ItemID.GLOW_SQUID_SPAWN_EGG, ItemGlowSquidSpawnEgg::class.java)
            register(ItemID.GLOW_STICK, ItemGlowStick::class.java)
            register(ItemID.GLOWSTONE_DUST, ItemGlowstoneDust::class.java)
            register(ItemID.GOAT_HORN, ItemGoatHorn::class.java)
            register(ItemID.GOAT_SPAWN_EGG, ItemGoatSpawnEgg::class.java)
            register(ItemID.GOLD_INGOT, ItemGoldIngot::class.java)
            register(ItemID.GOLD_NUGGET, ItemGoldNugget::class.java)
            register(ItemID.GOLDEN_APPLE, ItemGoldenApple::class.java)
            register(ItemID.GOLDEN_AXE, ItemGoldenAxe::class.java)
            register(ItemID.GOLDEN_BOOTS, ItemGoldenBoots::class.java)
            register(ItemID.GOLDEN_CARROT, ItemGoldenCarrot::class.java)
            register(ItemID.GOLDEN_CHESTPLATE, ItemGoldenChestplate::class.java)
            register(ItemID.GOLDEN_HELMET, ItemGoldenHelmet::class.java)
            register(ItemID.GOLDEN_HOE, ItemGoldenHoe::class.java)
            register(ItemID.GOLDEN_HORSE_ARMOR, ItemGoldenHorseArmor::class.java)
            register(ItemID.GOLDEN_LEGGINGS, ItemGoldenLeggings::class.java)
            register(ItemID.GOLDEN_PICKAXE, ItemGoldenPickaxe::class.java)
            register(ItemID.GOLDEN_SHOVEL, ItemGoldenShovel::class.java)
            register(ItemID.GOLDEN_SWORD, ItemGoldenSword::class.java)
            register(ItemID.GRAY_BUNDLE, ItemGrayBundle::class.java)
            register(ItemID.GRAY_DYE, ItemGrayDye::class.java)
            register(ItemID.GREEN_BUNDLE, ItemGreenBundle::class.java)
            register(ItemID.GREEN_DYE, ItemGreenDye::class.java)
            register(ItemID.GUARDIAN_SPAWN_EGG, ItemGuardianSpawnEgg::class.java)
            register(ItemID.GUNPOWDER, ItemGunpowder::class.java)
            register(ItemID.GUSTER_BANNER_PATTERN, ItemGusterBannerPattern::class.java)
            register(ItemID.GUSTER_POTTERY_SHERD, ItemGusterPotterySherd::class.java)
            register(ItemID.HARD_STAINED_GLASS, ItemHardStainedGlass::class.java)
            register(ItemID.HARD_STAINED_GLASS_PANE, ItemHardStainedGlassPane::class.java)
            register(ItemID.HEART_OF_THE_SEA, ItemHeartOfTheSea::class.java)
            register(ItemID.HEART_POTTERY_SHERD, ItemHeartPotterySherd::class.java)
            register(ItemID.HEARTBREAK_POTTERY_SHERD, ItemHeartbreakPotterySherd::class.java)
            register(ItemID.HOGLIN_SPAWN_EGG, ItemHoglinSpawnEgg::class.java)
            register(ItemID.HONEY_BOTTLE, ItemHoneyBottle::class.java)
            register(ItemID.HONEYCOMB, ItemHoneycomb::class.java)
            register(ItemID.HOPPER_MINECART, ItemHopperMinecart::class.java)
            register(ItemID.HORSE_SPAWN_EGG, ItemHorseSpawnEgg::class.java)
            register(ItemID.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, ItemHostArmorTrimSmithingTemplate::class.java)
            register(ItemID.HOWL_POTTERY_SHERD, ItemHowlPotterySherd::class.java)
            register(ItemID.HUSK_SPAWN_EGG, ItemHuskSpawnEgg::class.java)
            register(ItemID.ICE_BOMB, ItemIceBomb::class.java)
            register(ItemID.INK_SAC, ItemInkSac::class.java)
            register(ItemID.IRON_AXE, ItemIronAxe::class.java)
            register(ItemID.IRON_BOOTS, ItemIronBoots::class.java)
            register(ItemID.IRON_CHESTPLATE, ItemIronChestplate::class.java)
            register(ItemID.IRON_GOLEM_SPAWN_EGG, ItemIronGolemSpawnEgg::class.java)
            register(ItemID.IRON_HELMET, ItemIronHelmet::class.java)
            register(ItemID.IRON_HOE, ItemIronHoe::class.java)
            register(ItemID.IRON_HORSE_ARMOR, ItemIronHorseArmor::class.java)
            register(ItemID.IRON_INGOT, ItemIronIngot::class.java)
            register(ItemID.IRON_LEGGINGS, ItemIronLeggings::class.java)
            register(ItemID.IRON_NUGGET, ItemIronNugget::class.java)
            register(ItemID.IRON_PICKAXE, ItemIronPickaxe::class.java)
            register(ItemID.IRON_SHOVEL, ItemIronShovel::class.java)
            register(ItemID.IRON_SWORD, ItemIronSword::class.java)
            register(ItemID.JUNGLE_BOAT, ItemJungleBoat::class.java)
            register(ItemID.JUNGLE_CHEST_BOAT, ItemJungleChestBoat::class.java)
            register(ItemID.JUNGLE_SIGN, ItemJungleSign::class.java)
            register(ItemID.LAPIS_LAZULI, ItemLapisLazuli::class.java)
            register(ItemID.LAVA_BUCKET, ItemLavaBucket::class.java)
            register(ItemID.LEAD, ItemLead::class.java)
            register(ItemID.LEATHER, ItemLeather::class.java)
            register(ItemID.LEATHER_BOOTS, ItemLeatherBoots::class.java)
            register(ItemID.LEATHER_CHESTPLATE, ItemLeatherChestplate::class.java)
            register(ItemID.LEATHER_HELMET, ItemLeatherHelmet::class.java)
            register(ItemID.LEATHER_HORSE_ARMOR, ItemLeatherHorseArmor::class.java)
            register(ItemID.LEATHER_LEGGINGS, ItemLeatherLeggings::class.java)
            register(ItemID.LEAVES, ItemLeaves::class.java)
            register(ItemID.LEAVES2, ItemLeaves2::class.java)
            register(ItemID.LIGHT_BLUE_BUNDLE, ItemLightBlueBundle::class.java)
            register(ItemID.LIGHT_BLUE_DYE, ItemLightBlueDye::class.java)
            register(ItemID.LIGHT_GRAY_BUNDLE, ItemLightGrayBundle::class.java)
            register(ItemID.LIGHT_GRAY_DYE, ItemLightGrayDye::class.java)
            register(ItemID.LIME_BUNDLE, ItemLimeBundle::class.java)
            register(ItemID.LIME_DYE, ItemLimeDye::class.java)
            register(ItemID.LINGERING_POTION, ItemLingeringPotion::class.java)
            register(ItemID.LLAMA_SPAWN_EGG, ItemLlamaSpawnEgg::class.java)
            register(ItemID.LODESTONE_COMPASS, ItemLodestoneCompass::class.java)
            register(ItemID.LOG, ItemLog::class.java)
            register(ItemID.LOG2, ItemLog2::class.java)
            register(ItemID.MACE, ItemMace::class.java)
            register(ItemID.MAGENTA_BUNDLE, ItemMagentaBundle::class.java)
            register(ItemID.MAGENTA_DYE, ItemMagentaDye::class.java)
            register(ItemID.MAGMA_CREAM, ItemMagmaCream::class.java)
            register(ItemID.MAGMA_CUBE_SPAWN_EGG, ItemMagmaCubeSpawnEgg::class.java)
            register(ItemID.MANGROVE_BOAT, ItemMangroveBoat::class.java)
            register(ItemID.MANGROVE_CHEST_BOAT, ItemMangroveChestBoat::class.java)
            register(ItemID.MANGROVE_SIGN, ItemMangroveSign::class.java)
            register(ItemID.MEDICINE, ItemMedicine::class.java)
            register(ItemID.MELON_SEEDS, ItemMelonSeeds::class.java)
            register(ItemID.MELON_SLICE, ItemMelonSlice::class.java)
            register(ItemID.MILK_BUCKET, ItemMilkBucket::class.java)
            register(ItemID.MINECART, ItemMinecart::class.java)
            register(ItemID.MINER_POTTERY_SHERD, ItemMinerPotterySherd::class.java)
            register(ItemID.MOJANG_BANNER_PATTERN, ItemMojangBannerPattern::class.java)
            register(ItemID.MOOSHROOM_SPAWN_EGG, ItemMooshroomSpawnEgg::class.java)
            register(ItemID.MOURNER_POTTERY_SHERD, ItemMournerPotterySherd::class.java)
            register(ItemID.MULE_SPAWN_EGG, ItemMuleSpawnEgg::class.java)
            register(ItemID.MUSHROOM_STEW, ItemMushroomStew::class.java)
            register(ItemID.MUSIC_DISC_11, ItemMusicDisc11::class.java)
            register(ItemID.MUSIC_DISC_13, ItemMusicDisc13::class.java)
            register(ItemID.MUSIC_DISC_5, ItemMusicDisc5::class.java)
            register(ItemID.MUSIC_DISC_BLOCKS, ItemMusicDiscBlocks::class.java)
            register(ItemID.MUSIC_DISC_CAT, ItemMusicDiscCat::class.java)
            register(ItemID.MUSIC_DISC_CHIRP, ItemMusicDiscChirp::class.java)
            register(ItemID.MUSIC_DISC_CREATOR, ItemMusicDiscCreator::class.java)
            register(ItemID.MUSIC_DISC_CREATOR_MUSIC_BOX, ItemMusicDiscCreatorMusicBox::class.java)
            register(ItemID.MUSIC_DISC_FAR, ItemMusicDiscFar::class.java)
            register(ItemID.MUSIC_DISC_MALL, ItemMusicDiscMall::class.java)
            register(ItemID.MUSIC_DISC_MELLOHI, ItemMusicDiscMellohi::class.java)
            register(ItemID.MUSIC_DISC_OTHERSIDE, ItemMusicDiscOtherside::class.java)
            register(ItemID.MUSIC_DISC_PIGSTEP, ItemMusicDiscPigstep::class.java)
            register(ItemID.MUSIC_DISC_PRECIPICE, ItemMusicDiscPrecipice::class.java)
            register(ItemID.MUSIC_DISC_RELIC, ItemMusicDiscRelic::class.java)
            register(ItemID.MUSIC_DISC_STAL, ItemMusicDiscStal::class.java)
            register(ItemID.MUSIC_DISC_STRAD, ItemMusicDiscStrad::class.java)
            register(ItemID.MUSIC_DISC_WAIT, ItemMusicDiscWait::class.java)
            register(ItemID.MUSIC_DISC_WARD, ItemMusicDiscWard::class.java)
            register(ItemID.MUTTON, ItemMutton::class.java)
            register(ItemID.NAME_TAG, ItemNameTag::class.java)
            register(ItemID.NAUTILUS_SHELL, ItemNautilusShell::class.java)
            register(ItemID.NETHER_STAR, ItemNetherStar::class.java)
            register(ItemID.NETHERBRICK, ItemNetherbrick::class.java)
            register(ItemID.NETHERITE_AXE, ItemNetheriteAxe::class.java)
            register(ItemID.NETHERITE_BOOTS, ItemNetheriteBoots::class.java)
            register(ItemID.NETHERITE_CHESTPLATE, ItemNetheriteChestplate::class.java)
            register(ItemID.NETHERITE_HELMET, ItemNetheriteHelmet::class.java)
            register(ItemID.NETHERITE_HOE, ItemNetheriteHoe::class.java)
            register(ItemID.NETHERITE_INGOT, ItemNetheriteIngot::class.java)
            register(ItemID.NETHERITE_LEGGINGS, ItemNetheriteLeggings::class.java)
            register(ItemID.NETHERITE_PICKAXE, ItemNetheritePickaxe::class.java)
            register(ItemID.NETHERITE_SCRAP, ItemNetheriteScrap::class.java)
            register(ItemID.NETHERITE_SHOVEL, ItemNetheriteShovel::class.java)
            register(ItemID.NETHERITE_SWORD, ItemNetheriteSword::class.java)
            register(ItemID.NETHERITE_UPGRADE_SMITHING_TEMPLATE, ItemNetheriteUpgradeSmithingTemplate::class.java)
            register(ItemID.NPC_SPAWN_EGG, ItemNpcSpawnEgg::class.java)
            register(ItemID.OAK_BOAT, ItemOakBoat::class.java)
            register(ItemID.OAK_CHEST_BOAT, ItemOakChestBoat::class.java)
            register(ItemID.OAK_SIGN, ItemOakSign::class.java)
            register(ItemID.OCELOT_SPAWN_EGG, ItemOcelotSpawnEgg::class.java)
            register(ItemID.OMINOUS_BOTTLE, ItemOminousBottle::class.java)
            register(ItemID.OMINOUS_TRIAL_KEY, ItemOminousTrialKey::class.java)
            register(ItemID.ORANGE_BUNDLE, ItemOrangeBundle::class.java)
            register(ItemID.ORANGE_DYE, ItemOrangeDye::class.java)
            register(ItemID.PAINTING, ItemPainting::class.java)
            register(ItemID.PALE_OAK_BOAT, ItemPaleOakBoat::class.java)
            register(ItemID.PALE_OAK_CHEST_BOAT, ItemPaleOakChestBoat::class.java)
            register(ItemID.PALE_OAK_HANGING_SIGN, ItemPaleOakHangingSign::class.java)
            register(ItemID.PALE_OAK_SIGN, ItemPaleOakSign::class.java)
            register(ItemID.PANDA_SPAWN_EGG, ItemPandaSpawnEgg::class.java)
            register(ItemID.PAPER, ItemPaper::class.java)
            register(ItemID.PARROT_SPAWN_EGG, ItemParrotSpawnEgg::class.java)
            register(ItemID.PHANTOM_MEMBRANE, ItemPhantomMembrane::class.java)
            register(ItemID.PHANTOM_SPAWN_EGG, ItemPhantomSpawnEgg::class.java)
            register(ItemID.PIG_SPAWN_EGG, ItemPigSpawnEgg::class.java)
            register(ItemID.PIGLIN_BANNER_PATTERN, ItemPiglinBannerPattern::class.java)
            register(ItemID.PIGLIN_BRUTE_SPAWN_EGG, ItemPiglinBruteSpawnEgg::class.java)
            register(ItemID.PIGLIN_HEAD, ItemPiglinHead::class.java)
            register(ItemID.PIGLIN_SPAWN_EGG, ItemPiglinSpawnEgg::class.java)
            register(ItemID.PILLAGER_SPAWN_EGG, ItemPillagerSpawnEgg::class.java)
            register(ItemID.PINK_BUNDLE, ItemPinkBundle::class.java)
            register(ItemID.PINK_DYE, ItemPinkDye::class.java)
            register(ItemID.PITCHER_POD, ItemPitcherPod::class.java)
            register(ItemID.PLANKS, ItemPlanks::class.java)
            register(ItemID.PLAYER_HEAD, ItemPlayerHead::class.java)
            register(ItemID.PLENTY_POTTERY_SHERD, ItemPlentyPotterySherd::class.java)
            register(ItemID.POISONOUS_POTATO, ItemPoisonousPotato::class.java)
            register(ItemID.POLAR_BEAR_SPAWN_EGG, ItemPolarBearSpawnEgg::class.java)
            register(ItemID.POPPED_CHORUS_FRUIT, ItemPoppedChorusFruit::class.java)
            register(ItemID.PORKCHOP, ItemPorkchop::class.java)
            register(ItemID.POTATO, ItemPotato::class.java)
            register(ItemID.POTION, ItemPotion::class.java)
            register(ItemID.POWDER_SNOW_BUCKET, ItemPowderSnowBucket::class.java)
            register(ItemID.PRISMARINE, ItemPrismarine::class.java)
            register(ItemID.PRISMARINE_CRYSTALS, ItemPrismarineCrystals::class.java)
            register(ItemID.PRISMARINE_SHARD, ItemPrismarineShard::class.java)
            register(ItemID.PRIZE_POTTERY_SHERD, ItemPrizePotterySherd::class.java)
            register(ItemID.PUFFERFISH, ItemPufferfish::class.java)
            register(ItemID.PUFFERFISH_BUCKET, ItemPufferfishBucket::class.java)
            register(ItemID.PUFFERFISH_SPAWN_EGG, ItemPufferfishSpawnEgg::class.java)
            register(ItemID.PUMPKIN_PIE, ItemPumpkinPie::class.java)
            register(ItemID.PUMPKIN_SEEDS, ItemPumpkinSeeds::class.java)
            register(ItemID.PURPLE_BUNDLE, ItemPurpleBundle::class.java)
            register(ItemID.PURPLE_DYE, ItemPurpleDye::class.java)
            register(ItemID.PURPUR_BLOCK, ItemPurpurBlock::class.java)
            register(ItemID.QUARTZ, ItemQuartz::class.java)
            register(ItemID.QUARTZ_BLOCK, ItemQuartzBlock::class.java)
            register(ItemID.RABBIT, ItemRabbit::class.java)
            register(ItemID.RABBIT_FOOT, ItemRabbitFoot::class.java)
            register(ItemID.RABBIT_HIDE, ItemRabbitHide::class.java)
            register(ItemID.RABBIT_SPAWN_EGG, ItemRabbitSpawnEgg::class.java)
            register(ItemID.RABBIT_STEW, ItemRabbitStew::class.java)
            register(ItemID.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, ItemRaiserArmorTrimSmithingTemplate::class.java)
            register(ItemID.RAPID_FERTILIZER, ItemRapidFertilizer::class.java)
            register(ItemID.RAVAGER_SPAWN_EGG, ItemRavagerSpawnEgg::class.java)
            register(ItemID.RAW_COPPER, ItemRawCopper::class.java)
            register(ItemID.RAW_GOLD, ItemRawGold::class.java)
            register(ItemID.RAW_IRON, ItemRawIron::class.java)
            register(ItemID.RECOVERY_COMPASS, ItemRecoveryCompass::class.java)
            register(ItemID.RED_BUNDLE, ItemRedBundle::class.java)
            register(ItemID.RED_DYE, ItemRedDye::class.java)
            register(ItemID.RED_FLOWER, ItemRedFlower::class.java)
            register(ItemID.RED_SANDSTONE, ItemRedSandstone::class.java)
            register(ItemID.REDSTONE, ItemRedstone::class.java)
            register(ItemID.REPEATER, ItemRepeater::class.java)
            register(ItemID.RESIN_BRICK, ItemResinBrick::class.java)
            register(ItemID.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, ItemRibArmorTrimSmithingTemplate::class.java)
            register(ItemID.ROTTEN_FLESH, ItemRottenFlesh::class.java)
            register(ItemID.SADDLE, ItemSaddle::class.java)
            register(ItemID.SALMON, ItemSalmon::class.java)
            register(ItemID.SALMON_BUCKET, ItemSalmonBucket::class.java)
            register(ItemID.SALMON_SPAWN_EGG, ItemSalmonSpawnEgg::class.java)
            register(ItemID.SAND, ItemSand::class.java)
            register(ItemID.SANDSTONE, ItemSandstone::class.java)
            register(ItemID.SAPLING, ItemSapling::class.java)
            register(ItemID.SCRAPE_POTTERY_SHERD, ItemScrapePotterySherd::class.java)
            register(ItemID.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, ItemSentryArmorTrimSmithingTemplate::class.java)
            register(ItemID.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, ItemShaperArmorTrimSmithingTemplate::class.java)
            register(ItemID.SHEAF_POTTERY_SHERD, ItemSheafPotterySherd::class.java)
            register(ItemID.SHEARS, ItemShears::class.java)
            register(ItemID.SHEEP_SPAWN_EGG, ItemSheepSpawnEgg::class.java)
            register(ItemID.SHELTER_POTTERY_SHERD, ItemShelterPotterySherd::class.java)
            register(ItemID.SHIELD, ItemShield::class.java)
            register(ItemID.SHULKER_BOX, ItemShulkerBox::class.java)
            register(ItemID.SHULKER_SHELL, ItemShulkerShell::class.java)
            register(ItemID.SHULKER_SPAWN_EGG, ItemShulkerSpawnEgg::class.java)
            register(ItemID.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, ItemSilenceArmorTrimSmithingTemplate::class.java)
            register(ItemID.SILVERFISH_SPAWN_EGG, ItemSilverfishSpawnEgg::class.java)
            register(ItemID.SKELETON_HORSE_SPAWN_EGG, ItemSkeletonHorseSpawnEgg::class.java)
            register(ItemID.SKELETON_SKULL, ItemSkeletonSkull::class.java)
            register(ItemID.SKELETON_SPAWN_EGG, ItemSkeletonSpawnEgg::class.java)
            register(ItemID.SKULL_BANNER_PATTERN, ItemSkullBannerPattern::class.java)
            register(ItemID.SKULL_POTTERY_SHERD, ItemSkullPotterySherd::class.java)
            register(ItemID.SLIME_BALL, ItemSlimeBall::class.java)
            register(ItemID.SLIME_SPAWN_EGG, ItemSlimeSpawnEgg::class.java)
            register(ItemID.SNIFFER_SPAWN_EGG, ItemSnifferSpawnEgg::class.java)
            register(ItemID.SNORT_POTTERY_SHERD, ItemSnortPotterySherd::class.java)
            register(ItemID.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, ItemSnoutArmorTrimSmithingTemplate::class.java)
            register(ItemID.SNOW_GOLEM_SPAWN_EGG, ItemSnowGolemSpawnEgg::class.java)
            register(ItemID.SNOWBALL, ItemSnowball::class.java)
            register(ItemID.SPARKLER, ItemSparkler::class.java)
            register(ItemID.SPAWN_EGG, ItemSpawnEgg::class.java)
            register(ItemID.SPIDER_EYE, ItemSpiderEye::class.java)
            register(ItemID.SPIDER_SPAWN_EGG, ItemSpiderSpawnEgg::class.java)
            register(ItemID.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, ItemSpireArmorTrimSmithingTemplate::class.java)
            register(ItemID.SPLASH_POTION, ItemSplashPotion::class.java)
            register(ItemID.SPONGE, ItemSponge::class.java)
            register(ItemID.SPRUCE_BOAT, ItemSpruceBoat::class.java)
            register(ItemID.SPRUCE_CHEST_BOAT, ItemSpruceChestBoat::class.java)
            register(ItemID.SPRUCE_SIGN, ItemSpruceSign::class.java)
            register(ItemID.SPYGLASS, ItemSpyglass::class.java)
            register(ItemID.SQUID_SPAWN_EGG, ItemSquidSpawnEgg::class.java)
            register(ItemID.STAINED_GLASS, ItemStainedGlass::class.java)
            register(ItemID.STAINED_GLASS_PANE, ItemStainedGlassPane::class.java)
            register(ItemID.STAINED_HARDENED_CLAY, ItemStainedHardenedClay::class.java)
            register(ItemID.STICK, ItemStick::class.java)
            register(ItemID.STONE_AXE, ItemStoneAxe::class.java)
            register(ItemID.STONE_BLOCK_SLAB, ItemStoneBlockSlab::class.java)
            register(ItemID.STONE_HOE, ItemStoneHoe::class.java)
            register(ItemID.STONE_PICKAXE, ItemStonePickaxe::class.java)
            register(ItemID.STONE_SHOVEL, ItemStoneShovel::class.java)
            register(ItemID.STONE_SWORD, ItemStoneSword::class.java)
            register(ItemID.STRAY_SPAWN_EGG, ItemStraySpawnEgg::class.java)
            register(ItemID.STRIDER_SPAWN_EGG, ItemStriderSpawnEgg::class.java)
            register(ItemID.STRING, ItemString::class.java)
            register(ItemID.SUGAR, ItemSugar::class.java)
            register(ItemID.SUGAR_CANE, ItemSugarCane::class.java)
            register(ItemID.SUSPICIOUS_STEW, ItemSuspiciousStew::class.java)
            register(ItemID.SWEET_BERRIES, ItemSweetBerries::class.java)
            register(ItemID.TADPOLE_BUCKET, ItemTadpoleBucket::class.java)
            register(ItemID.TADPOLE_SPAWN_EGG, ItemTadpoleSpawnEgg::class.java)
            register(ItemID.TALLGRASS, ItemTallgrass::class.java)
            register(ItemID.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, ItemTideArmorTrimSmithingTemplate::class.java)
            register(ItemID.TNT_MINECART, ItemTntMinecart::class.java)
            register(ItemID.TORCHFLOWER_SEEDS, ItemTorchflowerSeeds::class.java)
            register(ItemID.TOTEM_OF_UNDYING, ItemTotemOfUndying::class.java)
            register(ItemID.TRADER_LLAMA_SPAWN_EGG, ItemTraderLlamaSpawnEgg::class.java)
            register(ItemID.TRIAL_KEY, ItemTrialKey::class.java)
            register(ItemID.TRIDENT, ItemTrident::class.java)
            register(ItemID.TROPICAL_FISH, ItemTropicalFish::class.java)
            register(ItemID.TROPICAL_FISH_BUCKET, ItemTropicalFishBucket::class.java)
            register(ItemID.TROPICAL_FISH_SPAWN_EGG, ItemTropicalFishSpawnEgg::class.java)
            register(ItemID.TURTLE_HELMET, ItemTurtleHelmet::class.java)
            register(ItemID.TURTLE_SCUTE, ItemTurtleScute::class.java)
            register(ItemID.TURTLE_SPAWN_EGG, ItemTurtleSpawnEgg::class.java)
            register(ItemID.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, ItemVexArmorTrimSmithingTemplate::class.java)
            register(ItemID.VEX_SPAWN_EGG, ItemVexSpawnEgg::class.java)
            register(ItemID.VILLAGER_SPAWN_EGG, ItemVillagerSpawnEgg::class.java)
            register(ItemID.VINDICATOR_SPAWN_EGG, ItemVindicatorSpawnEgg::class.java)
            register(ItemID.WANDERING_TRADER_SPAWN_EGG, ItemWanderingTraderSpawnEgg::class.java)
            register(ItemID.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, ItemWardArmorTrimSmithingTemplate::class.java)
            register(ItemID.WARDEN_SPAWN_EGG, ItemWardenSpawnEgg::class.java)
            register(ItemID.WARPED_FUNGUS_ON_A_STICK, ItemWarpedFungusOnAStick::class.java)
            register(ItemID.WARPED_SIGN, ItemWarpedSign::class.java)
            register(ItemID.WATER_BUCKET, ItemWaterBucket::class.java)
            register(ItemID.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, ItemWayfinderArmorTrimSmithingTemplate::class.java)
            register(ItemID.WHEAT_SEEDS, ItemWheatSeeds::class.java)
            register(ItemID.WHITE_BUNDLE, ItemWhiteBundle::class.java)
            register(ItemID.WHITE_DYE, ItemWhiteDye::class.java)
            register(ItemID.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, ItemWildArmorTrimSmithingTemplate::class.java)
            register(ItemID.WIND_CHARGE, ItemWindCharge::class.java)
            register(ItemID.WITCH_SPAWN_EGG, ItemWitchSpawnEgg::class.java)
            register(ItemID.WITHER_SKELETON_SKULL, ItemWitherSkeletonSkull::class.java)
            register(ItemID.WITHER_SKELETON_SPAWN_EGG, ItemWitherSkeletonSpawnEgg::class.java)
            register(ItemID.WITHER_SPAWN_EGG, ItemWitherSpawnEgg::class.java)
            register(ItemID.WOLF_ARMOR, ItemWolfArmor::class.java)
            register(ItemID.WOLF_SPAWN_EGG, ItemWolfSpawnEgg::class.java)
            register(ItemID.WOOD, ItemWood::class.java)
            register(ItemID.WOODEN_AXE, ItemWoodenAxe::class.java)
            register(ItemID.WOODEN_HOE, ItemWoodenHoe::class.java)
            register(ItemID.WOODEN_PICKAXE, ItemWoodenPickaxe::class.java)
            register(ItemID.WOODEN_SHOVEL, ItemWoodenShovel::class.java)
            register(ItemID.WOODEN_SLAB, ItemWoodenSlab::class.java)
            register(ItemID.WOODEN_SWORD, ItemWoodenSword::class.java)
            register(ItemID.WOOL, ItemWool::class.java)
            register(ItemID.WRITABLE_BOOK, ItemWritableBook::class.java)
            register(ItemID.WRITTEN_BOOK, ItemWrittenBook::class.java)
            register(ItemID.YELLOW_BUNDLE, ItemYellowBundle::class.java)
            register(ItemID.YELLOW_DYE, ItemYellowDye::class.java)
            register(ItemID.ZOGLIN_SPAWN_EGG, ItemZoglinSpawnEgg::class.java)
            register(ItemID.ZOMBIE_HEAD, ItemZombieHead::class.java)
            register(ItemID.ZOMBIE_HORSE_SPAWN_EGG, ItemZombieHorseSpawnEgg::class.java)
            register(ItemID.ZOMBIE_PIGMAN_SPAWN_EGG, ItemZombiePigmanSpawnEgg::class.java)
            register(ItemID.ZOMBIE_SPAWN_EGG, ItemZombieSpawnEgg::class.java)
            register(ItemID.ZOMBIE_VILLAGER_SPAWN_EGG, ItemZombieVillagerSpawnEgg::class.java)
            registerBlockItem()
        } catch (e: RegisterException) {
            throw RuntimeException(e)
        }
        loadItemComponents()
    }

    private fun loadItemComponents() {
        try {
            ItemRegistry::class.java.classLoader.getResourceAsStream("item_components.nbt").use { stream ->
                itemComponents = readCompressed(stream)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(RegisterException::class)
    private fun registerBlockItem() {
        register(BlockID.BED, ItemBed::class.java)
        register(BlockID.BIRCH_HANGING_SIGN, ItemBirchHangingSign::class.java)
        register(BlockID.ACACIA_HANGING_SIGN, ItemAcaciaHangingSign::class.java)
        register(BlockID.BAMBOO_HANGING_SIGN, ItemBambooHangingSign::class.java)
        register(BlockID.CHERRY_HANGING_SIGN, ItemCherryHangingSign::class.java)
        register(BlockID.CRIMSON_HANGING_SIGN, ItemCrimsonHangingSign::class.java)
        register(BlockID.DARK_OAK_HANGING_SIGN, ItemDarkOakHangingSign::class.java)
        register(BlockID.JUNGLE_HANGING_SIGN, ItemJungleHangingSign::class.java)
        register(BlockID.MANGROVE_HANGING_SIGN, ItemMangroveHangingSign::class.java)
        register(BlockID.OAK_HANGING_SIGN, ItemOakHangingSign::class.java)
        register(BlockID.SPRUCE_HANGING_SIGN, ItemSpruceHangingSign::class.java)
        register(BlockID.WARPED_HANGING_SIGN, ItemWarpedHangingSign::class.java)
        register(BlockID.BEETROOT, ItemBeetroot::class.java)
    }

    override fun get(key: String): Item? {
        try {
            val fastConstructor = CACHE_CONSTRUCTORS[key] ?: return null
            return fastConstructor.invoke() as Item
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    fun get(id: String?, meta: Int): Item? {
        try {
            val c = CACHE_CONSTRUCTORS[id] ?: return null
            val item = c.invoke() as Item
            item.damage = meta
            return item
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    fun get(id: String?, meta: Int, count: Int): Item? {
        try {
            val c = CACHE_CONSTRUCTORS[id] ?: return null
            val item = c.invoke() as Item
            item.setCount(count)
            item.damage = meta
            return item
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    fun get(id: String?, meta: Int, count: Int, tags: CompoundTag?): Item? {
        try {
            val c = CACHE_CONSTRUCTORS[id] ?: return null
            val item = c.invoke() as Item
            item.setCount(count)
            item.setCompoundTag(tags)
            item.damage = meta
            return item
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    fun get(id: String?, meta: Int, count: Int, tags: ByteArray?): Item? {
        try {
            val c = CACHE_CONSTRUCTORS[id] ?: return null
            val item = c.invoke() as Item
            item.setCount(count)
            if (tags != null) {
                item.setCompoundTag(tags)
            }
            item.damage = meta
            return item
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    val all: ObjectSet<String?>
        get() {
            val ids =
                CACHE_CONSTRUCTORS.keys
            ids.addAll(CUSTOM_ITEM_DEFINITIONS.keys)
            return ids
        }

    override fun trim() {
        CACHE_CONSTRUCTORS.trim()
    }

    override fun reload() {
        isLoad.set(false)
        CACHE_CONSTRUCTORS.clear()
        CUSTOM_ITEM_DEFINITIONS.clear()
        init()
    }

    @Throws(RegisterException::class)
    override fun register(key: String, value: Class<out Item?>) {
        try {
            val c = FastConstructor.create(value.getConstructor())
            if (CACHE_CONSTRUCTORS.putIfAbsent(key, c) != null) {
                throw RegisterException("This item has already been registered with the identifier: $key")
            }
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
    fun registerCustomItem(plugin: Plugin, vararg values: Class<out Item?>) {
        for (c in values) {
            registerCustomItem(plugin, c)
        }
    }


    @Throws(RegisterException::class)
    fun registerCustomItem(plugin: Plugin, value: Class<out Item?>) {
        try {
            if (CustomItem::class.java.isAssignableFrom(value)) {
                val memberLoader: FastMemberLoader =
                    IRegistry.Companion.fastMemberLoaderCache.computeIfAbsent(plugin.name) { p: String? ->
                        FastMemberLoader(plugin.pluginClassLoader)
                    }
                val c = FastConstructor.create(value.getConstructor(), memberLoader, false)
                val customItem = c.invoke(null as Any?) as CustomItem
                val key = customItem.definition!!.identifier
                if (CACHE_CONSTRUCTORS.putIfAbsent(key, c) == null) {
                    CUSTOM_ITEM_DEFINITIONS[key] = customItem.definition
                    Registries.ITEM_RUNTIMEID.registerCustomRuntimeItem(
                        RuntimeEntry(
                            key,
                            customItem.definition.getRuntimeId(),
                            true
                        )
                    )
                    val ci = customItem as Item
                    ci.setNetId(null)
                    Registries.CREATIVE.addCreativeItem(ci)
                } else {
                    throw RegisterException("This item has already been registered with the identifier: $key")
                }
            } else {
                throw RegisterException("This class does not implement the CustomItem interface and cannot be registered as a custom item!")
            }
        } catch (e: NoSuchMethodException) {
            throw RegisterException(e)
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    private fun register0(key: String, value: Class<out Item?>) {
        try {
            register(key, value)
        } catch (e: RegisterException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private val CACHE_CONSTRUCTORS = Object2ObjectOpenHashMap<String?, FastConstructor<out Item>?>()
        private val CUSTOM_ITEM_DEFINITIONS: MutableMap<String?, CustomItemDefinition?> = HashMap()
        private val isLoad = AtomicBoolean(false)

        
        private var itemComponents = CompoundTag()
    }
}
