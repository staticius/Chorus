package org.chorus.level

import org.chorus.Player
import org.chorus.entity.Entity.despawnFrom
import org.chorus.level.format.IChunk
import org.chorus.math.BlockVector3
import com.google.common.collect.Sets
import it.unimi.dsi.fastutil.longs.Long2ObjectFunction
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongComparator
import it.unimi.dsi.fastutil.longs.LongOpenHashSet

import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.*


class PlayerChunkManager(private val player: Player) {
    private val chunkDistanceComparator: LongComparator = object : LongComparator {
        override fun compare(chunkHash1: Long, chunkHash2: Long): Int {
            val floor: BlockVector3 = player.getLocator().position.asBlockVector3()
            val loaderChunkX = floor.x shr 4
            val loaderChunkZ = floor.z shr 4
            val chunkDX1: Int = loaderChunkX - Level.Companion.getHashX(chunkHash1)
            val chunkDZ1: Int = loaderChunkZ - Level.Companion.getHashZ(chunkHash1)
            val chunkDX2: Int = loaderChunkX - Level.Companion.getHashX(chunkHash2)
            val chunkDZ2: Int = loaderChunkZ - Level.Companion.getHashZ(chunkHash2)
            //Compare distance to loader
            return Integer.compare(
                chunkDX1 * chunkDX1 + chunkDZ1 * chunkDZ1,
                chunkDX2 * chunkDX2 + chunkDZ2 * chunkDZ2
            )
        }
    }

    //保存着上tick已经发送的全部区块hash值
    @get:ApiStatus.Internal
    val usedChunks: LongOpenHashSet = LongOpenHashSet()

    //保存着这tick将要发送的全部区块hash值
    @get:ApiStatus.Internal
    val inRadiusChunks: LongOpenHashSet = LongOpenHashSet()
    private val trySendChunkCountPerTick: Int
    private val chunkSendQueue: LongArrayPriorityQueue
    private val chunkLoadingQueue: Long2ObjectOpenHashMap<CompletableFuture<IChunk?>?>
    private val chunkReadyToSend: Long2ObjectOpenHashMap<IChunk>
    private var lastLoaderChunkPosHashed = java.lang.Long.MAX_VALUE

    init {
        this.chunkSendQueue = LongArrayPriorityQueue(player.viewDistance * player.viewDistance, chunkDistanceComparator)
        this.chunkLoadingQueue = Long2ObjectOpenHashMap(
            player.viewDistance * player.viewDistance
        )
        this.trySendChunkCountPerTick = player.chunkSendCountPerTick
        this.chunkReadyToSend = Long2ObjectOpenHashMap()
    }

    /**
     * Handle chunk loading when the player teleported
     */
    @Synchronized
    fun handleTeleport() {
        if (!player.isConnected) return
        val floor = player.position.asBlockVector3()
        updateInRadiusChunks(1, floor)
        removeOutOfRadiusChunks()
        updateChunkSendingQueue()
        loadQueuedChunks(5, true)
        sendChunk()
    }

    @Synchronized
    fun tick() {
        if (!player.isConnected) return
        val currentLoaderChunkPosHashed: Long
        val floor = player.position.asBlockVector3()
        if ((Level.Companion.chunkHash(floor.x shr 4, floor.z shr 4)
                .also { currentLoaderChunkPosHashed = it }) != lastLoaderChunkPosHashed
        ) {
            lastLoaderChunkPosHashed = currentLoaderChunkPosHashed
            updateInRadiusChunks(player.viewDistance, floor)
            removeOutOfRadiusChunks()
            updateChunkSendingQueue()
        }
        loadQueuedChunks(trySendChunkCountPerTick, false)
        sendChunk()
    }

    @ApiStatus.Internal
    fun addSendChunk(x: Int, z: Int) {
        chunkSendQueue.enqueue(Level.Companion.chunkHash(x, z))
    }

    private fun updateChunkSendingQueue() {
        chunkSendQueue.clear()
        //已经发送的区块不再二次发送
        val difference = Sets.difference(
            inRadiusChunks,
            usedChunks
        )
        for (v in difference) {
            chunkSendQueue.enqueue(v)
        }
    }

    private fun updateInRadiusChunks(viewDistance: Int, currentPos: BlockVector3) {
        inRadiusChunks.clear()
        val loaderChunkX = currentPos.x shr 4
        val loaderChunkZ = currentPos.z shr 4
        for (rx in -viewDistance..viewDistance) {
            for (rz in -viewDistance..viewDistance) {
                if (ifChunkNotInRadius(rx, rz, viewDistance)) continue
                val chunkX = loaderChunkX + rx
                val chunkZ = loaderChunkZ + rz
                val hashXZ: Long = Level.Companion.chunkHash(chunkX, chunkZ)
                inRadiusChunks.add(hashXZ)
            }
        }
    }

    private fun removeOutOfRadiusChunks() {
        val difference: Set<Long> = HashSet(
            Sets.difference(
                usedChunks, inRadiusChunks
            )
        )
        // Unload blocks that are out of range
        for (hash in difference) {
            val x: Int = Level.Companion.getHashX(hash)
            val z: Int = Level.Companion.getHashZ(hash)
            if (player.level!!.unregisterChunkLoader(player, x, z)) {
                for (entity in player.level!!.getChunkEntities(x, z)!!.values()) {
                    if (entity !== player) {
                        entity.despawnFrom(player)
                    }
                }
            }
        }
        // The intersection of the remaining sentChunks and inRadiusChunks
        usedChunks.removeAll(difference)
    }

    private fun loadQueuedChunks(trySendChunkCountPerTick: Int, force: Boolean) {
        if (chunkSendQueue.isEmpty()) return
        var triedSendChunkCount = 0
        do {
            triedSendChunkCount++
            val chunkHash: Long = chunkSendQueue.dequeueLong()
            val chunkX: Int = Level.Companion.getHashX(chunkHash)
            val chunkZ: Int = Level.Companion.getHashZ(chunkHash)
            val chunkTask = chunkLoadingQueue.computeIfAbsent(
                chunkHash,
                Long2ObjectFunction { hash: Long -> player.level!!.getChunkAsync(chunkX, chunkZ) })
            if (chunkTask!!.isDone) {
                try {
                    val chunk = chunkTask[10, TimeUnit.MICROSECONDS]
                    if (chunk == null || !chunk.chunkState.canSend()) {
                        player.level!!.generateChunk(chunkX, chunkZ, force)
                        chunkSendQueue.enqueue(chunkHash)
                        continue
                    }
                    chunkLoadingQueue.remove(chunkHash)
                    player.level!!.registerChunkLoader(player, chunkX, chunkZ, false)
                    chunkReadyToSend.put(chunkHash, chunk)
                } catch (ignore: InterruptedException) {
                } catch (ignore: ExecutionException) {
                } catch (e: TimeoutException) {
                    PlayerChunkManager.log.warn("read chunk timeout {} {}", chunkX, chunkZ)
                }
            } else {
                chunkSendQueue.enqueue(chunkHash)
            }
        } while (!chunkSendQueue.isEmpty() && triedSendChunkCount < trySendChunkCountPerTick)
    }

    private fun sendChunk() {
        if (!chunkReadyToSend.isEmpty()) {
            val ncp: NetworkChunkPublisherUpdatePacket = NetworkChunkPublisherUpdatePacket()
            ncp.position = player.position.asBlockVector3()
            ncp.radius = player.viewDistance shl 4
            player.dataPacket(ncp)
            for (e in chunkReadyToSend.long2ObjectEntrySet()) {
                val chunkX: Int = Level.Companion.getHashX(e.longKey)
                val chunkZ: Int = Level.Companion.getHashZ(e.longKey)
                val ev: PlayerChunkRequestEvent = PlayerChunkRequestEvent(player, chunkX, chunkZ)
                Server.instance.pluginManager.callEvent(ev)
                player.level!!.requestChunk(chunkX, chunkZ, player)
            }
            usedChunks.addAll(chunkReadyToSend.keySet())
        }
        chunkReadyToSend.clear()
    }

    private fun ifChunkNotInRadius(chunkX: Int, chunkZ: Int, radius: Int): Boolean {
        return chunkX * chunkX + chunkZ * chunkZ > radius * radius
    }
}
