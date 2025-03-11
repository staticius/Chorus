package org.chorus.registry

import org.chorus.Chorus
import org.chorus.entity.Entity
import org.chorus.entity.EntityID
import org.chorus.entity.custom.CustomEntity
import org.chorus.entity.item.*
import org.chorus.entity.mob.*
import org.chorus.entity.mob.animal.*
import org.chorus.entity.mob.monster.*
import org.chorus.entity.mob.monster.humanoid_monster.*
import org.chorus.entity.mob.villagers.EntityVillager
import org.chorus.entity.mob.villagers.EntityVillagerV2
import org.chorus.entity.mob.water_animal.EntityDolphin
import org.chorus.entity.mob.water_animal.EntityTadpole
import org.chorus.entity.mob.water_animal.EntityTropicalfish
import org.chorus.entity.mob.water_animal.fish.EntityCod
import org.chorus.entity.mob.water_animal.fish.EntityPufferfish
import org.chorus.entity.mob.water_animal.fish.EntitySalmon
import org.chorus.entity.projectile.*
import org.chorus.entity.projectile.abstract_arrow.EntityArrow
import org.chorus.entity.projectile.abstract_arrow.EntityThrownTrident
import org.chorus.entity.projectile.throwable.*
import org.chorus.entity.weather.EntityLightningBolt
import org.chorus.level.format.IChunk
import org.chorus.nbt.NBTIO.read
import org.chorus.nbt.NBTIO.write
import org.chorus.nbt.tag.CompoundTag
import org.chorus.plugin.Plugin
import org.chorus.registry.EntityRegistry.EntityDefinition
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.ints.IntCollection
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap


import me.sunlan.fastreflection.FastConstructor
import me.sunlan.fastreflection.FastMemberLoader
import org.jetbrains.annotations.UnmodifiableView
import java.io.BufferedInputStream
import java.nio.ByteOrder
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.set


class EntityRegistry : EntityID,
    IRegistry<EntityDefinition, Class<out Entity>?, Class<out Entity>> {
    override fun init() {
        if (isLoad.getAndSet(true)) return
        registerInternal(EntityDefinition(EntityID.CHICKEN, "", 10, true, true), EntityChicken::class.java)
        registerInternal(EntityDefinition(EntityID.COW, "", 11, true, true), EntityCow::class.java)
        registerInternal(EntityDefinition(EntityID.PIG, "", 12, true, true), EntityPig::class.java)
        registerInternal(EntityDefinition(EntityID.SHEEP, "", 13, true, true), EntitySheep::class.java)
        registerInternal(EntityDefinition(EntityID.WOLF, "", 14, true, true), EntityWolf::class.java)
        registerInternal(EntityDefinition(EntityID.VILLAGER, "", 15, false, true), EntityVillager::class.java)
        registerInternal(EntityDefinition(EntityID.MOOSHROOM, "", 16, true, true), EntityMooshroom::class.java)
        registerInternal(EntityDefinition(EntityID.SQUID, "", 17, true, true), EntitySquid::class.java)
        registerInternal(EntityDefinition(EntityID.RABBIT, "", 18, true, true), EntityRabbit::class.java)
        registerInternal(EntityDefinition(EntityID.BAT, "", 19, true, true), EntityBat::class.java)
        registerInternal(EntityDefinition(EntityID.IRON_GOLEM, "", 20, true, true), EntityIronGolem::class.java)
        registerInternal(EntityDefinition(EntityID.SNOW_GOLEM, "", 21, true, true), EntitySnowGolem::class.java)
        registerInternal(EntityDefinition(EntityID.OCELOT, "", 22, true, true), EntityOcelot::class.java)
        registerInternal(EntityDefinition(EntityID.HORSE, "", 23, true, true), EntityHorse::class.java)
        registerInternal(EntityDefinition(EntityID.DONKEY, "", 24, true, true), EntityDonkey::class.java)
        registerInternal(EntityDefinition(EntityID.MULE, "", 25, true, true), EntityMule::class.java)
        registerInternal(EntityDefinition(EntityID.SKELETON_HORSE, "", 26, true, true), EntitySkeletonHorse::class.java)
        registerInternal(EntityDefinition(EntityID.ZOMBIE_HORSE, "", 27, true, true), EntityZombieHorse::class.java)
        registerInternal(EntityDefinition(EntityID.POLAR_BEAR, "", 28, true, true), EntityPolarBear::class.java)
        registerInternal(EntityDefinition(EntityID.LLAMA, "", 29, true, true), EntityLlama::class.java)
        registerInternal(EntityDefinition(EntityID.PARROT, "", 30, true, true), EntityParrot::class.java)
        registerInternal(EntityDefinition(EntityID.DOLPHIN, "", 31, true, true), EntityDolphin::class.java)
        registerInternal(EntityDefinition(EntityID.ZOMBIE, "", 32, true, true), EntityZombie::class.java)
        registerInternal(EntityDefinition(EntityID.CREEPER, "", 33, true, true), EntityCreeper::class.java)
        registerInternal(EntityDefinition(EntityID.SKELETON, "", 34, true, true), EntitySkeleton::class.java)
        registerInternal(EntityDefinition(EntityID.SPIDER, "", 35, true, true), EntitySpider::class.java)
        registerInternal(EntityDefinition(EntityID.ZOMBIE_PIGMAN, "", 36, true, true), EntityZombiePigman::class.java)
        registerInternal(EntityDefinition(EntityID.SLIME, "", 37, true, true), EntitySlime::class.java)
        registerInternal(EntityDefinition(EntityID.ENDERMAN, "", 38, true, true), EntityEnderman::class.java)
        registerInternal(EntityDefinition(EntityID.SILVERFISH, "", 39, true, true), EntitySilverfish::class.java)
        registerInternal(EntityDefinition(EntityID.CAVE_SPIDER, "", 40, true, true), EntityCaveSpider::class.java)
        registerInternal(EntityDefinition(EntityID.GHAST, "", 41, true, true), EntityGhast::class.java)
        registerInternal(EntityDefinition(EntityID.MAGMA_CUBE, "", 42, true, true), EntityMagmaCube::class.java)
        registerInternal(EntityDefinition(EntityID.BLAZE, "", 43, true, true), EntityBlaze::class.java)
        registerInternal(
            EntityDefinition(EntityID.ZOMBIE_VILLAGER, "", 44, false, true),
            EntityZombieVillager::class.java
        )
        registerInternal(EntityDefinition(EntityID.WITCH, "", 45, true, true), EntityWitch::class.java)
        registerInternal(EntityDefinition(EntityID.STRAY, "", 46, true, true), EntityStray::class.java)
        registerInternal(EntityDefinition(EntityID.HUSK, "", 47, true, true), EntityHusk::class.java)
        registerInternal(
            EntityDefinition(EntityID.WITHER_SKELETON, "", 48, true, true),
            EntityWitherSkeleton::class.java
        )
        registerInternal(EntityDefinition(EntityID.GUARDIAN, "", 49, true, true), EntityGuardian::class.java)
        registerInternal(EntityDefinition(EntityID.ELDER_GUARDIAN, "", 50, true, true), EntityElderGuardian::class.java)
        registerInternal(EntityDefinition(EntityID.NPC, "", 51, true, true), EntityNPC::class.java)
        registerInternal(EntityDefinition(EntityID.WITHER, "", 52, true, true), EntityWither::class.java)
        registerInternal(EntityDefinition(EntityID.ENDER_DRAGON, "", 53, true, true), EntityEnderDragon::class.java)
        registerInternal(EntityDefinition(EntityID.SHULKER, "", 54, true, true), EntityShulker::class.java)
        registerInternal(EntityDefinition(EntityID.ENDERMITE, "", 55, true, true), EntityEndermite::class.java)
        //        registerInternal(new EntityDefinition(AGENT, "", 56, false, false), EntityAgent.class);
        registerInternal(EntityDefinition(EntityID.VINDICATOR, "", 57, true, true), EntityVindicator::class.java)
        registerInternal(EntityDefinition(EntityID.PHANTOM, "", 58, true, true), EntityPhantom::class.java)
        registerInternal(EntityDefinition(EntityID.RAVAGER, "", 59, true, true), EntityRavager::class.java)
        registerInternal(EntityDefinition(EntityID.ARMOR_STAND, "", 61, false, true), EntityArmorStand::class.java)
        //        registerInternal(new EntityDefinition(TRIPOD_CAMERA, "", 62, false, false), EntityTripodCamera.class);
        registerInternal(EntityDefinition(EntityID.ITEM, "", 64, false, false), EntityItem::class.java)
        registerInternal(EntityDefinition(EntityID.TNT, "", 65, false, true), EntityTnt::class.java)
        registerInternal(EntityDefinition(EntityID.FALLING_BLOCK, "", 66, false, false), EntityFallingBlock::class.java)
        registerInternal(EntityDefinition(EntityID.XP_BOTTLE, "", 68, false, true), EntityXpBottle::class.java)
        registerInternal(EntityDefinition(EntityID.XP_ORB, "", 69, false, true), EntityXpOrb::class.java)
        //        registerInternal(new EntityDefinition(EYE_OF_ENDER_SIGNAL, "", 70, false, false), EntityEyeOfEnderSignal.class);
        registerInternal(EntityDefinition(EntityID.ENDER_CRYSTAL, "", 71, false, true), EntityEnderCrystal::class.java)
        registerInternal(
            EntityDefinition(EntityID.FIREWORKS_ROCKET, "", 72, false, true),
            EntityFireworksRocket::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.THROWN_TRIDENT, "", 73, false, false),
            EntityThrownTrident::class.java
        )
        registerInternal(EntityDefinition(EntityID.TURTLE, "", 74, true, true), EntityTurtle::class.java)
        registerInternal(EntityDefinition(EntityID.CAT, "", 75, true, true), EntityCat::class.java)
        registerInternal(
            EntityDefinition(EntityID.SHULKER_BULLET, "", 76, false, false),
            EntityShulkerBullet::class.java
        )
        registerInternal(EntityDefinition(EntityID.FISHING_HOOK, "", 77, false, false), EntityFishingHook::class.java)
        registerInternal(
            EntityDefinition(EntityID.DRAGON_FIREBALL, "", 79, false, false),
            EntityDragonFireball::class.java
        )
        registerInternal(EntityDefinition(EntityID.ARROW, "", 80, false, true), EntityArrow::class.java)
        registerInternal(EntityDefinition(EntityID.SNOWBALL, "", 81, false, true), EntitySnowball::class.java)
        registerInternal(EntityDefinition(EntityID.EGG, "", 82, false, true), EntityEgg::class.java)
        registerInternal(EntityDefinition(EntityID.PAINTING, "", 83, false, false), EntityPainting::class.java)
        registerInternal(EntityDefinition(EntityID.MINECART, "", 84, false, true), EntityMinecart::class.java)
        registerInternal(EntityDefinition(EntityID.FIREBALL, "", 85, false, false), EntityFireball::class.java)
        registerInternal(EntityDefinition(EntityID.SPLASH_POTION, "", 86, false, true), EntitySplashPotion::class.java)
        registerInternal(EntityDefinition(EntityID.ENDER_PEARL, "", 87, false, false), EntityEnderPearl::class.java)
        //        registerInternal(new EntityDefinition(LEASH_KNOT, "", 88, false, true), EntityLeashKnot.class);
        registerInternal(EntityDefinition(EntityID.WITHER_SKULL, "", 89, false, false), EntityWitherSkull::class.java)
        registerInternal(EntityDefinition(EntityID.BOAT, "", 90, false, true), EntityBoat::class.java)
        registerInternal(
            EntityDefinition(EntityID.WITHER_SKULL_DANGEROUS, "", 91, false, false),
            EntityWitherSkullDangerous::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.LIGHTNING_BOLT, "", 93, false, true),
            EntityLightningBolt::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.SMALL_FIREBALL, "", 94, false, false),
            EntitySmallFireball::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.AREA_EFFECT_CLOUD, "", 95, false, false),
            EntityAreaEffectCloud::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.HOPPER_MINECART, "", 96, false, true),
            EntityHopperMinecart::class.java
        )
        registerInternal(EntityDefinition(EntityID.TNT_MINECART, "", 97, false, true), EntityTntMinecart::class.java)
        registerInternal(
            EntityDefinition(EntityID.CHEST_MINECART, "", 98, false, true),
            EntityChestMinecart::class.java
        )
        //        registerInternal(new EntityDefinition(COMMAND_BLOCK_MINECART, "", 100, false, true), EntityCommandBlockMinecart.class);
        registerInternal(
            EntityDefinition(EntityID.LINGERING_POTION, "", 101, false, false),
            EntityLingeringPotion::class.java
        )
        registerInternal(EntityDefinition(EntityID.LLAMA_SPIT, "", 102, false, false), EntityLlamaSpit::class.java)
        registerInternal(
            EntityDefinition(EntityID.EVOCATION_FANG, "", 103, false, true),
            EntityEvocationFang::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.EVOCATION_ILLAGER, "", 104, true, true),
            EntityEvocationIllager::class.java
        )
        registerInternal(EntityDefinition(EntityID.VEX, "", 105, true, true), EntityVex::class.java)
        //        registerInternal(new EntityDefinition(ICE_BOMB, "", 106, false, false), EntityIceBomb.class);
//        registerInternal(new EntityDefinition(BALLOON, "", 107, false, false), EntityBalloon.class);
        registerInternal(EntityDefinition(EntityID.PUFFERFISH, "", 108, true, true), EntityPufferfish::class.java)
        registerInternal(EntityDefinition(EntityID.SALMON, "", 109, true, true), EntitySalmon::class.java)
        registerInternal(EntityDefinition(EntityID.DROWNED, "", 110, true, true), EntityDrowned::class.java)
        registerInternal(EntityDefinition(EntityID.TROPICALFISH, "", 111, true, true), EntityTropicalfish::class.java)
        registerInternal(EntityDefinition(EntityID.COD, "", 112, true, true), EntityCod::class.java)
        registerInternal(EntityDefinition(EntityID.PANDA, "", 113, true, true), EntityPanda::class.java)
        registerInternal(EntityDefinition(EntityID.PILLAGER, "", 114, true, true), EntityPillager::class.java)
        registerInternal(EntityDefinition(EntityID.VILLAGER_V2, "", 115, true, false), EntityVillagerV2::class.java)
        registerInternal(
            EntityDefinition(EntityID.ZOMBIE_VILLAGER_V2, "", 116, true, false),
            EntityZombieVillagerV2::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.WANDERING_TRADER, "", 118, true, true),
            EntityWanderingTrader::class.java
        )
        //        registerInternal(new EntityDefinition(ELDER_GUARDIAN_GHOST, "", 120, false, true), EntityElderGuardianGhost.class);
        registerInternal(EntityDefinition(EntityID.FOX, "", 121, true, true), EntityFox::class.java)
        registerInternal(EntityDefinition(EntityID.BEE, "", 122, true, true), EntityBee::class.java)
        registerInternal(EntityDefinition(EntityID.PIGLIN, "", 123, true, true), EntityPiglin::class.java)
        registerInternal(EntityDefinition(EntityID.HOGLIN, "", 124, true, true), EntityHoglin::class.java)
        registerInternal(EntityDefinition(EntityID.STRIDER, "", 125, true, true), EntityStrider::class.java)
        registerInternal(EntityDefinition(EntityID.ZOGLIN, "", 126, true, true), EntityZoglin::class.java)
        registerInternal(EntityDefinition(EntityID.PIGLIN_BRUTE, "", 127, true, true), EntityPiglinBrute::class.java)
        registerInternal(EntityDefinition(EntityID.GOAT, "", 128, true, true), EntityGoat::class.java)
        registerInternal(EntityDefinition(EntityID.GLOW_SQUID, "", 129, true, true), EntityGlowSquid::class.java)
        registerInternal(EntityDefinition(EntityID.AXOLOTL, "", 130, true, true), EntityAxolotl::class.java)
        registerInternal(EntityDefinition(EntityID.WARDEN, "", 131, true, true), EntityWarden::class.java)
        registerInternal(EntityDefinition(EntityID.FROG, "", 132, true, true), EntityFrog::class.java)
        registerInternal(EntityDefinition(EntityID.TADPOLE, "", 133, true, true), EntityTadpole::class.java)
        registerInternal(EntityDefinition(EntityID.ALLAY, "", 134, true, true), EntityAllay::class.java)
        registerInternal(EntityDefinition(EntityID.CAMEL, "", 138, true, true), EntityCamel::class.java)
        registerInternal(EntityDefinition(EntityID.SNIFFER, "", 139, true, true), EntitySniffer::class.java)
        registerInternal(EntityDefinition(EntityID.TRADER_LLAMA, "", 157, true, true), EntityTraderLlama::class.java)
        registerInternal(EntityDefinition(EntityID.CHEST_BOAT, "", 218, false, true), EntityChestBoat::class.java)
        registerInternal(EntityDefinition(EntityID.ARMADILLO, "", 142, true, true), EntityArmadillo::class.java)
        registerInternal(EntityDefinition(EntityID.BREEZE, "", 140, true, true), EntityBreeze::class.java)
        registerInternal(
            EntityDefinition(EntityID.BREEZE_WIND_CHARGE_PROJECTILE, "", 141, false, false),
            EntityBreezeWindCharge::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.WIND_CHARGE_PROJECTILE, "", 143, false, false),
            EntityWindCharge::class.java
        )
        registerInternal(EntityDefinition(EntityID.BOGGED, "", 144, true, true), EntityBogged::class.java)
        registerInternal(EntityDefinition(EntityID.CREAKING, "", 146, true, true), EntityCreaking::class.java)

        this.rebuildTag()
    }

    fun getEntityClass(id: String): Class<out Entity>? {
        return CLASS[id]
    }

    fun getEntityClass(id: Int): Class<out Entity>? {
        return getEntityClass(RID2ID[id])
    }

    fun getEntityNetworkId(entityID: String?): Int {
        return ID2RID.getInt(entityID)
    }

    fun getEntityIdentifier(networkID: Int): String {
        return RID2ID[networkID]
    }

    fun getEntityDefinition(id: String): EntityDefinition? {
        return DEFINITIONS[id]
    }

    val customEntityDefinitions: @UnmodifiableView MutableList<EntityDefinition>
        get() = Collections.unmodifiableList(CUSTOM_ENTITY_DEFINITIONS)

    val knownEntityIds: IntCollection
        /**
         * 获得全部实体的网络id
         *
         *
         * Get the network id of all entities
         *
         * @return the known entity ids
         */
        get() = ID2RID.values

    val entityId2NetworkIdMap: Map<Int, String>
        get() = Collections.unmodifiableMap(RID2ID)

    val knownEntities: @UnmodifiableView MutableMap<String, Class<out Entity>?>
        /**
         * 获取全部已经注册的实体，包括自定义实体
         *
         *
         * Get all registered entities, including custom entities
         *
         * @return the known entities
         */
        get() = Collections.unmodifiableMap(
            CLASS
        )

    fun provideEntity(id: String, chunk: IChunk, nbt: CompoundTag, vararg args: Any?): Entity? {
        val clazz = getEntityClass(id) ?: return null

        var entity: Entity? = null
        var exceptions: MutableList<Exception?>? = null
        for (constructor in clazz.constructors) {
            if (entity != null) {
                break
            }

            if (constructor.parameterCount != (if (args == null) 2 else args.size + 2)) {
                continue
            }

            try {
                if (args == null || args.size == 0) {
                    val fastConstructor = FAST_NEW[id]!!
                    entity = fastConstructor.invoke(chunk, nbt) as Entity
                } else {
                    val objects = arrayOfNulls<Any>(args.size + 2)

                    objects[0] = chunk
                    objects[1] = nbt
                    System.arraycopy(args, 0, objects, 2, args.size)
                    entity = constructor.newInstance(*objects) as Entity
                }
            } catch (e: Exception) {
                if (exceptions == null) {
                    exceptions = ArrayList()
                }
                exceptions.add(e)
            } catch (e: Throwable) {
                if (exceptions == null) {
                    exceptions = ArrayList()
                }
                exceptions.add(RuntimeException(e))
            }
        }

        if (entity == null) {
            val cause: Exception = IllegalArgumentException(
                "Could not create an entity of identifier $id",
                if (exceptions != null && !exceptions.isEmpty()) exceptions[0] else null
            )
            if (exceptions != null && exceptions.size > 1) {
                for (i in 1..<exceptions.size) {
                    cause.addSuppressed(exceptions[i])
                }
            }
            EntityRegistry.log.error("Could not create an entity of type {} with {} args", id, args?.size ?: 0, cause)
        } else {
            return entity
        }
        return null
    }

    override fun get(key: EntityDefinition): Class<out Entity>? {
        return CLASS[key.id]
    }

    override fun trim() {
        CLASS.trim()
        FAST_NEW.trim()
        ID2RID.trim()
        DEFINITIONS.trim()
    }

    override fun reload() {
        isLoad.set(false)
        CLASS.clear()
        FAST_NEW.clear()
        ID2RID.clear()
        RID2ID.clear()
        DEFINITIONS.clear()
        CUSTOM_ENTITY_DEFINITIONS.clear()
        init()
    }

    @Throws(RegisterException::class)
    override fun register(key: EntityDefinition, value: Class<out Entity>) {
        if (CLASS.putIfAbsent(key.id, value) == null) {
            try {
                FAST_NEW[key.id] = FastConstructor.create(
                    value.getConstructor(
                        IChunk::class.java,
                        CompoundTag::class.java
                    )
                )
            } catch (e: NoSuchMethodException) {
                throw RuntimeException(e)
            }
            ID2RID.put(key.id, key.rid)
            RID2ID.put(key.rid, key.id)
            DEFINITIONS[key.id] = key
        } else {
            throw RegisterException("This Entity has already been registered with the identifier: " + key.id)
        }
    }

    /**
     * Register an entity to override internal entity.
     *
     * @param plugin   the plugin
     * @param entityId the entity id [EntityID]
     * @param value    the entity class,must extends internal entity
     * @throws RegisterException the register exception
     */
    @Throws(RegisterException::class)
    fun registerOverrideEntity(plugin: Plugin, entityId: String, value: Class<out Entity?>) {
        val key = getEntityDefinition(entityId)
        val entityClass = getEntityClass(entityId)
            ?: throw RegisterException(
                "This entity class does not override because can't find entity class from entityId {}",
                entityId
            )
        if (!entityClass.isAssignableFrom(value)) {
            throw RegisterException(
                "This entity class {} does not override the {} because is not assignable from {}!",
                entityClass.simpleName,
                value.simpleName,
                value.simpleName
            )
        }
        try {
            val memberLoader: FastMemberLoader =
                IRegistry.Companion.fastMemberLoaderCache.computeIfAbsent(plugin.name) { p: String? ->
                    FastMemberLoader(plugin.pluginClassLoader)
                }
            FAST_NEW[key!!.id] = FastConstructor.create(
                value.getConstructor(
                    IChunk::class.java,
                    CompoundTag::class.java
                ), memberLoader, false
            )
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        }
        ID2RID.put(key.id, key.rid)
        RID2ID.put(key.rid, key.id)
        DEFINITIONS[key.id] = key
    }

    /**
     * register custom entity
     */
    @Throws(RegisterException::class)
    fun registerCustomEntity(plugin: Plugin, key: CustomEntityDefinition, value: Class<out Entity>) {
        if (CustomEntity::class.java.isAssignableFrom(value)) {
            if (CLASS.putIfAbsent(key.id, value) == null) {
                try {
                    val memberLoader: FastMemberLoader =
                        IRegistry.Companion.fastMemberLoaderCache.computeIfAbsent(plugin.name) { p: String? ->
                            FastMemberLoader(plugin.pluginClassLoader)
                        }
                    FAST_NEW[key.id] = FastConstructor.create(
                        value.getConstructor(
                            IChunk::class.java,
                            CompoundTag::class.java
                        ), memberLoader, false
                    )
                } catch (e: NoSuchMethodException) {
                    throw RuntimeException(e)
                }
                val rid = RUNTIME_ID.getAndIncrement()
                ID2RID.put(key.id, rid)
                RID2ID.put(rid, key.id)
                val entityDefinition = EntityDefinition(key.id, key.bid, rid, key.hasSpawnegg, key.summonable)
                DEFINITIONS[key.id] = entityDefinition
                CUSTOM_ENTITY_DEFINITIONS.add(entityDefinition)
            } else {
                throw RegisterException("This Entity has already been registered with the identifier: " + key.id)
            }
        } else {
            throw RegisterException("This class does not implement the CustomEntity interface and cannot be registered as a custom entity!")
        }
    }

    private fun registerInternal(key: EntityDefinition, value: Class<out Entity>) {
        try {
            register(key, value)
        } catch (e: RegisterException) {
            EntityRegistry.log.error("{}", e.cause!!.message)
        }
    }

    
    class CustomEntityDefinition(val id: String, val bid: String, val hasSpawnegg: Boolean, val summonable: Boolean)

    @JvmRecord
    data class EntityDefinition(
        val id: String,
        val bid: String,
        val rid: Int,
        val hasSpawnegg: Boolean,
        val summonable: Boolean
    ) {
        fun toNBT(): CompoundTag? {
            return CompoundTag()
                .putString("bid", bid)
                .putBoolean("hasspawnegg", hasSpawnegg)
                .putString("id", id)
                .putInt("rid", rid)
                .putBoolean("summonable", summonable)
        }
    }

    val tag: ByteArray
        get() = TAG!!.clone()

    fun rebuildTag() {
        try {
            Chorus::class.java.module.getResourceAsStream("entity_identifiers.nbt").use { inputStream ->
                if (inputStream == null) {
                    throw AssertionError("Could not find entity_identifiers.nbt")
                }
                val bis = BufferedInputStream(inputStream)
                val nbt = read(bis, ByteOrder.BIG_ENDIAN, true)
                val list = nbt.getList("idlist", CompoundTag::class.java)
                for (customEntityDefinition in Registries.ENTITY.customEntityDefinitions) {
                    list!!.add(customEntityDefinition.toNBT())
                }
                nbt.putList("idlist", list)
                TAG = write(nbt, ByteOrder.BIG_ENDIAN, true)
            }
        } catch (e: Exception) {
            throw AssertionError("Error whilst loading entity_identifiers.dat", e)
        }
    }

    companion object {
        private val CLASS = Object2ObjectOpenHashMap<String, Class<out Entity>?>()
        private val FAST_NEW = Object2ObjectOpenHashMap<String, FastConstructor<out Entity>>()
        private val ID2RID = Object2IntOpenHashMap<String>()
        private val RID2ID = Int2ObjectArrayMap<String>()
        private val DEFINITIONS = Object2ObjectOpenHashMap<String, EntityDefinition?>()
        private val CUSTOM_ENTITY_DEFINITIONS: MutableList<EntityDefinition> = ArrayList()
        private val isLoad = AtomicBoolean(false)
        private var TAG: ByteArray?


        private val RUNTIME_ID = AtomicInteger(10000)
    }
}
