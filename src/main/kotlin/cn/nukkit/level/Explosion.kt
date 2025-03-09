package cn.nukkit.level

import cn.nukkit.block.*
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.blockentity.BlockEntity.onUpdate
import cn.nukkit.entity.Entity
import cn.nukkit.entity.Entity.onUpdate
import cn.nukkit.event.Event.isCancelled
import cn.nukkit.event.block.BlockEvent.getBlock
import cn.nukkit.event.block.BlockExplodeEvent.affectedBlocks
import cn.nukkit.event.entity.EntityExplodeEvent.blockList
import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.item.Item.getBlock
import cn.nukkit.math.NukkitMath
import cn.nukkit.math.Vector3
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.LevelEventPacket
import cn.nukkit.utils.*
import it.unimi.dsi.fastutil.longs.LongArraySet
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Angelic47 (Nukkit Project)
 */
class Explosion protected constructor(private val source: Locator, size: Double, private val what: Any) {
    private val RAYS = 16 //Rays
    private val STEP_LEN = 0.3

    private val level = source.getLevel()
    private val size = Math.max(size, 0.0)
    @JvmField
    var fireChance: Double = 0.0

    private var affectedBlocks: MutableSet<Block>? = null
    private var fireIgnitions: Set<Block>? = null
    private var doesDamage = true

    constructor(center: Locator, size: Double, what: Entity) : this(center, size, what as Any)

    constructor(center: Locator, size: Double, what: Block) : this(center, size, what as Any)

    var isIncendiary: Boolean
        get() = fireChance > 0
        set(incendiary) {
            if (!incendiary) {
                fireChance = 0.0
            } else if (fireChance <= 0) {
                fireChance = 1.0 / 3.0
            }
        }

    /**
     * @return bool
     */
    fun explode(): Boolean {
        if (explodeA()) {
            return explodeB()
        }
        return false
    }

    /**
     * Calculates which blocks will be destroyed by this explosion. If [.explodeB] is called without calling this,
     * no blocks will be destroyed.
     *
     * @return `true` if success
     */
    fun explodeA(): Boolean {
        if (what is EntityExplosive) {
            val entity = what as Entity
            val blockLayer0 = level.getBlock(entity.position.floor())
            val blockLayer1 = level.getBlock(entity.position.floor(), 1)
            if (BlockID.FLOWING_WATER == blockLayer0!!.id
                || BlockID.WATER == blockLayer0!!.id
                || BlockID.FLOWING_WATER == blockLayer1!!.id
                || BlockID.WATER == blockLayer1!!.id
            ) {
                this.doesDamage = false
                return true
            }
        }

        if (this.size < 0.1) {
            return false
        }

        if (affectedBlocks == null) {
            affectedBlocks = LinkedHashSet()
        }

        val incendiary = fireChance > 0
        if (incendiary && fireIgnitions == null) {
            fireIgnitions = LinkedHashSet()
        }

        val random = ThreadLocalRandom.current()

        val vector = Vector3(0.0, 0.0, 0.0)
        val vBlock = Vector3(0.0, 0.0, 0.0)

        val mRays = this.RAYS - 1
        for (i in 0..<this.RAYS) {
            for (j in 0..<this.RAYS) {
                for (k in 0..<this.RAYS) {
                    if (i == 0 || i == mRays || j == 0 || j == mRays || k == 0 || k == mRays) {
                        vector.setComponents(
                            i.toDouble() / mRays.toDouble() * 2.0 - 1,
                            j.toDouble() / mRays.toDouble() * 2.0 - 1,
                            k.toDouble() / mRays.toDouble() * 2.0 - 1
                        )
                        val len = vector.length()
                        vector.setComponents(
                            (vector.x / len) * this.STEP_LEN,
                            (vector.y / len) * this.STEP_LEN,
                            (vector.z / len) * this.STEP_LEN
                        )
                        var pointerX = source.position.x
                        var pointerY = source.position.y
                        var pointerZ = source.position.z

                        var blastForce = this.size * (random.nextInt(700, 1301)) / 1000.0
                        while (blastForce > 0) {
                            val x = pointerX.toInt()
                            val y = pointerY.toInt()
                            val z = pointerZ.toInt()
                            vBlock.x = (if (pointerX >= x) x else x - 1).toDouble()
                            vBlock.y = (if (pointerY >= y) y else y - 1).toDouble()
                            vBlock.z = (if (pointerZ >= z) z else z - 1).toDouble()
                            if (!level.isYInRange(vBlock.y.toInt())) {
                                break
                            }
                            val block = level.getBlock(vBlock)

                            if (!block!!.isAir) {
                                val layer1 = block!!.getLevelBlockAtLayer(1)
                                val resistance = Math.max(block!!.resistance, layer1.resistance)
                                blastForce -= (resistance / 5 + 0.3) * this.STEP_LEN
                                if (blastForce > 0) {
                                    if (affectedBlocks!!.add(block!!)) {
                                        if (incendiary && random.nextDouble() <= fireChance) {
                                            fireIgnitions.add(block)
                                        }
                                        if (!layer1.isAir) {
                                            affectedBlocks!!.add(layer1)
                                        }
                                    }
                                }
                            }
                            pointerX += vector.x
                            pointerY += vector.y
                            pointerZ += vector.z
                            blastForce -= this.STEP_LEN * 0.75
                        }
                    }
                }
            }
        }

        return true
    }

    /**
     * Executes the explosion's effects on the world. This includes destroying blocks (if any),
     * harming and knocking back entities, and creating sounds and particles.
     *
     * @return `false` if explosion was canceled, otherwise `true`
     */
    fun explodeB(): Boolean {
        val updateBlocks = LongArraySet()
        val send: MutableList<Vector3> = ArrayList()

        val source = (Vector3(
            source.position.x,
            source.position.y, source.position.z
        )).floor()
        var yield = (1.0 / this.size) * 100.0

        if (affectedBlocks == null) {
            affectedBlocks = LinkedHashSet()
        }

        if (what is Entity) {
            val affectedBlocksList: List<Block> = ArrayList(this.affectedBlocks)
            val ev: EntityExplodeEvent = EntityExplodeEvent(
                what,
                this.source, affectedBlocksList, yield
            )
            ev.setIgnitions(if (fireIgnitions == null) LinkedHashSet<E>(0) else fireIgnitions)
            level.server.pluginManager.callEvent(ev)
            if (ev.isCancelled) {
                return false
            } else {
                yield = ev.yield
                affectedBlocks!!.clear()
                affectedBlocks!!.addAll(ev.blockList)
                fireIgnitions = ev.ignitions
            }
        } else if (what is Block) {
            val ev: BlockExplodeEvent = BlockExplodeEvent(
                what, this.source, this.affectedBlocks,
                if (fireIgnitions == null) LinkedHashSet<Block>(0) else fireIgnitions, yield, this.fireChance
            )
            level.server.pluginManager.callEvent(ev)
            if (ev.isCancelled) {
                return false
            } else {
                yield = ev.yield
                affectedBlocks = ev.affectedBlocks
                fireIgnitions = ev.ignitions
            }
        }

        val explosionSize = this.size * 2.0
        val minX = NukkitMath.floorDouble(this.source.position.x - explosionSize - 1).toDouble()
        val maxX = NukkitMath.ceilDouble(this.source.position.x + explosionSize + 1).toDouble()
        val minY = NukkitMath.floorDouble(this.source.position.y - explosionSize - 1).toDouble()
        val maxY = NukkitMath.ceilDouble(this.source.position.y + explosionSize + 1).toDouble()
        val minZ = NukkitMath.floorDouble(this.source.position.z - explosionSize - 1).toDouble()
        val maxZ = NukkitMath.ceilDouble(this.source.position.z + explosionSize + 1).toDouble()

        val explosionBB: AxisAlignedBB = SimpleAxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)
        val list = level.getNearbyEntities(explosionBB, if (what is Entity) what else null)
        for (entity in list!!) {
            val distance = entity.position.distance(this.source.position) / explosionSize

            if (distance <= 1) {
                val motion = entity.position.subtract(this.source.position).normalize()

                val blockDensity = level.getBlockDensity(this.source.position, entity.boundingBox)
                val force = this.size * 2.0f
                val d = entity.position.distance(source) / force
                val impact = (1.0 - d) * blockDensity
                val entityDamageAmount = ((impact * impact + impact).toFloat() / 2.0 * 7.0 * force + 1.0).toFloat()
                val damage = if (this.doesDamage) entityDamageAmount else 0f

                if (what is Entity) {
                    entity.attack(EntityDamageByEntityEvent(what, entity, DamageCause.ENTITY_EXPLOSION, damage))
                } else if (what is Block) {
                    entity.attack(EntityDamageByBlockEvent(what, entity, DamageCause.BLOCK_EXPLOSION, damage))
                } else {
                    entity.attack(EntityDamageEvent(entity, DamageCause.BLOCK_EXPLOSION, damage))
                }

                if (!(entity is EntityItem || entity is EntityXpOrb)) {
                    val multipliedMotion = motion.multiply(impact)
                    entity.motion.x += multipliedMotion.x
                    entity.motion.y += multipliedMotion.y
                    entity.motion.z += multipliedMotion.z
                }
            }
        }

        val air: ItemBlock = ItemBlock(Block.get(BlockID.AIR))
        var container: BlockEntity?
        val smokePositions = if (affectedBlocks!!.isEmpty()) Collections.emptyList() else ObjectArrayList<Vector3>()
        val random = ThreadLocalRandom.current()

        for (block in affectedBlocks!!) {
            if (block is BlockTnt) {
                block.prime(random.nextInt(10, 31), if (what is Entity) what else null)
            } else if ((block.getLevel().getBlockEntity(block.position).also { container = it }) is InventoryHolder) {
                if (container is BlockEntityShulkerBox) {
                    level.dropItem(block.position.add(0.5, 0.5, 0.5), block.toItem())
                    inventoryHolder.inventory.clearAll()
                } else {
                    for (drop in inventoryHolder.inventory.contents.values()) {
                        level.dropItem(block.position.add(0.5, 0.5, 0.5), drop)
                    }
                    inventoryHolder.inventory.clearAll()
                }
            } else if (random.nextDouble() * 100 < yield) {
                for (drop in block.getDrops(air)) {
                    level.dropItem(block.position.add(0.5, 0.5, 0.5), drop)
                }
            }

            if (random.nextInt(8) == 0) {
                smokePositions.add(block.position)
            }

            level.setBlock(
                Vector3(
                    block.position.x.toInt().toDouble(),
                    block.position.y.toInt().toDouble(),
                    block.position.z.toInt().toDouble()
                ),
                block.layer, Block.get(BlockID.AIR)
            )

            if (block.layer != 0) {
                continue
            }

            val pos = Vector3(block.position.x, block.position.y, block.position.z)

            for (side in BlockFace.entries) {
                val sideBlock = pos.getSide(side)
                val index = Hash.hashBlock(sideBlock.x.toInt(), sideBlock.y.toInt(), sideBlock.z.toInt())
                if (!affectedBlocks!!.contains(sideBlock) && !updateBlocks.contains(index)) {
                    var ev: BlockUpdateEvent = BlockUpdateEvent(level.getBlock(sideBlock))
                    level.server.pluginManager.callEvent(ev)
                    if (!ev.isCancelled) {
                        ev.getBlock().onUpdate(Level.Companion.BLOCK_UPDATE_NORMAL)
                    }
                    val layer1 = level.getBlock(sideBlock, 1)
                    if (!layer1!!.isAir) {
                        ev = BlockUpdateEvent(layer1)
                        level.server.pluginManager.callEvent(ev)
                        if (!ev.isCancelled) {
                            ev.getBlock().onUpdate(Level.Companion.BLOCK_UPDATE_NORMAL)
                        }
                    }
                    updateBlocks.add(index)
                }
            }
            send.add(Vector3(block.position.x - source.x, block.position.y - source.y, block.position.z - source.z))
        }

        if (fireIgnitions != null) {
            for (remainingPos in fireIgnitions!!) {
                val toIgnite = level.getBlock(remainingPos.position)
                if (toIgnite!!.isAir && toIgnite!!.down().isSolid(BlockFace.UP)) {
                    level.setBlock(toIgnite!!.position, Block.get(BlockID.FIRE))
                }
            }
        }

        val count: Int = smokePositions.size()
        val data = CompoundTag(Object2ObjectOpenHashMap(count, 0.999999f))
            .putFloat("originX", this.source.position.x.toFloat())
            .putFloat("originY", this.source.position.y.toFloat())
            .putFloat("originZ", this.source.position.z.toFloat())
            .putFloat("radius", size.toFloat())
            .putInt("size", count)
        for (i in 0..<count) {
            val pos = smokePositions[i]
            val prefix = "pos$i"
            data.putFloat(prefix + "x", pos.x.toFloat())
            data.putFloat(prefix + "y", pos.y.toFloat())
            data.putFloat(prefix + "z", pos.z.toFloat())
        }
        level.addSound(this.source.position, Sound.RANDOM_EXPLODE)
        level.addLevelEvent(
            this.source.position, LevelEventPacket.EVENT_PARTICLE_EXPLOSION, Math.round(
                size.toFloat()
            )
        )
        level.addLevelEvent(this.source.position, LevelEventPacket.EVENT_PARTICLE_BLOCK_EXPLOSION, data)

        return true
    }
}
