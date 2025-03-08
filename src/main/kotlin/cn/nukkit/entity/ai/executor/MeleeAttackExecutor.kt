package cn.nukkit.entity.ai.executor

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.MemoryType
import cn.nukkit.entity.effect.*
import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause
import cn.nukkit.event.entity.EntityDamageEvent.DamageModifier
import cn.nukkit.inventory.EntityInventoryHolder
import cn.nukkit.item.*
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.math.*
import cn.nukkit.network.protocol.EntityEventPacket
import java.util.*

/**
 * 通用近战攻击执行器.
 *
 *
 * Universal melee attack actuator.
 */
open class MeleeAttackExecutor(
    protected var memory: MemoryType<out Entity?>?,
    protected var speed: Float,
    maxSenseRange: Int,
    protected var clearDataWhenLose: Boolean,
    protected var coolDown: Int,
    protected var attackRange: Float,
    vararg effects: Effect
) :
    EntityControl, IBehaviorExecutor {
    protected var maxSenseRangeSquared: Int = maxSenseRange * maxSenseRange

    protected var attackTick: Int = 0

    protected var oldTarget: Vector3? = null

    /**
     * 用来指定特定的攻击目标.
     *
     *
     * Used to specify a specific attack target.
     */
    protected var target: Entity? = null

    /**
     * 用来指定特定的视线目标
     *
     *
     * Used to specify a specific look target.
     */
    protected var lookTarget: Vector3? = null

    /**
     * 给予目标药水效果
     *
     *
     * Give target potion effect
     */
    protected var effects: Array<Effect> = effects

    constructor(
        memory: MemoryType<out Entity?>?,
        speed: Float,
        maxSenseRange: Int,
        clearDataWhenLose: Boolean,
        coolDown: Int
    ) : this(memory, speed, maxSenseRange, clearDataWhenLose, coolDown, 2.5f)

    constructor(
        memory: MemoryType<out Entity?>?,
        speed: Float,
        maxSenseRange: Int,
        clearDataWhenLose: Boolean,
        coolDown: Int,
        vararg effects: Effect?
    ) : this(memory, speed, maxSenseRange, clearDataWhenLose, coolDown, 2.5f, *effects)

    override fun execute(entity: EntityMob): Boolean {
        attackTick++
        if (entity.behaviorGroup!!.memoryStorage!!.isEmpty(memory)) return false
        val newTarget = entity.behaviorGroup!!.memoryStorage!![memory]

        //first is null
        if (this.target == null) {
            this.target = newTarget
        }
        if (this.lookTarget == null) {
            this.lookTarget = target!!.position.clone()
        }

        //some check
        if (!target!!.isAlive) return false
        else if (entity.position.distanceSquared(target!!.position) > maxSenseRangeSquared) return false
        else if (target is Player) {
            if (target.isCreative() || target.isSpectator() || !target.isOnline() || (entity.level!!.name != target.level.name)) {
                return false
            }
        }


        //update target and look target
        if (target!!.locator != newTarget!!.locator) {
            target = newTarget
        }
        if (this.lookTarget != newTarget!!.transform) {
            lookTarget = newTarget!!.transform.position
        }

        //set some motion control
        if (!entity.isEnablePitch) entity.isEnablePitch = true
        if (entity.movementSpeed != speed) entity.movementSpeed = speed
        //set target and look target
        setRouteTarget(entity, target!!.position.clone())
        setLookTarget(entity, lookTarget!!.clone())

        val floor = target!!.position.floor()
        if (oldTarget == null || oldTarget != floor) entity.behaviorGroup!!.isForceUpdateRoute = true
        oldTarget = floor

        //attack logic
        if (entity.position.distanceSquared(target!!.position) <= attackRange && attackTick > coolDown) {
            val item = if (entity is EntityInventoryHolder) entity.itemInHand else Item.AIR

            var defaultDamage = 0f
            if (entity is EntityCanAttack) {
                defaultDamage = entity.getDiffHandDamage(entity.server!!.difficulty)
            }
            var itemDamage = item.getAttackDamage(entity) + defaultDamage

            val enchantments = item.enchantments
            if (item.applyEnchantments()) {
                for (enchantment in enchantments) {
                    itemDamage += enchantment.getDamageBonus(target, entity).toFloat()
                }
            }

            val damage: MutableMap<DamageModifier, Float> = EnumMap(
                DamageModifier::class.java
            )
            damage[DamageModifier.BASE] = itemDamage

            var knockBack = 0.3f
            if (item.applyEnchantments()) {
                val knockBackEnchantment = item.getEnchantment(Enchantment.ID_KNOCKBACK)
                if (knockBackEnchantment != null) {
                    knockBack += knockBackEnchantment.level * 0.1f
                }
            }

            val ev = EntityDamageByEntityEvent(
                entity,
                target!!,
                DamageCause.ENTITY_ATTACK,
                damage,
                knockBack,
                if (item.applyEnchantments()) enchantments else null
            )

            ev.isBreakShield = item.canBreakShield()

            target!!.attack(ev)
            if (!ev.isCancelled) {
                for (e in effects) {
                    target!!.addEffect(e)
                }

                playAttackAnimation(entity)
                entity.memoryStorage!!.put<Int>(CoreMemoryTypes.Companion.LAST_ATTACK_TIME, entity.level!!.tick)
                entity.memoryStorage!!
                    .put<Entity>(CoreMemoryTypes.Companion.LAST_ATTACK_ENTITY, target)
                attackTick = 0
            }

            return target!!.health != 0f
        }
        return true
    }

    override fun onStop(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        //重置速度
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        if (clearDataWhenLose) {
            entity.behaviorGroup!!.memoryStorage!!.clear(memory)
        }
        entity.isEnablePitch = false
        this.target = null
        this.lookTarget = null
    }

    override fun onInterrupt(entity: EntityMob) {
        removeRouteTarget(entity)
        removeLookTarget(entity)
        //重置速度
        entity.movementSpeed = EntityLiving.Companion.DEFAULT_SPEED
        if (clearDataWhenLose) {
            entity.behaviorGroup!!.memoryStorage!!.clear(memory)
        }
        entity.isEnablePitch = false
        this.target = null
        this.lookTarget = null
    }

    protected fun playAttackAnimation(entity: EntityMob) {
        val pk = EntityEventPacket()
        pk.eid = entity.id
        pk.event = EntityEventPacket.ARM_SWING
        Server.broadcastPacket(entity.viewers.values, pk)
    }
}
