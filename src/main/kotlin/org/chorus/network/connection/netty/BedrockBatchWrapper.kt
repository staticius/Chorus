package org.chorus.network.connection.netty

import io.netty.buffer.ByteBuf
import io.netty.util.AbstractReferenceCounted
import io.netty.util.ReferenceCountUtil
import io.netty.util.ReferenceCounted
import io.netty.util.internal.ObjectPool
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import org.chorus.network.connection.util.BatchFlag
import org.chorus.network.protocol.DataPacket
import org.chorus.network.protocol.types.CompressionAlgorithm
import java.util.function.Consumer

class BedrockBatchWrapper private constructor(private val handle: ObjectPool.Handle<BedrockBatchWrapper>) :
    AbstractReferenceCounted() {
    var compressed: ByteBuf? = null
    private var algorithm: CompressionAlgorithm? = null

    var uncompressed: ByteBuf? = null
    private val packets: MutableList<BedrockPacketWrapper?> = ObjectArrayList()

    private var modified = false
    private val flags: MutableSet<BatchFlag> = ObjectOpenHashSet()

    override fun deallocate() {
        packets.forEach(Consumer { msg: BedrockPacketWrapper? -> ReferenceCountUtil.safeRelease(msg) })
        ReferenceCountUtil.safeRelease(this.uncompressed)
        ReferenceCountUtil.safeRelease(this.compressed)
        this.compressed = null
        this.uncompressed = null
        packets.clear()
        this.modified = false
        this.algorithm = null
        flags.clear()
        handle.recycle(this)
    }

    fun addPacket(wrapper: BedrockPacketWrapper?) {
        packets.add(wrapper)
        this.modify()
    }

    fun modify() {
        this.modified = true
    }

    fun setCompressed(compressed: ByteBuf?) {
        if (this.compressed != null) {
            this.compressed!!.release()
        }

        this.compressed = compressed
        if (compressed == null) {
            this.algorithm = null
        }
    }

    fun setCompressed(compressed: ByteBuf?, algorithm: CompressionAlgorithm?) {
        if (this.compressed != null) {
            this.compressed!!.release()
        }

        this.compressed = compressed
        this.algorithm = algorithm
    }

    fun setUncompressed(uncompressed: ByteBuf?) {
        if (this.uncompressed != null) {
            this.uncompressed!!.release()
        }
        this.uncompressed = uncompressed
    }

    fun setFlag(flag: BatchFlag) {
        flags.add(flag)
    }

    fun hasFlag(flag: BatchFlag): Boolean {
        return flags.contains(flag)
    }

    fun unsetFlag(flag: BatchFlag) {
        flags.remove(flag)
    }

    override fun touch(o: Any): ReferenceCounted {
        return this
    }

    override fun retain(): BedrockBatchWrapper {
        return super.retain() as BedrockBatchWrapper
    }

    override fun retain(increment: Int): BedrockBatchWrapper {
        return super.retain(increment) as BedrockBatchWrapper
    }

    companion object {
        private val RECYCLER: ObjectPool<BedrockBatchWrapper> =
            ObjectPool.newPool { handle: ObjectPool.Handle<BedrockBatchWrapper> ->
                BedrockBatchWrapper(
                    handle
                )
            }

        @JvmOverloads
        fun newInstance(compressed: ByteBuf? = null, uncompressed: ByteBuf? = null): BedrockBatchWrapper {
            val batch = RECYCLER.get()
            batch.compressed = compressed
            batch.uncompressed = uncompressed
            batch.setRefCnt(1)

            check(!(batch.packets.isNotEmpty() || batch.flags.isNotEmpty())) { "Batch was not deallocated" }
            return batch
        }

        fun create(subClientId: Int, vararg packets: DataPacket?): BedrockBatchWrapper {
            val batch = newInstance()
            for (packet in packets) {
                batch.packets.add(BedrockPacketWrapper(0, subClientId, 0, packet, null))
            }
            return batch
        }
    }
}
