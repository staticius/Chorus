package org.chorus

import cn.nukkit.block.*
import cn.nukkit.dialog.window.FormWindowDialog
import cn.nukkit.entity.*
import cn.nukkit.entity.Entity.getName
import cn.nukkit.entity.EntityHuman.getName
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.form.window.Form
import cn.nukkit.inventory.*
import cn.nukkit.level.*
import cn.nukkit.level.Level.getName
import cn.nukkit.math.*
import cn.nukkit.network.protocol.PlayerFogPacket
import cn.nukkit.network.protocol.types.PlayerBlockActionData
import cn.nukkit.scheduler.AsyncTask
import cn.nukkit.utils.DummyBossBar
import cn.nukkit.utils.LoginChainData
import com.github.benmanes.caffeine.cache.Cache
import com.google.common.collect.BiMap
import java.net.InetSocketAddress
import java.util.*

/**
 * A PlayerHandle is used to access a player's protected data.
 */
class PlayerHandle(val player: Player) {
    fun forceSendEmptyChunks() {
        player.forceSendEmptyChunks()
    }

    fun removeWindow(inventory: Inventory) {
        player.removeWindow(inventory)
    }

    fun onBlock(entity: Entity?, e: EntityDamageEvent, animate: Boolean) {
        player.onBlock(entity, e, animate)
    }

    var breakingBlockTime: Long
        get() = player.breakingBlockTime
        set(breakingBlockTime) {
            player.breakingBlockTime = breakingBlockTime
        }

    var blockBreakProgress: Double
        get() = player.blockBreakProgress
        set(blockBreakProgress) {
            player.blockBreakProgress = blockBreakProgress
        }

    val hiddenPlayers: Map<UUID?, Player?>
        get() = player.hiddenPlayers

    val chunksPerTick: Int
        get() = player.chunksPerTick

    val spawnThreshold: Int
        get() = player.spawnThreshold

    var messageCounter: Int
        get() = player.messageLimitCounter
        set(messageCounter) {
            player.messageLimitCounter = messageCounter
        }

    fun setConnected(connected: Boolean) {
        player.connected.set(connected)
    }

    fun setSocketAddress(socketAddress: InetSocketAddress?) {
        player.socketAddress = socketAddress
    }

    val isRemoveFormat: Boolean
        get() = player.removeFormat

    val username: String
        get() = player.getName()

    var displayName: String?
        get() = player.displayName
        set(displayName) {
            player.displayName = displayName!!
        }

    var sleeping: Vector3?
        get() = player.sleeping
        set(sleeping) {
            player.sleeping = sleeping
        }

    var chunkLoadCount: Int
        get() = player.chunkLoadCount
        set(chunkLoadCount) {
            player.chunkLoadCount = chunkLoadCount
        }

    var nextChunkOrderRun: Int
        get() = player.nextChunkOrderRun
        set(nextChunkOrderRun) {
            player.nextChunkOrderRun = nextChunkOrderRun
        }

    var newPosition: Vector3?
        get() = player.newPosition
        set(newPosition) {
            player.newPosition = newPosition
        }

    var chunkRadius: Int
        get() = player.chunkRadius
        set(chunkRadius) {
            player.chunkRadius = chunkRadius
        }

    var spawnPosition: Locator?
        get() = player.spawnPoint
        set(spawnLocator) {
            player.spawnPoint = spawnLocator
        }

    fun setInAirTicks(inAirTicks: Int) {
        player.inAirTicks = inAirTicks
    }

    var startAirTicks: Int
        get() = player.startAirTicks
        set(startAirTicks) {
            player.startAirTicks = startAirTicks
        }

    val isCheckMovement: Boolean
        get() = player.checkMovement

    fun setFoodData(foodData: PlayerFood?) {
        player.foodData = foodData
    }

    var formWindowCount: Int
        get() = player.formWindowCount
        set(formWindowCount) {
            player.formWindowCount = formWindowCount
        }

    val formWindows: Map<Int?, Form<*>?>
        get() = player.formWindows

    val windows: BiMap<Inventory?, Int?>
        get() = player.windows

    val windowIndex: BiMap<Int?, Inventory?>
        get() = player.windowIndex

    var closingWindowId: Int
        get() = player.closingWindowId
        set(closingWindowId) {
            player.closingWindowId = closingWindowId
        }

    fun setFormWindows(formWindows: MutableMap<Int?, Form<*>?>) {
        player.formWindows = formWindows
    }

    val serverSettings: Map<Int?, Form<*>?>
        get() = player.serverSettings

    fun setServerSettings(serverSettings: MutableMap<Int?, Form<*>?>) {
        player.serverSettings = serverSettings
    }

    var dialogWindows: Cache<String?, FormWindowDialog?>
        get() = player.dialogWindows
        set(dialogWindows) {
            player.dialogWindows = dialogWindows
        }

    fun setDummyBossBars(dummyBossBars: MutableMap<Long?, DummyBossBar?>) {
        player.dummyBossBars = dummyBossBars
    }

    var lastRightClickTime: Double
        get() = player.lastRightClickTime
        set(lastRightClickTime) {
            player.lastRightClickTime = lastRightClickTime
        }

    var lastRightClickPos: Vector3?
        get() = player.lastRightClickPos
        set(lastRightClickPos) {
            player.lastRightClickPos = lastRightClickPos
        }

    fun setLastInAirTick(lastInAirTick: Int) {
        player.lastInAirTick = lastInAirTick
    }

    var lastPlayerdLevelUpSoundTime: Int
        get() = player.lastPlayerdLevelUpSoundTime
        set(lastPlayerdLevelUpSoundTime) {
            player.lastPlayerdLevelUpSoundTime = lastPlayerdLevelUpSoundTime
        }

    fun setLastAttackEntity(lastAttackEntity: Entity?) {
        player.lastAttackEntity = lastAttackEntity
    }

    var fogStack: List<PlayerFogPacket.Fog?>?
        get() = player.fogStack
        set(fogStack) {
            player.fogStack = fogStack
        }

    fun setLastBeAttackEntity(lastBeAttackEntity: Entity?) {
        player.lastBeAttackEntity = lastBeAttackEntity
    }

    val loginChainData: LoginChainData?
        get() = player.loginChainData

    var preLoginEventTask: AsyncTask?
        get() = player.preLoginEventTask
        set(preLoginEventTask) {
            player.preLoginEventTask = preLoginEventTask
        }

    fun onPlayerLocallyInitialized() {
        player.onPlayerLocallyInitialized()
    }

    fun isValidRespawnBlock(block: Block): Boolean {
        return player.isValidRespawnBlock(block)
    }

    fun respawn() {
        player.respawn()
    }

    fun checkChunks() {
        player.checkChunks()
    }

    fun processLogin() {
        player.processLogin()
    }

    fun initEntity() {
        player.initEntity()
    }

    fun doFirstSpawn() {
        player.doFirstSpawn()
    }

    fun checkGroundState(movX: Double, movY: Double, movZ: Double, dx: Double, dy: Double, dz: Double) {
        player.checkGroundState(movX, movY, movZ, dx, dy, dz)
    }

    fun checkBlockCollision() {
        player.checkBlockCollision()
    }

    fun checkNearEntities() {
        player.checkNearEntities()
    }

    fun handleMovement(clientPos: Transform) {
        player.handleMovement(clientPos)
    }

    fun offerMovementTask(newPosition: Transform) {
        player.offerMovementTask(newPosition)
    }

    fun handleLogicInMove(invalidMotion: Boolean, distance: Double) {
        player.handleLogicInMove(invalidMotion, distance)
    }

    fun resetClientMovement() {
        player.resetClientMovement()
    }

    fun revertClientMotion(originalPos: Transform) {
        player.revertClientMotion(originalPos)
    }

    val baseOffset: Float
        get() = player.getBaseOffset()

    var lastBlockAction: PlayerBlockActionData?
        get() = player.lastBlockAction
        set(actionData) {
            player.lastBlockAction = actionData
        }

    fun onBlockBreakContinue(pos: Vector3, face: BlockFace?) {
        player.onBlockBreakContinue(pos, face)
    }

    fun onBlockBreakStart(pos: Vector3, face: BlockFace) {
        player.onBlockBreakStart(pos, face)
    }

    fun onBlockBreakAbort(pos: Vector3) {
        player.onBlockBreakAbort(pos)
    }

    fun onBlockBreakComplete(blockPos: BlockVector3, face: BlockFace?) {
        player.onBlockBreakComplete(blockPos, face)
    }

    val showingCredits: Boolean
        get() = player.showingCredits

    var inventoryOpen: Boolean
        get() = player.inventoryOpen
        set(inventoryOpen) {
            player.inventoryOpen = inventoryOpen
        }

    fun addDefaultWindows() {
        player.addDefaultWindows()
    }

    companion object {
        val noShieldDelay: Int
            get() = Player.Companion.NO_SHIELD_DELAY
    }
}