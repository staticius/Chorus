/**
 * 与方块实现相关的类,基类[org.chorus.block.Block]
 *
 *
 * Classes relevant to block implementation, the base class is [org.chorus.block.Block]
 */
package org.chorus.block

import org.chorus.block.Block.toItem
import org.chorus.plugin.PluginManager.callEvent
import org.chorus.event.Event.isCancelled
import org.chorus.level.Level.setBlock
import org.chorus.level.Locator.levelBlock
import org.chorus.level.Locator.locator
import org.chorus.math.Vector3.setComponents
import org.chorus.math.Vector3.distanceManhattan
import org.chorus.item.Item.isAxe
import org.chorus.Player.isCreative
import org.chorus.item.Item.useOn
import org.chorus.level.Level.addParticle
import org.chorus.item.Item.id
import org.chorus.block.BlockYellowCandle
import org.chorus.block.BlockCandle
import org.chorus.block.BlockProperties
import org.chorus.block.BlockYellowCandleCake
import org.chorus.block.BlockID
import org.chorus.block.property.CommonBlockProperties
import org.chorus.block.BlockCandleCake
import org.chorus.block.BlockYellowCarpet
import org.chorus.block.BlockCarpet
import org.chorus.block.BlockYellowConcrete
import org.chorus.block.BlockConcrete
import org.chorus.block.BlockYellowConcretePowder
import org.chorus.block.BlockConcretePowder
import org.chorus.block.BlockYellowGlazedTerracotta
import org.chorus.block.BlockGlazedTerracotta
import org.chorus.block.BlockYellowShulkerBox
import org.chorus.block.BlockUndyedShulkerBox
import org.chorus.item.ItemShulkerBox
import org.chorus.tags.BlockTags
import org.chorus.block.BlockYellowStainedGlass
import org.chorus.block.BlockGlassStained
import org.chorus.utils.DyeColor
import org.chorus.block.BlockYellowStainedGlassPane
import org.chorus.block.BlockGlassPaneStained
import org.chorus.block.BlockYellowTerracotta
import org.chorus.block.BlockHardenedClay
import org.chorus.block.BlockYellowWool
import org.chorus.block.BlockWool
import org.chorus.block.BlockHead
import org.chorus.block.BlockZombieHead
import org.chorus.item.ItemZombieHead
import org.chorus.event.block.BlockFadeEvent
import org.chorus.block.BlockEntityHolder
import org.chorus.block.property.enums.OxidizationLevel
import org.chorus.block.Waxable
import org.chorus.block.Oxidizable
import org.chorus.math.BlockFace
import org.chorus.level.particle.ScrapeParticle
import org.chorus.item.ItemID
import org.chorus.level.particle.WaxOnParticle
import org.chorus.level.particle.WaxOffParticle

