package org.chorus.utils.collection

import cn.nukkit.utils.collection.AutoFreezable.FreezeStatus
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max
import kotlin.math.min

class FreezableByteArray : ByteArrayWrapper, AutoFreezable {
    val manager: FreezableArrayManager
    override val freezeStatus: AtomicReference<FreezeStatus> = AtomicReference(FreezeStatus.NONE)
    override var temperature: Int
        private set
    private val rawLength: Int
    private var data: ByteArray

    internal constructor(length: Int, manager: FreezableArrayManager) {
        this.rawLength = length
        this.data = ByteArray(length)
        this.manager = manager
        this.temperature = manager.defaultTemperature
    }

    internal constructor(src: ByteArray, manager: FreezableArrayManager) {
        this.rawLength = src.size
        this.data = src
        this.manager = manager
        this.temperature = manager.defaultTemperature
    }

    override fun getFreezeStatus(): FreezeStatus {
        return freezeStatus.get()
    }

    override fun warmer(temperature: Int) {
        this.temperature = min(manager.boilingPoint.toDouble(), (this.temperature + temperature).toDouble()).toInt()
    }

    override fun colder(temperature: Int) {
        this.temperature = max(manager.absoluteZero.toDouble(), (this.temperature - temperature).toDouble()).toInt()
    }

    override fun freeze() {
        if (temperature > manager.freezingPoint) return
        if (freezeStatus.get() != FreezeStatus.NONE) return
        freezeStatus.set(FreezeStatus.FREEZING)
        data = LZ4Freezer.compressor.compress(data)
        freezeStatus.set(FreezeStatus.FREEZE)
    }

    override fun deepFreeze() {
        if (temperature > manager.absoluteZero) return
        if (freezeStatus.get() != FreezeStatus.NONE || freezeStatus.get() != FreezeStatus.FREEZE) return
        val needDecompressFirst = freezeStatus.get() == FreezeStatus.FREEZE
        freezeStatus.set(FreezeStatus.DEEP_FREEZING)
        val tmp = if (needDecompressFirst) LZ4Freezer.decompressor.decompress(data, rawLength) else data
        data = LZ4Freezer.deepCompressor.compress(tmp)
        freezeStatus.set(FreezeStatus.DEEP_FREEZE)
    }

    override fun thaw() {
        while (freezeStatus.get() == FreezeStatus.THAWING || freezeStatus.get() == FreezeStatus.FREEZING || freezeStatus.get() == FreezeStatus.DEEP_FREEZING) {
            try {
                Thread.sleep(0) // Put a safe-point here
            } catch (ignore: InterruptedException) {
            }
        }
        if (freezeStatus.get() == FreezeStatus.FREEZE || freezeStatus.get() == FreezeStatus.DEEP_FREEZE) {
            data = LZ4Freezer.decompressor.decompress(data, rawLength)
            freezeStatus.set(FreezeStatus.NONE)
        }
        if (temperature < manager.meltingHeat) temperature = manager.meltingHeat
    }

    override var rawBytes: ByteArray
        get() {
            while (freezeStatus.get() == FreezeStatus.THAWING || freezeStatus.get() == FreezeStatus.FREEZING || freezeStatus.get() == FreezeStatus.DEEP_FREEZING) {
                try {
                    Thread.sleep(0) // Put a safe-point here
                } catch (ignore: InterruptedException) {
                }
            }
            if (freezeStatus.get() != FreezeStatus.NONE) {
                thaw()
            }
            warmer(manager.batchOperationHeat)
            return data
        }
        set(bytes) {
            while (freezeStatus.get() == FreezeStatus.THAWING || freezeStatus.get() == FreezeStatus.FREEZING || freezeStatus.get() == FreezeStatus.DEEP_FREEZING) {
                try {
                    Thread.sleep(0) // Put a safe-point here
                } catch (ignore: InterruptedException) {
                }
            }
            data = bytes
            freezeStatus.set(FreezeStatus.NONE)
        }

    override fun getByte(index: Int): Byte {
        while (freezeStatus.get() == FreezeStatus.THAWING || freezeStatus.get() == FreezeStatus.FREEZING || freezeStatus.get() == FreezeStatus.DEEP_FREEZING) {
            try {
                Thread.sleep(0) // Put a safe-point here
            } catch (ignore: InterruptedException) {
            }
        }
        if (freezeStatus.get() != FreezeStatus.NONE) {
            thaw()
        }
        warmer(manager.singleOperationHeat)
        return data[index]
    }

    override fun setByte(index: Int, b: Byte) {
        while (freezeStatus.get() == FreezeStatus.THAWING || freezeStatus.get() == FreezeStatus.FREEZING || freezeStatus.get() == FreezeStatus.DEEP_FREEZING) {
            try {
                Thread.sleep(0) // Put a safe-point here
            } catch (ignore: InterruptedException) {
            }
        }
        if (freezeStatus.get() != FreezeStatus.NONE) {
            thaw()
        }
        warmer(manager.singleOperationHeat)
        data[index] = b
    }
}
