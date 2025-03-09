package org.chorus.utils.collection

import org.chorus.Server
import org.chorus.utils.collection.AutoFreezable.FreezeStatus
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * FreezableArrayManager负责管理所有AutoFreezable的ByteArrayWrapper<br></br>
 * 这包括计算温度，冻结和解冻
 */
class FreezableArrayManager(
    val enable: Boolean,
    val cycleTick: Int,
    /**
     * 默认温度，新创建的数组温度等于此温度
     */
    val defaultTemperature: Int,
    /**
     * 冰点，当可冻结数组的温度低于冰点时有可能被冻结
     */
    val freezingPoint: Int,
    /**
     * 绝对零度，任何可冻结数组的温度都不应该低于此温度，等于此温度的可冻结数组有可能被深度冻结
     */
    val absoluteZero: Int,
    /**
     * 沸点，一个可冻结数组的温度无论如何加热都不能高于此温度
     */
    val boilingPoint: Int,
    /**
     * 熔化热，解冻后的数组温度会等于熔化热
     */
    val meltingHeat: Int,
    /**
     * 单次数组读写操作升温
     */
    val singleOperationHeat: Int,
    /**
     * 一次批量数组读写操作升温
     */
    val batchOperationHeat: Int
) {
    protected var tickArrayMap: ConcurrentHashMap<Int, WeakConcurrentSet<AutoFreezable>> =
        ConcurrentHashMap(cycleTick + 1, 0.999f)

    /**
     * 最大工作时间，如果一直压缩超出这个时间就会放弃接下来其他数组的压缩（冻结）
     */
    var maxCompressionTime: Int = 50
        private set
    private val currentArrayId = AtomicInteger(0)
    private var currentTick = 0

    fun setMaxCompressionTime(maxCompressionTime: Int): FreezableArrayManager {
        this.maxCompressionTime = maxCompressionTime
        return this
    }

    fun createByteArray(length: Int): ByteArrayWrapper {
        if (enable) {
            val tmp = FreezableByteArray(length, this)
            val set = tickArrayMap.computeIfAbsent(currentArrayId.getAndIncrement() % cycleTick) { ignore: Int? ->
                WeakConcurrentSet(
                    WeakConcurrentSet.Cleaner.MANUAL
                )
            }
            set.add(tmp)
            return tmp
        } else {
            return PureByteArray(length)
        }
    }

    fun wrapByteArray(array: ByteArray): ByteArrayWrapper {
        if (enable) {
            val tmp = FreezableByteArray(array, this)
            val set = tickArrayMap.computeIfAbsent(currentArrayId.getAndIncrement() % cycleTick) { ignore: Int? ->
                WeakConcurrentSet(
                    WeakConcurrentSet.Cleaner.MANUAL
                )
            }
            set.add(tmp)
            return tmp
        } else {
            return PureByteArray(array)
        }
    }

    fun cloneByteArray(array: ByteArray): ByteArrayWrapper {
        if (enable) {
            val tmp = FreezableByteArray(array.copyOf(array.size), this)
            val set = tickArrayMap.computeIfAbsent(currentArrayId.getAndIncrement() % cycleTick) { ignore: Int? ->
                WeakConcurrentSet(
                    WeakConcurrentSet.Cleaner.MANUAL
                )
            }
            set.add(tmp)
            return tmp
        } else {
            return PureByteArray(array.copyOf(array.size))
        }
    }

    fun tick() {
        currentTick++
        if (!enable) return
        val dt = currentTick % cycleTick
        val set = tickArrayMap[dt] ?: return
        // 冻结数组
        val start = System.currentTimeMillis()
        // 清理死引用
        CompletableFuture.runAsync({
            set.parallelForeach { e: AutoFreezable? ->
                if (e == null) return@parallelForeach
                val temp = e.temperature
                e.colder(1)
                if (temp <= freezingPoint + 1) {
                    if (System.currentTimeMillis() - start > maxCompressionTime) {
                        return@parallelForeach
                    }
                    if (e.freezeStatus == FreezeStatus.NONE || e.freezeStatus == FreezeStatus.FREEZE) {
                        if (e.temperature == absoluteZero) {
                            e.deepFreeze()
                        } else {
                            e.freeze()
                        }
                    }
                }
            }
        }, Server.getInstance().computeThreadPool).thenRun { set.clearDeadReferences() }
    }

    companion object {
        private var fallbackInstance: FreezableArrayManager? = null

        val instance: FreezableArrayManager?
            get() {
                try {
                    val server = Server.getInstance()
                    if (server != null) {
                        val tmp = server.freezableArrayManager
                        if (tmp != null) {
                            return tmp
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (fallbackInstance == null) {
                    fallbackInstance =
                        FreezableArrayManager(true, 32, 32, 0, -256, 1024, 16, 1, 32)
                    System.err.println("Cannot get FreezableArrayManager from Server instance, using a fallback instance!")
                }
                return fallbackInstance
            }
    }
}
