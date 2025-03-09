/**
 * 与发射器行为相关的类
 *
 *
 * Classes relevant to BlockDispenser behavior
 */
package org.chorus.dispenser

import org.chorus.block.BlockDispenser
import org.chorus.math.BlockFace
import org.chorus.dispenser.DefaultDispenseBehavior
import org.chorus.entity.item.EntityTnt
import org.chorus.block.BlockFlowingWater
import org.chorus.entity.item.EntityBoat
import org.chorus.item.ItemBucket
import org.chorus.block.BlockID
import org.chorus.item.ItemLavaBucket
import org.chorus.level.Sound
import org.chorus.item.ItemPowderSnowBucket
import org.chorus.level.vibration.VibrationType
import org.chorus.block.BlockLiquid
import org.chorus.item.ItemID
import org.chorus.block.BlockFlowingLava
import org.chorus.block.BlockPowderSnow
import org.chorus.math.AxisAlignedBB
import org.chorus.math.SimpleAxisAlignedBB
import org.chorus.entity.EntityShearable
import org.chorus.dispenser.DispenseBehavior
import org.chorus.inventory.EntityInventoryHolder
import org.chorus.inventory.HumanInventory
import java.util.HashMap
import org.chorus.dispenser.DispenseBehaviorRegister
import org.chorus.dispenser.ShearsDispenseBehavior
import org.chorus.dispenser.BucketDispenseBehavior
import org.chorus.dispenser.DyeDispenseBehavior
import org.chorus.dispenser.FireworksDispenseBehavior
import org.chorus.dispenser.FlintAndSteelDispenseBehavior
import org.chorus.dispenser.BoatDispenseBehavior
import org.chorus.dispenser.ChestBoatDispenseBehavior
import org.chorus.dispenser.ShulkerBoxDispenseBehavior
import org.chorus.dispenser.SpawnEggDispenseBehavior
import org.chorus.dispenser.TNTDispenseBehavior
import org.chorus.dispenser.ProjectileDispenseBehavior
import org.chorus.entity.EntityID
import org.chorus.dispenser.GlassBottleDispenseBehavior
import org.chorus.dispenser.WaterBottleDispenseBehavior
import org.chorus.dispenser.MinecartDispenseBehavior
import org.chorus.block.BlockRail
import org.chorus.item.ItemSpawnEgg
import org.chorus.entity.EntityLiving
import org.chorus.entity.item.EntityChestBoat
import org.chorus.item.ItemChestBoat
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.NBTIO
import org.chorus.entity.item.EntityFireworksRocket
import org.chorus.entity.projectile.EntityProjectile
import org.chorus.block.BlockUndyedShulkerBox
import org.chorus.item.ItemPotion

