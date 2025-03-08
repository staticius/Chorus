/**
 * 与发射器行为相关的类
 *
 *
 * Classes relevant to BlockDispenser behavior
 */
package cn.nukkit.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.math.BlockFace
import cn.nukkit.dispenser.DefaultDispenseBehavior
import cn.nukkit.entity.item.EntityTnt
import cn.nukkit.block.BlockFlowingWater
import cn.nukkit.entity.item.EntityBoat
import cn.nukkit.item.ItemBucket
import cn.nukkit.block.BlockID
import cn.nukkit.item.ItemLavaBucket
import cn.nukkit.level.Sound
import cn.nukkit.item.ItemPowderSnowBucket
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.block.BlockLiquid
import cn.nukkit.item.ItemID
import cn.nukkit.block.BlockFlowingLava
import cn.nukkit.block.BlockPowderSnow
import cn.nukkit.math.AxisAlignedBB
import cn.nukkit.math.SimpleAxisAlignedBB
import cn.nukkit.entity.EntityShearable
import cn.nukkit.dispenser.DispenseBehavior
import cn.nukkit.inventory.EntityInventoryHolder
import cn.nukkit.inventory.HumanInventory
import java.util.HashMap
import cn.nukkit.dispenser.DispenseBehaviorRegister
import cn.nukkit.dispenser.ShearsDispenseBehavior
import cn.nukkit.dispenser.BucketDispenseBehavior
import cn.nukkit.dispenser.DyeDispenseBehavior
import cn.nukkit.dispenser.FireworksDispenseBehavior
import cn.nukkit.dispenser.FlintAndSteelDispenseBehavior
import cn.nukkit.dispenser.BoatDispenseBehavior
import cn.nukkit.dispenser.ChestBoatDispenseBehavior
import cn.nukkit.dispenser.ShulkerBoxDispenseBehavior
import cn.nukkit.dispenser.SpawnEggDispenseBehavior
import cn.nukkit.dispenser.TNTDispenseBehavior
import cn.nukkit.dispenser.ProjectileDispenseBehavior
import cn.nukkit.entity.EntityID
import cn.nukkit.dispenser.GlassBottleDispenseBehavior
import cn.nukkit.dispenser.WaterBottleDispenseBehavior
import cn.nukkit.dispenser.MinecartDispenseBehavior
import cn.nukkit.block.BlockRail
import cn.nukkit.item.ItemSpawnEgg
import cn.nukkit.entity.EntityLiving
import cn.nukkit.entity.item.EntityChestBoat
import cn.nukkit.item.ItemChestBoat
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.NBTIO
import cn.nukkit.entity.item.EntityFireworksRocket
import cn.nukkit.entity.projectile.EntityProjectile
import cn.nukkit.block.BlockUndyedShulkerBox
import cn.nukkit.item.ItemPotion

