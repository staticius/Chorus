/**
 * 与方块实现相关的类,基类[cn.nukkit.block.Block]
 *
 *
 * Classes relevant to block implementation, the base class is [cn.nukkit.block.Block]
 */
package org.chorus.block

import cn.nukkit.block.Block.toItem
import cn.nukkit.plugin.PluginManager.callEvent
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.level.Level.setBlock
import cn.nukkit.level.Locator.levelBlock
import cn.nukkit.level.Locator.locator
import cn.nukkit.math.Vector3.setComponents
import cn.nukkit.math.Vector3.distanceManhattan
import cn.nukkit.item.Item.isAxe
import cn.nukkit.Player.isCreative
import cn.nukkit.item.Item.useOn
import cn.nukkit.level.Level.addParticle
import cn.nukkit.item.Item.id
import cn.nukkit.block.BlockYellowCandle
import cn.nukkit.block.BlockCandle
import cn.nukkit.block.BlockProperties
import cn.nukkit.block.BlockYellowCandleCake
import cn.nukkit.block.BlockID
import cn.nukkit.block.property.CommonBlockProperties
import cn.nukkit.block.BlockCandleCake
import cn.nukkit.block.BlockYellowCarpet
import cn.nukkit.block.BlockCarpet
import cn.nukkit.block.BlockYellowConcrete
import cn.nukkit.block.BlockConcrete
import cn.nukkit.block.BlockYellowConcretePowder
import cn.nukkit.block.BlockConcretePowder
import cn.nukkit.block.BlockYellowGlazedTerracotta
import cn.nukkit.block.BlockGlazedTerracotta
import cn.nukkit.block.BlockYellowShulkerBox
import cn.nukkit.block.BlockUndyedShulkerBox
import cn.nukkit.item.ItemShulkerBox
import cn.nukkit.tags.BlockTags
import cn.nukkit.block.BlockYellowStainedGlass
import cn.nukkit.block.BlockGlassStained
import cn.nukkit.utils.DyeColor
import cn.nukkit.block.BlockYellowStainedGlassPane
import cn.nukkit.block.BlockGlassPaneStained
import cn.nukkit.block.BlockYellowTerracotta
import cn.nukkit.block.BlockHardenedClay
import cn.nukkit.block.BlockYellowWool
import cn.nukkit.block.BlockWool
import cn.nukkit.block.BlockHead
import cn.nukkit.block.BlockZombieHead
import cn.nukkit.item.ItemZombieHead
import cn.nukkit.event.block.BlockFadeEvent
import cn.nukkit.block.BlockEntityHolder
import cn.nukkit.block.property.enums.OxidizationLevel
import cn.nukkit.block.Waxable
import cn.nukkit.block.Oxidizable
import cn.nukkit.math.BlockFace
import cn.nukkit.level.particle.ScrapeParticle
import cn.nukkit.item.ItemID
import cn.nukkit.level.particle.WaxOnParticle
import cn.nukkit.level.particle.WaxOffParticle

