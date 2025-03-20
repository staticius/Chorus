package org.chorus.level.vibration

import org.chorus.Server
import org.chorus.entity.Entity
import org.chorus.level.*
import org.chorus.math.Vector3
import org.chorus.nbt.tag.CompoundTag
import org.chorus.network.protocol.LevelEventPacket
import org.chorus.plugin.InternalPlugin
import java.util.function.Predicate

class SimpleVibrationManager(protected var level: Level) : VibrationManager {
    protected var listeners: Set<VibrationListener> = CopyOnWriteArraySet<VibrationListener>()

    override fun callVibrationEvent(event: VibrationEvent) {
        val vibrationOccurPluginEvent: VibrationOccurEvent = VibrationOccurEvent(event)
        Server.instance.pluginManager.callEvent(vibrationOccurPluginEvent)
        if (vibrationOccurPluginEvent.isCancelled) {
            return
        }
        for (listener in listeners) {
            if ((listener.listenerVector != event.source) && listener.listenerVector.distanceSquared(event.source) <= Math.pow(
                    listener.listenRange,
                    2.0
                ) && canVibrationArrive(
                    level,
                    event.source,
                    listener.listenerVector
                ) && listener.onVibrationOccur(event)
            ) {
                this.createVibration(listener, event)
                level.scheduler.scheduleDelayedTask(InternalPlugin.INSTANCE, {
                    val vibrationArrivePluginEvent: VibrationArriveEvent = VibrationArriveEvent(event, listener)
                    Server.instance.pluginManager.callEvent(vibrationArrivePluginEvent)
                    if (vibrationArrivePluginEvent.isCancelled) {
                        return@scheduleDelayedTask
                    }
                    listener.onVibrationArrive(event)
                }, event.source.distance(listener.listenerVector).toInt())
            }
        }
    }

    override fun addListener(listener: VibrationListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: VibrationListener) {
        listeners.remove(listener)
    }

    protected fun createVibration(listener: VibrationListener, event: VibrationEvent) {
        val listenerPos: Vector3f = listener.listenerVector.asVector3f()
        val sourcePos: Vector3f = event.source.asVector3f()
        val tag = CompoundTag()
            .putCompound("origin", createVec3fTag(sourcePos))
            .putFloat("speed", 20.0f)
            .putCompound(
                "target",
                if (listener.isEntity) createEntityTargetTag(listener.asEntity()) else createVec3fTag(listenerPos)
            )
            .putFloat("timeToLive", (listenerPos.distance(sourcePos) / 20.0).toFloat())
        val packet: LevelEventGenericPacket = LevelEventGenericPacket()
        packet.eventId = LevelEventPacket.EVENT_PARTICLE_VIBRATION_SIGNAL
        packet.tag = tag
        //todo: 只对在视野范围内的玩家发包
        Server.broadcastPacket(level.players.values(), packet)
    }

    protected fun createVec3fTag(vec3f: Vector3f): CompoundTag {
        return CompoundTag()
            .putString("type", "vec3")
            .putFloat("x", vec3f.x)
            .putFloat("y", vec3f.y)
            .putFloat("z", vec3f.z)
    }

    protected fun createEntityTargetTag(entity: Entity): CompoundTag {
        return CompoundTag()
            .putString("type", "actor")
            .putLong("uniqueID", entity.getId())
            .putInt("attachPos", 3) //todo: check the use of this value :)
    }

    protected fun canVibrationArrive(level: Level, from: Vector3, to: Vector3?): Boolean {
        return VectorMath.getPassByVector3(from, to)
            .stream()
            .noneMatch(Predicate<Vector3> { vec: Vector3 ->
                level.getTickCachedBlock(vec)
                    .`is`(BlockTags.PNX_WOOL)
            })
    }
}