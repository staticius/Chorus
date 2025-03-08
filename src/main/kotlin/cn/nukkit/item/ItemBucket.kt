package cn.nukkit.item

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.Entity.Companion.createEntity
import cn.nukkit.entity.Entity.getServer
import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntityVariant
import cn.nukkit.event.player.PlayerBucketEmptyEvent
import cn.nukkit.event.player.PlayerBucketFillEvent
import cn.nukkit.level.Level
import cn.nukkit.level.Locator
import cn.nukkit.level.Sound
import cn.nukkit.level.particle.ExplodeParticle
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.*
import cn.nukkit.network.protocol.LevelSoundEventPacket
import cn.nukkit.network.protocol.UpdateBlockPacket
import cn.nukkit.utils.*

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class ItemBucket : Item {
    @JvmOverloads
    constructor(meta: Int = 0, count: Int = 1) : super(ItemID.Companion.BUCKET, meta, count)

    @JvmOverloads
    constructor(id: String, count: Int = 0) : super(id, 0, count)

    constructor(id: String, count: Int, name: String?) : super(id, 0, count, name)

    override fun internalAdjust() {
        if (this.id == ItemID.Companion.BUCKET) {
            when (damage) {
                1 -> {
                    this.id = ItemID.Companion.MILK_BUCKET
                    this.identifier = Identifier(ItemID.Companion.MILK_BUCKET)
                }

                2 -> {
                    this.id = ItemID.Companion.COD_BUCKET
                    this.identifier = Identifier(ItemID.Companion.COD_BUCKET)
                }

                3 -> {
                    this.id = ItemID.Companion.SALMON_BUCKET
                    this.identifier = Identifier(ItemID.Companion.SALMON_BUCKET)
                }

                4 -> {
                    this.id = ItemID.Companion.TROPICAL_FISH_BUCKET
                    this.identifier = Identifier(ItemID.Companion.TROPICAL_FISH_BUCKET)
                }

                5 -> {
                    this.id = ItemID.Companion.PUFFERFISH_BUCKET
                    this.identifier = Identifier(ItemID.Companion.PUFFERFISH_BUCKET)
                }

                8 -> {
                    this.id = ItemID.Companion.WATER_BUCKET
                    this.identifier = Identifier(ItemID.Companion.WATER_BUCKET)
                }

                10 -> {
                    this.id = ItemID.Companion.LAVA_BUCKET
                    this.identifier = Identifier(ItemID.Companion.LAVA_BUCKET)
                }

                11 -> {
                    this.id = ItemID.Companion.POWDER_SNOW_BUCKET
                    this.identifier = Identifier(ItemID.Companion.POWDER_SNOW_BUCKET)
                }

                12 -> {
                    this.id = ItemID.Companion.AXOLOTL_BUCKET
                    this.identifier = Identifier(ItemID.Companion.AXOLOTL_BUCKET)
                }

                13 -> {
                    this.id = ItemID.Companion.TADPOLE_BUCKET
                    this.identifier = Identifier(ItemID.Companion.TADPOLE_BUCKET)
                }

                else -> {
                    this.id = ItemID.Companion.BUCKET
                    this.identifier = Identifier(ItemID.Companion.BUCKET)
                }
            }
            this.meta = 0
            this.name = null
        }
    }

    val isEmpty: Boolean
        get() = getId() == ItemID.Companion.BUCKET && meta == 0

    val isWater: Boolean
        get() = when (getId()) {
            ItemID.Companion.COD_BUCKET, ItemID.Companion.SALMON_BUCKET, ItemID.Companion.TROPICAL_FISH_BUCKET, ItemID.Companion.PUFFERFISH_BUCKET, ItemID.Companion.WATER_BUCKET, ItemID.Companion.AXOLOTL_BUCKET, ItemID.Companion.TADPOLE_BUCKET -> true
            else -> false
        }

    val isMilk: Boolean
        get() = getId() == ItemID.Companion.MILK_BUCKET

    val isLava: Boolean
        get() = getId() == ItemID.Companion.LAVA_BUCKET

    val isPowderSnow: Boolean
        get() = getId() == ItemID.Companion.POWDER_SNOW_BUCKET

    val fishEntityId: String?
        get() = when (this.getId()) {
            ItemID.Companion.COD_BUCKET -> EntityID.COD
            ItemID.Companion.SALMON_BUCKET -> EntityID.SALMON
            ItemID.Companion.TROPICAL_FISH_BUCKET -> EntityID.TROPICALFISH
            ItemID.Companion.PUFFERFISH_BUCKET -> EntityID.PUFFERFISH
            ItemID.Companion.AXOLOTL_BUCKET -> EntityID.AXOLOTL
            ItemID.Companion.TADPOLE_BUCKET -> EntityID.TADPOLE
            else -> null
        }

    override val maxStackSize: Int
        get() = if (meta == 0 && getId() == ItemID.Companion.BUCKET) 16 else 1

    override fun canBeActivated(): Boolean {
        return true
    }

    val targetBlock: Block
        /**
         * get the placed block for this bucket
         */
        get() = Block.get(getDamageByTarget(getId()))


    override fun onActivate(
        level: Level,
        player: Player,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double
    ): Boolean {
        var target = target
        if (player.isAdventure) {
            return false
        }
        if (player.isItemCoolDownEnd(ItemID.Companion.BUCKET)) {
            player.setItemCoolDown(5, ItemID.Companion.BUCKET)
        } else {
            return false
        }

        val targetBlock = targetBlock

        if (targetBlock is BlockAir) {
            if (target !is BlockPowderSnow) {
                //                                       is liquid and state is
                if (target !is BlockLiquid || !target.isDefaultState()) {
                    target = target.getLevelBlockAtLayer(1)
                }
                if (target !is BlockLiquid || !target.isDefaultState()) {
                    target = block
                }
                if (target !is BlockLiquid || !target.isDefaultState()) {
                    target = block.getLevelBlockAtLayer(1)
                }
            }
            if ((target is BlockLiquid || target is BlockPowderSnow) && target.isDefaultState) {
                val result = if (player.isCreative) {
                    get(ItemID.Companion.BUCKET, 0, 1)
                } else if (target is BlockPowderSnow) {
                    get(ItemID.Companion.BUCKET, 11, 1)
                } else if (target is BlockFlowingLava) {
                    get(ItemID.Companion.BUCKET, 10, 1)
                } else {
                    get(ItemID.Companion.BUCKET, 8, 1)
                }
                val ev: PlayerBucketFillEvent
                player.getServer().getPluginManager().callEvent(
                    PlayerBucketFillEvent(
                        player, block, face, target,
                        this, result
                    ).also { ev = it })
                if (!ev.isCancelled) {
                    player.level!!.setBlock(target.position, target.layer, Block.get(BlockID.AIR), true, true)

                    level.vibrationManager.callVibrationEvent(
                        VibrationEvent(
                            player,
                            target.position.add(0.5, 0.5, 0.5),
                            VibrationType.FLUID_PICKUP
                        )
                    )

                    // When water is removed ensure any adjacent still water is replaced with water that can flow.
                    for (side in BlockFace.Plane.HORIZONTAL) {
                        val b = target.getSideAtLayer(0, side)
                        if (b.id == BlockID.WATER) {
                            level.setBlock(b.position, Block.get(BlockID.FLOWING_WATER))
                        } else if (b.id == BlockID.LAVA) level.setBlock(b.position, Block.get(BlockID.FLOWING_LAVA))
                    }

                    if (player.isSurvival) {
                        if (this.getCount() - 1 <= 0) {
                            player.getInventory().setItemInHand(ev.item)
                        } else {
                            val clone = this.clone()
                            clone.setCount(this.getCount() - 1)
                            player.getInventory().setItemInHand(clone)
                            if (player.getInventory().canAddItem(ev.item)) {
                                player.getInventory().addItem(ev.item)
                            } else {
                                player.dropItem(ev.item)
                            }
                            player.getInventory().sendContents(player)
                        }
                    }

                    if (target is BlockFlowingLava) {
                        level.addSound(block.position, Sound.BUCKET_FILL_LAVA)
                    } else if (target is BlockFlowingWater) {
                        level.addSound(block.position, Sound.BUCKET_FILL_WATER)
                    } else if (target is BlockPowderSnow) {
                        level.addSound(block.position, Sound.BUCKET_FILL_POWDER_SNOW)
                    }

                    return true
                } else {
                    player.getInventory().sendContents(player)
                }
            }
        } else if (targetBlock is BlockLiquid) {
            val result = get(ItemID.Companion.BUCKET, 0, 1)
            val usesWaterlogging = targetBlock.usesWaterLogging()
            val placementBlock = if (usesWaterlogging) {
                if (block.id == BlockID.BAMBOO) {
                    block
                } else if (target.waterloggingLevel > 0) {
                    target.getLevelBlockAtLayer(1)
                } else if (block.waterloggingLevel > 0) {
                    block.getLevelBlockAtLayer(1)
                } else {
                    block
                }
            } else {
                block
            }

            val ev = PlayerBucketEmptyEvent(player, placementBlock, face, target, this, result)
            val canBeFlowedInto = placementBlock.canBeFlowedInto() || placementBlock.id == BlockID.BAMBOO
            if (usesWaterlogging) {
                ev.isCancelled = placementBlock.waterloggingLevel <= 0 && !canBeFlowedInto
            } else {
                ev.isCancelled = !canBeFlowedInto
            }

            var nether = false
            if (!canBeUsedOnDimension(player.level!!.dimension)) {
                ev.isCancelled = true
                nether = !isLava
            }

            player.getServer().getPluginManager().callEvent(ev)

            if (!ev.isCancelled) {
                player.level!!.setBlock(placementBlock.position, placementBlock.layer, targetBlock, true, true)
                player.level!!.sendBlocks(
                    arrayOf(player),
                    arrayOf(target.getLevelBlockAtLayer(1)),
                    UpdateBlockPacket.FLAG_ALL_PRIORITY,
                    1
                )
                target.level.vibrationManager.callVibrationEvent(
                    VibrationEvent(
                        player,
                        target.position.add(0.5, 0.5, 0.5),
                        VibrationType.FLUID_PLACE
                    )
                )
                updateBucketItem(player, ev)

                afterUse(level, block)

                return true
            } else if (nether) { //handle the logic that the player can't use water bucket in nether
                if (!player.isCreative) {
                    this.damage = 0 // Empty bucket
                    player.getInventory().setItemInHand(this)
                }
                player.level!!.addLevelSoundEvent(target.position, LevelSoundEventPacket.SOUND_FIZZ)
                player.level!!.addParticle(ExplodeParticle(target.position.add(0.5, 1.0, 0.5)))
            } else {
                player.level!!.sendBlocks(
                    arrayOf(player),
                    arrayOf(block.getLevelBlockAtLayer(1)),
                    UpdateBlockPacket.FLAG_ALL_PRIORITY,
                    1
                )
                player.getInventory().sendContents(player)
            }
        } else if (targetBlock is BlockPowderSnow) {
            val result = get(ItemID.Companion.BUCKET, 0, 1)
            if (!target.canBeReplaced()) {
                val side = target.getSide(face)
                if (side.canBeReplaced()) {
                    target = side
                }
            }
            val ev = PlayerBucketEmptyEvent(player, targetBlock, face, target, this, result)
            if (!ev.isCancelled) {
                target.level.setBlock(target.position, targetBlock, true, true)
                player.level!!.addSound(target.position, Sound.BUCKET_FILL_POWDER_SNOW)

                updateBucketItem(player, ev)

                target.level.vibrationManager.callVibrationEvent(
                    VibrationEvent(
                        player,
                        target.position.add(0.5, 0.5, 0.5),
                        VibrationType.BLOCK_PLACE
                    )
                )
            }
        }

        return true
    }

    /**
     * update the count of bucket and set to inventory
     */
    private fun updateBucketItem(player: Player, ev: PlayerBucketEmptyEvent) {
        if (player.isSurvival) {
            if (this.getCount() - 1 <= 0) {
                player.getInventory().setItemInHand(ev.item)
            } else {
                val clone = this.clone()
                clone.setCount(this.getCount() - 1)
                player.getInventory().setItemInHand(clone)
                if (player.getInventory().canAddItem(ev.item)) {
                    player.getInventory().addItem(ev.item)
                } else {
                    player.dropItem(ev.item)
                }
            }
        }
    }

    /**
     * whether the bucket can be used in the dimension
     */
    protected fun canBeUsedOnDimension(dimension: Int): Boolean {
        return dimension != Level.DIMENSION_NETHER || (isEmpty || isLava || isMilk)
    }

    /**
     * handle logic after use bucket.
     */
    protected fun afterUse(level: Level, block: Block) {
        if (isLava) {
            level.addSound(block.position, Sound.BUCKET_EMPTY_LAVA)
        } else {
            level.addSound(block.position, Sound.BUCKET_EMPTY_WATER)
        }

        spawnFishEntity(block.add(0.5, 0.5, 0.5))
    }

    fun spawnFishEntity(spawnPos: Locator) {
        val fishEntityId = fishEntityId
        if (fishEntityId != null) {
            val fishEntity = createEntity(fishEntityId, spawnPos)
            if (fishEntity != null) if (fishEntity is EntityVariant) {
                if (namedTag != null) {
                    if (namedTag.containsInt("Variant")) {
                        fishEntity.setVariant(namedTag.getInt("Variant"))
                    }
                }
            }
            fishEntity!!.spawnToAll()
        }
    }

    override fun onClickAir(player: Player, directionVector: Vector3): Boolean {
        return getId() == ItemID.Companion.BUCKET && isMilk // Milk
    }

    companion object {
        fun getDamageByTarget(target: String): String {
            return when (target) {
                ItemID.Companion.COD_BUCKET, ItemID.Companion.SALMON_BUCKET, ItemID.Companion.TROPICAL_FISH_BUCKET, ItemID.Companion.PUFFERFISH_BUCKET, ItemID.Companion.WATER_BUCKET, ItemID.Companion.AXOLOTL_BUCKET, ItemID.Companion.TADPOLE_BUCKET -> BlockID.FLOWING_WATER
                ItemID.Companion.LAVA_BUCKET -> BlockID.FLOWING_LAVA
                ItemID.Companion.POWDER_SNOW_BUCKET -> BlockID.POWDER_SNOW
                else -> BlockID.AIR
            }
        }
    }
}
