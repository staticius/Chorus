package org.chorus.registry


import me.sunlan.fastreflection.FastConstructor
import me.sunlan.fastreflection.FastMemberLoader
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
import org.chorus.utils.Loggable
import org.jetbrains.annotations.UnmodifiableView
import java.io.BufferedInputStream
import java.nio.ByteOrder
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.HashMap
import kotlin.collections.set


class EntityRegistry : IRegistry<EntityDefinition, Class<out Entity>?, Class<out Entity>> {
    override fun init() {
        if (isLoad.getAndSet(true)) return
        registerInternal(EntityDefinition(EntityID.CHICKEN, "", 10, hasSpawnegg = true, summonable = true), EntityChicken::class.java)
        registerInternal(EntityDefinition(EntityID.COW, "", 11, hasSpawnegg = true, summonable = true), EntityCow::class.java)
        registerInternal(EntityDefinition(EntityID.PIG, "", 12, hasSpawnegg = true, summonable = true), EntityPig::class.java)
        registerInternal(EntityDefinition(EntityID.SHEEP, "", 13, hasSpawnegg = true, summonable = true), EntitySheep::class.java)
        registerInternal(EntityDefinition(EntityID.WOLF, "", 14, hasSpawnegg = true, summonable = true), EntityWolf::class.java)
        registerInternal(EntityDefinition(EntityID.VILLAGER, "", 15, hasSpawnegg = false, summonable = true), EntityVillager::class.java)
        registerInternal(EntityDefinition(EntityID.MOOSHROOM, "", 16, hasSpawnegg = true, summonable = true), EntityMooshroom::class.java)
        registerInternal(EntityDefinition(EntityID.SQUID, "", 17, hasSpawnegg = true, summonable = true), EntitySquid::class.java)
        registerInternal(EntityDefinition(EntityID.RABBIT, "", 18, hasSpawnegg = true, summonable = true), EntityRabbit::class.java)
        registerInternal(EntityDefinition(EntityID.BAT, "", 19, hasSpawnegg = true, summonable = true), EntityBat::class.java)
        registerInternal(EntityDefinition(EntityID.IRON_GOLEM, "", 20, hasSpawnegg = true, summonable = true), EntityIronGolem::class.java)
        registerInternal(EntityDefinition(EntityID.SNOW_GOLEM, "", 21, hasSpawnegg = true, summonable = true), EntitySnowGolem::class.java)
        registerInternal(EntityDefinition(EntityID.OCELOT, "", 22, hasSpawnegg = true, summonable = true), EntityOcelot::class.java)
        registerInternal(EntityDefinition(EntityID.HORSE, "", 23, hasSpawnegg = true, summonable = true), EntityHorse::class.java)
        registerInternal(EntityDefinition(EntityID.DONKEY, "", 24, hasSpawnegg = true, summonable = true), EntityDonkey::class.java)
        registerInternal(EntityDefinition(EntityID.MULE, "", 25, hasSpawnegg = true, summonable = true), EntityMule::class.java)
        registerInternal(EntityDefinition(EntityID.SKELETON_HORSE, "", 26, hasSpawnegg = true, summonable = true), EntitySkeletonHorse::class.java)
        registerInternal(EntityDefinition(EntityID.ZOMBIE_HORSE, "", 27, hasSpawnegg = true, summonable = true), EntityZombieHorse::class.java)
        registerInternal(EntityDefinition(EntityID.POLAR_BEAR, "", 28, hasSpawnegg = true, summonable = true), EntityPolarBear::class.java)
        registerInternal(EntityDefinition(EntityID.LLAMA, "", 29, hasSpawnegg = true, summonable = true), EntityLlama::class.java)
        registerInternal(EntityDefinition(EntityID.PARROT, "", 30, hasSpawnegg = true, summonable = true), EntityParrot::class.java)
        registerInternal(EntityDefinition(EntityID.DOLPHIN, "", 31, hasSpawnegg = true, summonable = true), EntityDolphin::class.java)
        registerInternal(EntityDefinition(EntityID.ZOMBIE, "", 32, hasSpawnegg = true, summonable = true), EntityZombie::class.java)
        registerInternal(EntityDefinition(EntityID.CREEPER, "", 33, hasSpawnegg = true, summonable = true), EntityCreeper::class.java)
        registerInternal(EntityDefinition(EntityID.SKELETON, "", 34, hasSpawnegg = true, summonable = true), EntitySkeleton::class.java)
        registerInternal(EntityDefinition(EntityID.SPIDER, "", 35, hasSpawnegg = true, summonable = true), EntitySpider::class.java)
        registerInternal(EntityDefinition(EntityID.ZOMBIE_PIGMAN, "", 36, hasSpawnegg = true, summonable = true), EntityZombiePigman::class.java)
        registerInternal(EntityDefinition(EntityID.SLIME, "", 37, hasSpawnegg = true, summonable = true), EntitySlime::class.java)
        registerInternal(EntityDefinition(EntityID.ENDERMAN, "", 38, hasSpawnegg = true, summonable = true), EntityEnderman::class.java)
        registerInternal(EntityDefinition(EntityID.SILVERFISH, "", 39, hasSpawnegg = true, summonable = true), EntitySilverfish::class.java)
        registerInternal(EntityDefinition(EntityID.CAVE_SPIDER, "", 40, hasSpawnegg = true, summonable = true), EntityCaveSpider::class.java)
        registerInternal(EntityDefinition(EntityID.GHAST, "", 41, hasSpawnegg = true, summonable = true), EntityGhast::class.java)
        registerInternal(EntityDefinition(EntityID.MAGMA_CUBE, "", 42, hasSpawnegg = true, summonable = true), EntityMagmaCube::class.java)
        registerInternal(EntityDefinition(EntityID.BLAZE, "", 43, hasSpawnegg = true, summonable = true), EntityBlaze::class.java)
        registerInternal(
            EntityDefinition(EntityID.ZOMBIE_VILLAGER, "", 44, hasSpawnegg = false, summonable = true),
            EntityZombieVillager::class.java
        )
        registerInternal(EntityDefinition(EntityID.WITCH, "", 45, hasSpawnegg = true, summonable = true), EntityWitch::class.java)
        registerInternal(EntityDefinition(EntityID.STRAY, "", 46, hasSpawnegg = true, summonable = true), EntityStray::class.java)
        registerInternal(EntityDefinition(EntityID.HUSK, "", 47, hasSpawnegg = true, summonable = true), EntityHusk::class.java)
        registerInternal(
            EntityDefinition(EntityID.WITHER_SKELETON, "", 48, hasSpawnegg = true, summonable = true),
            EntityWitherSkeleton::class.java
        )
        registerInternal(EntityDefinition(EntityID.GUARDIAN, "", 49, hasSpawnegg = true, summonable = true), EntityGuardian::class.java)
        registerInternal(EntityDefinition(EntityID.ELDER_GUARDIAN, "", 50, hasSpawnegg = true, summonable = true), EntityElderGuardian::class.java)
        registerInternal(EntityDefinition(EntityID.NPC, "", 51, hasSpawnegg = true, summonable = true), EntityNPC::class.java)
        registerInternal(EntityDefinition(EntityID.WITHER, "", 52, hasSpawnegg = true, summonable = true), EntityWither::class.java)
        registerInternal(EntityDefinition(EntityID.ENDER_DRAGON, "", 53, hasSpawnegg = true, summonable = true), EntityEnderDragon::class.java)
        registerInternal(EntityDefinition(EntityID.SHULKER, "", 54, hasSpawnegg = true, summonable = true), EntityShulker::class.java)
        registerInternal(EntityDefinition(EntityID.ENDERMITE, "", 55, hasSpawnegg = true, summonable = true), EntityEndermite::class.java)
        //        registerInternal(new EntityDefinition(AGENT, "", 56, hasSpawnegg = false, summonable = false), EntityAgent.class);
        registerInternal(EntityDefinition(EntityID.VINDICATOR, "", 57, hasSpawnegg = true, summonable = true), EntityVindicator::class.java)
        registerInternal(EntityDefinition(EntityID.PHANTOM, "", 58, hasSpawnegg = true, summonable = true), EntityPhantom::class.java)
        registerInternal(EntityDefinition(EntityID.RAVAGER, "", 59, hasSpawnegg = true, summonable = true), EntityRavager::class.java)
        registerInternal(EntityDefinition(EntityID.ARMOR_STAND, "", 61, hasSpawnegg = false, summonable = true), EntityArmorStand::class.java)
        //        registerInternal(new EntityDefinition(TRIPOD_CAMERA, "", 62, hasSpawnegg = false, summonable = false), EntityTripodCamera.class);
        registerInternal(EntityDefinition(EntityID.ITEM, "", 64, hasSpawnegg = false, summonable = false), EntityItem::class.java)
        registerInternal(EntityDefinition(EntityID.TNT, "", 65, hasSpawnegg = false, summonable = true), EntityTnt::class.java)
        registerInternal(EntityDefinition(EntityID.FALLING_BLOCK, "", 66, hasSpawnegg = false, summonable = false), EntityFallingBlock::class.java)
        registerInternal(EntityDefinition(EntityID.XP_BOTTLE, "", 68, hasSpawnegg = false, summonable = true), EntityXpBottle::class.java)
        registerInternal(EntityDefinition(EntityID.XP_ORB, "", 69, hasSpawnegg = false, summonable = true), EntityXpOrb::class.java)
        //        registerInternal(new EntityDefinition(EYE_OF_ENDER_SIGNAL, "", 70, hasSpawnegg = false, summonable = false), EntityEyeOfEnderSignal.class);
        registerInternal(EntityDefinition(EntityID.ENDER_CRYSTAL, "", 71, hasSpawnegg = false, summonable = true), EntityEnderCrystal::class.java)
        registerInternal(
            EntityDefinition(EntityID.FIREWORKS_ROCKET, "", 72, hasSpawnegg = false, summonable = true),
            EntityFireworksRocket::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.THROWN_TRIDENT, "", 73, hasSpawnegg = false, summonable = false),
            EntityThrownTrident::class.java
        )
        registerInternal(EntityDefinition(EntityID.TURTLE, "", 74, hasSpawnegg = true, summonable = true), EntityTurtle::class.java)
        registerInternal(EntityDefinition(EntityID.CAT, "", 75, hasSpawnegg = true, summonable = true), EntityCat::class.java)
        registerInternal(
            EntityDefinition(EntityID.SHULKER_BULLET, "", 76, hasSpawnegg = false, summonable = false),
            EntityShulkerBullet::class.java
        )
        registerInternal(EntityDefinition(EntityID.FISHING_HOOK, "", 77, hasSpawnegg = false, summonable = false), EntityFishingHook::class.java)
        registerInternal(
            EntityDefinition(EntityID.DRAGON_FIREBALL, "", 79, hasSpawnegg = false, summonable = false),
            EntityDragonFireball::class.java
        )
        registerInternal(EntityDefinition(EntityID.ARROW, "", 80, hasSpawnegg = false, summonable = true), EntityArrow::class.java)
        registerInternal(EntityDefinition(EntityID.SNOWBALL, "", 81, hasSpawnegg = false, summonable = true), EntitySnowball::class.java)
        registerInternal(EntityDefinition(EntityID.EGG, "", 82, hasSpawnegg = false, summonable = true), EntityEgg::class.java)
        registerInternal(EntityDefinition(EntityID.PAINTING, "", 83, hasSpawnegg = false, summonable = false), EntityPainting::class.java)
        registerInternal(EntityDefinition(EntityID.MINECART, "", 84, hasSpawnegg = false, summonable = true), EntityMinecart::class.java)
        registerInternal(EntityDefinition(EntityID.FIREBALL, "", 85, hasSpawnegg = false, summonable = false), EntityFireball::class.java)
        registerInternal(EntityDefinition(EntityID.SPLASH_POTION, "", 86, hasSpawnegg = false, summonable = true), EntitySplashPotion::class.java)
        registerInternal(EntityDefinition(EntityID.ENDER_PEARL, "", 87, hasSpawnegg = false, summonable = false), EntityEnderPearl::class.java)
        //        registerInternal(new EntityDefinition(LEASH_KNOT, "", 88, hasSpawnegg = false, summonable = true), EntityLeashKnot.class);
        registerInternal(EntityDefinition(EntityID.WITHER_SKULL, "", 89, hasSpawnegg = false, summonable = false), EntityWitherSkull::class.java)
        registerInternal(EntityDefinition(EntityID.BOAT, "", 90, hasSpawnegg = false, summonable = true), EntityBoat::class.java)
        registerInternal(
            EntityDefinition(EntityID.WITHER_SKULL_DANGEROUS, "", 91, hasSpawnegg = false, summonable = false),
            EntityWitherSkullDangerous::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.LIGHTNING_BOLT, "", 93, hasSpawnegg = false, summonable = true),
            EntityLightningBolt::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.SMALL_FIREBALL, "", 94, hasSpawnegg = false, summonable = false),
            EntitySmallFireball::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.AREA_EFFECT_CLOUD, "", 95, hasSpawnegg = false, summonable = false),
            EntityAreaEffectCloud::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.HOPPER_MINECART, "", 96, hasSpawnegg = false, summonable = true),
            EntityHopperMinecart::class.java
        )
        registerInternal(EntityDefinition(EntityID.TNT_MINECART, "", 97, hasSpawnegg = false, summonable = true), EntityTntMinecart::class.java)
        registerInternal(
            EntityDefinition(EntityID.CHEST_MINECART, "", 98, hasSpawnegg = false, summonable = true),
            EntityChestMinecart::class.java
        )
        //        registerInternal(new EntityDefinition(COMMAND_BLOCK_MINECART, "", 100, hasSpawnegg = false, summonable = true), EntityCommandBlockMinecart.class);
        registerInternal(
            EntityDefinition(EntityID.LINGERING_POTION, "", 101, hasSpawnegg = false, summonable = false),
            EntityLingeringPotion::class.java
        )
        registerInternal(EntityDefinition(EntityID.LLAMA_SPIT, "", 102, hasSpawnegg = false, summonable = false), EntityLlamaSpit::class.java)
        registerInternal(
            EntityDefinition(EntityID.EVOCATION_FANG, "", 103, hasSpawnegg = false, summonable = true),
            EntityEvocationFang::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.EVOCATION_ILLAGER, "", 104, hasSpawnegg = true, summonable = true),
            EntityEvocationIllager::class.java
        )
        registerInternal(EntityDefinition(EntityID.VEX, "", 105, hasSpawnegg = true, summonable = true), EntityVex::class.java)
        //        registerInternal(new EntityDefinition(ICE_BOMB, "", 106, hasSpawnegg = false, summonable = false), EntityIceBomb.class);
//        registerInternal(new EntityDefinition(BALLOON, "", 107, hasSpawnegg = false, summonable = false), EntityBalloon.class);
        registerInternal(EntityDefinition(EntityID.PUFFERFISH, "", 108, hasSpawnegg = true, summonable = true), EntityPufferfish::class.java)
        registerInternal(EntityDefinition(EntityID.SALMON, "", 109, hasSpawnegg = true, summonable = true), EntitySalmon::class.java)
        registerInternal(EntityDefinition(EntityID.DROWNED, "", 110, hasSpawnegg = true, summonable = true), EntityDrowned::class.java)
        registerInternal(EntityDefinition(EntityID.TROPICALFISH, "", 111, hasSpawnegg = true, summonable = true), EntityTropicalfish::class.java)
        registerInternal(EntityDefinition(EntityID.COD, "", 112, hasSpawnegg = true, summonable = true), EntityCod::class.java)
        registerInternal(EntityDefinition(EntityID.PANDA, "", 113, hasSpawnegg = true, summonable = true), EntityPanda::class.java)
        registerInternal(EntityDefinition(EntityID.PILLAGER, "", 114, hasSpawnegg = true, summonable = true), EntityPillager::class.java)
        registerInternal(EntityDefinition(EntityID.VILLAGER_V2, "", 115, hasSpawnegg = true, summonable = false), EntityVillagerV2::class.java)
        registerInternal(
            EntityDefinition(EntityID.ZOMBIE_VILLAGER_V2, "", 116, hasSpawnegg = true, summonable = false),
            EntityZombieVillagerV2::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.WANDERING_TRADER, "", 118, hasSpawnegg = true, summonable = true),
            EntityWanderingTrader::class.java
        )
        //        registerInternal(new EntityDefinition(ELDER_GUARDIAN_GHOST, "", 120, hasSpawnegg = false, summonable = true), EntityElderGuardianGhost.class);
        registerInternal(EntityDefinition(EntityID.FOX, "", 121, hasSpawnegg = true, summonable = true), EntityFox::class.java)
        registerInternal(EntityDefinition(EntityID.BEE, "", 122, hasSpawnegg = true, summonable = true), EntityBee::class.java)
        registerInternal(EntityDefinition(EntityID.PIGLIN, "", 123, hasSpawnegg = true, summonable = true), EntityPiglin::class.java)
        registerInternal(EntityDefinition(EntityID.HOGLIN, "", 124, hasSpawnegg = true, summonable = true), EntityHoglin::class.java)
        registerInternal(EntityDefinition(EntityID.STRIDER, "", 125, hasSpawnegg = true, summonable = true), EntityStrider::class.java)
        registerInternal(EntityDefinition(EntityID.ZOGLIN, "", 126, hasSpawnegg = true, summonable = true), EntityZoglin::class.java)
        registerInternal(EntityDefinition(EntityID.PIGLIN_BRUTE, "", 127, hasSpawnegg = true, summonable = true), EntityPiglinBrute::class.java)
        registerInternal(EntityDefinition(EntityID.GOAT, "", 128, hasSpawnegg = true, summonable = true), EntityGoat::class.java)
        registerInternal(EntityDefinition(EntityID.GLOW_SQUID, "", 129, hasSpawnegg = true, summonable = true), EntityGlowSquid::class.java)
        registerInternal(EntityDefinition(EntityID.AXOLOTL, "", 130, hasSpawnegg = true, summonable = true), EntityAxolotl::class.java)
        registerInternal(EntityDefinition(EntityID.WARDEN, "", 131, hasSpawnegg = true, summonable = true), EntityWarden::class.java)
        registerInternal(EntityDefinition(EntityID.FROG, "", 132, hasSpawnegg = true, summonable = true), EntityFrog::class.java)
        registerInternal(EntityDefinition(EntityID.TADPOLE, "", 133, hasSpawnegg = true, summonable = true), EntityTadpole::class.java)
        registerInternal(EntityDefinition(EntityID.ALLAY, "", 134, hasSpawnegg = true, summonable = true), EntityAllay::class.java)
        registerInternal(EntityDefinition(EntityID.CAMEL, "", 138, hasSpawnegg = true, summonable = true), EntityCamel::class.java)
        registerInternal(EntityDefinition(EntityID.SNIFFER, "", 139, hasSpawnegg = true, summonable = true), EntitySniffer::class.java)
        registerInternal(EntityDefinition(EntityID.TRADER_LLAMA, "", 157, hasSpawnegg = true, summonable = true), EntityTraderLlama::class.java)
        registerInternal(EntityDefinition(EntityID.CHEST_BOAT, "", 218, hasSpawnegg = false, summonable = true), EntityChestBoat::class.java)
        registerInternal(EntityDefinition(EntityID.ARMADILLO, "", 142, hasSpawnegg = true, summonable = true), EntityArmadillo::class.java)
        registerInternal(EntityDefinition(EntityID.BREEZE, "", 140, hasSpawnegg = true, summonable = true), EntityBreeze::class.java)
        registerInternal(
            EntityDefinition(EntityID.BREEZE_WIND_CHARGE_PROJECTILE, "", 141, hasSpawnegg = false, summonable = false),
            EntityBreezeWindCharge::class.java
        )
        registerInternal(
            EntityDefinition(EntityID.WIND_CHARGE_PROJECTILE, "", 143, hasSpawnegg = false, summonable = false),
            EntityWindCharge::class.java
        )
        registerInternal(EntityDefinition(EntityID.BOGGED, "", 144, hasSpawnegg = true, summonable = true), EntityBogged::class.java)
        registerInternal(EntityDefinition(EntityID.CREAKING, "", 146, hasSpawnegg = true, summonable = true), EntityCreaking::class.java)

        this.rebuildTag()
    }

    fun getEntityClass(id: String): Class<out Entity>? {
        return CLASS[id]
    }

    fun getEntityClass(id: Int): Class<out Entity>? {
        return getEntityClass(RID2ID[id] ?: return null)
    }

    fun getEntityNetworkId(entityID: String): Int? {
        return ID2RID[entityID]
    }

    fun getEntityIdentifier(networkID: Int): String? {
        return RID2ID[networkID]
    }

    fun getEntityDefinition(id: String): EntityDefinition? {
        return DEFINITIONS[id]
    }

    val customEntityDefinitions: @UnmodifiableView MutableList<EntityDefinition>
        get() = Collections.unmodifiableList(CUSTOM_ENTITY_DEFINITIONS)

    val knownEntityIds: MutableCollection<Int>
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

            if (constructor.parameterCount != (args.size + 2)) {
                continue
            }

            try {
                if (args.isEmpty()) {
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
                if (!exceptions.isNullOrEmpty()) exceptions[0] else null
            )
            if (exceptions != null && exceptions.size > 1) {
                for (i in 1..<exceptions.size) {
                    cause.addSuppressed(exceptions[i])
                }
            }
            EntityRegistry.log.error("Could not create an entity of type {} with {} args", id, args.size, cause)
        } else {
            return entity
        }
        return null
    }

    override fun get(key: EntityDefinition): Class<out Entity>? {
        return CLASS[key.id]
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
            ID2RID[key.id] = key.rid
            RID2ID[key.rid] = key.id
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
        ID2RID[key.id] = key.rid
        RID2ID[key.rid] = key.id
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
                        IRegistry.fastMemberLoaderCache.computeIfAbsent(plugin.name) { p: String? ->
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
                ID2RID[key.id] = rid
                RID2ID[rid] = key.id
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
        fun toNBT(): CompoundTag {
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
                    list.add(customEntityDefinition.toNBT())
                }
                nbt.putList("idlist", list)
                TAG = write(nbt, ByteOrder.BIG_ENDIAN, true)
            }
        } catch (e: Exception) {
            throw AssertionError("Error whilst loading entity_identifiers.dat", e)
        }
    }

    companion object : Loggable {
        private val CLASS = HashMap<String, Class<out Entity>>()
        private val FAST_NEW = HashMap<String, FastConstructor<out Entity>>()
        private val ID2RID = HashMap<String, Int>()
        private val RID2ID = HashMap<Int, String>()
        private val DEFINITIONS = HashMap<String, EntityDefinition>()
        private val CUSTOM_ENTITY_DEFINITIONS: MutableList<EntityDefinition> = ArrayList()
        private val isLoad = AtomicBoolean(false)
        private var TAG: ByteArray? = null


        private val RUNTIME_ID = AtomicInteger(10000)
    }
}
