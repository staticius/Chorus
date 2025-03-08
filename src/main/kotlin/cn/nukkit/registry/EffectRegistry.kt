package cn.nukkit.registry

import cn.nukkit.entity.effect.*
import cn.nukkit.registry.RegisterException
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import me.sunlan.fastreflection.FastConstructor
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.Map
import kotlin.collections.set

class EffectRegistry :
    IRegistry<EffectType, Effect?, Class<out Effect?>> {
    override fun init() {
        if (isLoad.getAndSet(true)) return
        register0(EffectType.SPEED, EffectSpeed::class.java)
        register0(EffectType.SLOWNESS, EffectSlowness::class.java)
        register0(EffectType.HASTE, EffectHaste::class.java)
        register0(EffectType.MINING_FATIGUE, EffectMiningFatigue::class.java)
        register0(EffectType.STRENGTH, EffectStrength::class.java)
        register0(EffectType.INSTANT_HEALTH, EffectInstantHealth::class.java)
        register0(EffectType.INSTANT_DAMAGE, EffectInstantDamage::class.java)
        register0(EffectType.JUMP_BOOST, EffectJumpBoost::class.java)
        register0(EffectType.NAUSEA, EffectNausea::class.java)
        register0(EffectType.REGENERATION, EffectRegeneration::class.java)
        register0(EffectType.RESISTANCE, EffectResistance::class.java)
        register0(EffectType.FIRE_RESISTANCE, EffectFireResistance::class.java)
        register0(EffectType.WATER_BREATHING, EffectWaterBreathing::class.java)
        register0(EffectType.INVISIBILITY, EffectInvisibility::class.java)
        register0(EffectType.BLINDNESS, EffectBlindness::class.java)
        register0(EffectType.NIGHT_VISION, EffectNightVision::class.java)
        register0(EffectType.HUNGER, EffectHunger::class.java)
        register0(EffectType.WEAKNESS, EffectWeakness::class.java)
        register0(EffectType.POISON, EffectPoison::class.java)
        register0(EffectType.WITHER, EffectWither::class.java)
        register0(EffectType.HEALTH_BOOST, EffectHealthBoost::class.java)
        register0(EffectType.ABSORPTION, EffectAbsorption::class.java)
        register0(EffectType.SATURATION, EffectSaturation::class.java)
        register0(EffectType.LEVITATION, EffectLevitation::class.java)
        register0(EffectType.FATAL_POISON, EffectFatalPoison::class.java)
        register0(EffectType.SLOW_FALLING, EffectSlowFalling::class.java)
        register0(EffectType.CONDUIT_POWER, EffectConduitPower::class.java)
        // Effects that cannot be realized at the moment
        //register0(EffectType.BAD_OMEN, BadOmenEffect.class);
        //register0(EffectType.VILLAGE_HERO, VillageHeroEffect.class);
        register0(EffectType.DARKNESS, EffectDarkness::class.java)
    }

    override fun get(key: EffectType): Effect? {
        try {
            val fastConstructor = CACHE_CONSTRUCTORS[key] ?: return null
            return fastConstructor.invoke() as Effect
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    fun getType(stringId: String): EffectType? {
        return STRING_ID_2_TYPE[stringId]
    }

    fun getType(id: Int): EffectType? {
        return INT_ID_2_TYPE[id]
    }

    val effectStringId2TypeMap: Map<String, EffectType>
        get() = Collections.unmodifiableMap(STRING_ID_2_TYPE)

    val effectId2TypeMap: Map<Int, EffectType>
        get() = Collections.unmodifiableMap(INT_ID_2_TYPE)

    override fun trim() {
        CACHE_CONSTRUCTORS.trim()
    }

    override fun reload() {
        isLoad.set(false)
        INT_ID_2_TYPE.clear()
        STRING_ID_2_TYPE.clear()
        CACHE_CONSTRUCTORS.clear()
        init()
    }

    @Throws(RegisterException::class)
    override fun register(type: EffectType, effect: Class<out Effect?>) {
        try {
            val c = FastConstructor.create(effect.getConstructor())
            if (CACHE_CONSTRUCTORS.putIfAbsent(type, c) == null) {
                STRING_ID_2_TYPE[type.stringId] = type
                if (type.id != null) {
                    INT_ID_2_TYPE[type.id] = type
                }
            } else {
                throw RegisterException("This effect has already been registered with the identifier: $type")
            }
        } catch (e: NoSuchMethodException) {
            throw RegisterException(e)
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    private fun register0(type: EffectType, effect: Class<out Effect?>) {
        try {
            register(type, effect)
        } catch (e: RegisterException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private val CACHE_CONSTRUCTORS = Object2ObjectOpenHashMap<EffectType, FastConstructor<out Effect>?>()
        private val STRING_ID_2_TYPE = Object2ObjectOpenHashMap<String, EffectType>()
        private val INT_ID_2_TYPE = Object2ObjectOpenHashMap<Int, EffectType>()
        private val isLoad = AtomicBoolean(false)
    }
}
