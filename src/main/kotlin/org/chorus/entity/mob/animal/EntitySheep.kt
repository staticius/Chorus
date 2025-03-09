package org.chorus.entity.mob.animal

import cn.nukkit.Player
import cn.nukkit.block.*
import cn.nukkit.entity.EntityID
import cn.nukkit.entity.EntityShearable
import cn.nukkit.entity.EntityWalkable
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.*
import cn.nukkit.entity.ai.executor.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.route.finder.impl.SimpleFlatAStarRouteFinder
import cn.nukkit.entity.ai.route.posevaluator.WalkingPosEvaluator
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestFeedingPlayerSensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.entity.data.EntityFlag
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.item.*
import cn.nukkit.level.Sound
import cn.nukkit.level.format.IChunk
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.DyeColor
import java.util.Set
import java.util.concurrent.*

/**
 * @author BeYkeRYkt (Nukkit Project)
 */
class EntitySheep(chunk: IChunk?, nbt: CompoundTag) : EntityAnimal(chunk, nbt), EntityWalkable, EntityShearable {
    override fun getIdentifier(): String {
        return EntityID.Companion.SHEEP
    }

    override var sheared: Boolean = false
    override var color: Int = 0

    public override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            Set.of<IBehavior>( //用于刷新InLove状态的核心行为
                Behavior(
                    InLoveExecutor(400),
                    all(
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_FEED_TIME, 0, 400),
                        PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_IN_LOVE_TIME, 6000, Int.MAX_VALUE)
                    ),
                    1, 1
                )
            ),
            Set.of<IBehavior>(
                Behavior(
                    FlatRandomRoamExecutor(0.5f, 12, 40, true, 100, true, 10),
                    PassByTimeEvaluator(CoreMemoryTypes.Companion.LAST_BE_ATTACKED_TIME, 0, 100),
                    6,
                    1
                ),
                Behavior(
                    EntityBreedingExecutor<EntitySheep>(EntitySheep::class.java, 16, 100, 0.5f),
                    { entity: EntityMob -> entity.memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.IS_IN_LOVE) },
                    5,
                    1
                ),
                Behavior(
                    MoveToTargetExecutor(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER, 0.5f, true, 8f, 1.5f),
                    MemoryCheckNotEmptyEvaluator(CoreMemoryTypes.Companion.NEAREST_FEEDING_PLAYER),
                    4,
                    1
                ),
                Behavior(
                    EatGrassExecutor(40), all(
                        any(
                            all(
                                IBehaviorEvaluator { entity: EntityMob -> entity is EntityAnimal && !entity.isBaby() },
                                ProbabilityEvaluator(1, 100)
                            ),
                            all(
                                IBehaviorEvaluator { entity: EntityMob -> entity is EntityAnimal && entity.isBaby() },
                                ProbabilityEvaluator(43, 50)
                            )
                        ),
                        any(
                            BlockCheckEvaluator(Block.GRASS_BLOCK, Vector3(0.0, -1.0, 0.0)),
                            BlockCheckEvaluator(Block.TALL_GRASS, Vector3.ZERO)
                        )
                    ),
                    3, 1, 100
                ),
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100),
                    ProbabilityEvaluator(4, 10),
                    1,
                    1,
                    100
                ),
                Behavior(
                    FlatRandomRoamExecutor(0.2f, 12, 100, false, -1, true, 10),
                    (IBehaviorEvaluator { entity: EntityMob? -> true }),
                    1,
                    1
                )
            ),
            Set.of<ISensor>(NearestFeedingPlayerSensor(8.0, 0.0), NearestPlayerSensor(8.0, 0.0, 20)),
            Set.of<IController>(WalkController(), LookController(true, true), FluctuateController()),
            SimpleFlatAStarRouteFinder(WalkingPosEvaluator(), this),
            this
        )
    }

    override fun getWidth(): Float {
        if (this.isBaby()) {
            return 0.45f
        }
        return 0.9f
    }

    override fun getHeight(): Float {
        if (isBaby()) {
            return 0.65f
        }
        return 1.3f
    }

    override fun getOriginalName(): String {
        return "Sheep"
    }


    public override fun initEntity() {
        this.maxHealth = 8
        super.initEntity()

        if (!namedTag!!.contains("Color")) {
            this.setColor(randomColor())
        } else {
            this.setColor(namedTag!!.getByte("Color").toInt())
        }

        if (!namedTag!!.contains("Sheared")) {
            namedTag!!.putByte("Sheared", 0)
        } else {
            this.sheared = namedTag!!.getBoolean("Sheared")
        }

        this.setDataFlag(EntityFlag.SHEARED, this.sheared)
    }

    override fun saveNBT() {
        super.saveNBT()

        namedTag!!.putByte("Color", this.color)
        namedTag!!.putBoolean("Sheared", this.sheared)
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        if (super.onInteract(player, item, clickedPos)) {
            return true
        }

        if (item is ItemDye) {
            this.setColor(item.dyeColor.woolData)
            return true
        }

        return item.isShears && shear()
    }

    override fun shear(): Boolean {
        if (sheared || this.isBaby()) {
            return false
        }

        this.sheared = true
        this.setDataFlag(EntityFlag.SHEARED, true)

        val woolItem = this.getWoolItem()
        woolItem.setCount(ThreadLocalRandom.current().nextInt(2) + 1)
        level!!.dropItem(this.position, woolItem)

        level!!.addSound(this.position, Sound.MOB_SHEEP_SHEAR)
        level!!.vibrationManager.callVibrationEvent(
            VibrationEvent(
                this,
                this.vector3, VibrationType.SHEAR
            )
        )
        return true
    }

    fun growWool() {
        this.setDataFlag(EntityFlag.SHEARED, false)
        this.sheared = false
    }

    override fun getDrops(): Array<Item?> {
        val woolItem = if (sheared) Item.AIR else this.getWoolItem()
        return arrayOf(Item.get((if (this.isOnFire) Item.COOKED_MUTTON else Item.MUTTON)), woolItem)
    }

    fun getColor(): Int {
        return namedTag!!.getByte("Color").toInt()
    }

    fun setColor(color: Int) {
        this.color = color
        this.setDataProperty(EntityDataTypes.Companion.COLOR, color)
        namedTag!!.putByte("Color", this.color)
    }

    fun getWoolItem(): Item {
        return when (getColor()) {
            0 -> Item.get(Block.WHITE_WOOL)
            1 -> Item.get(Block.ORANGE_WOOL)
            2 -> Item.get(Block.MAGENTA_WOOL)
            3 -> Item.get(Block.LIGHT_BLUE_WOOL)
            4 -> Item.get(Block.YELLOW_WOOL)
            5 -> Item.get(Block.LIME_WOOL)
            6 -> Item.get(Block.PINK_WOOL)
            7 -> Item.get(Block.GRAY_WOOL)
            8 -> Item.get(Block.LIGHT_GRAY_WOOL)
            9 -> Item.get(Block.CYAN_WOOL)
            10 -> Item.get(Block.PURPLE_WOOL)
            11 -> Item.get(Block.BLUE_WOOL)
            12 -> Item.get(Block.BROWN_WOOL)
            13 -> Item.get(Block.GREEN_WOOL)
            14 -> Item.get(Block.RED_WOOL)
            15 -> Item.get(Block.BLACK_WOOL)
            else -> throw IllegalStateException("Unexpected value: " + getColor())
        }
    }

    private fun randomColor(): Int {
        val random = ThreadLocalRandom.current()
        val rand = random.nextDouble(1.0, 100.0)

        if (rand <= 0.164) {
            return DyeColor.PINK.woolData
        }

        if (rand <= 15) {
            return if (random.nextBoolean()) DyeColor.BLACK.woolData else if (random.nextBoolean()) DyeColor.GRAY.woolData else DyeColor.LIGHT_GRAY.woolData
        }

        return DyeColor.WHITE.woolData
    }
}
