package cn.nukkit.blockentity

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.*
import cn.nukkit.command.CommandSender
import cn.nukkit.command.ConsoleCommandSender
import cn.nukkit.event.command.CommandBlockExecuteEvent
import cn.nukkit.inventory.*
import cn.nukkit.lang.CommandOutputContainer
import cn.nukkit.lang.TextContainer
import cn.nukkit.lang.TranslationContainer
import cn.nukkit.level.GameRule
import cn.nukkit.level.Level
import cn.nukkit.level.format.IChunk
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.nbt.tag.StringTag
import cn.nukkit.permission.*
import cn.nukkit.plugin.Plugin
import cn.nukkit.utils.*
import com.google.common.base.Strings
import lombok.Getter
import java.util.function.Consumer

@Getter
class BlockEntityCommandBlock(chunk: IChunk, nbt: CompoundTag) : BlockEntitySpawnable(chunk, nbt), ICommandBlock,
    BlockEntityInventoryHolder {
    protected var conditionalMode: Boolean = false
    override var isAuto: Boolean = false
    override var command: String? = null
    override var lastExecution: Long = 0
    override var isTrackingOutput: Boolean = false
        protected set
    override var lastOutput: String? = null
    protected var lastOutputParams: ListTag<StringTag>? = null
    override var lastOutputCommandMode: Int = 0
    override var isLastOutputCondionalMode: Boolean = false
    override var isLastOutputRedstoneMode: Boolean = false
    override var successCount: Int = 0
    override var isConditionMet: Boolean = false
        protected set
    override var isPowered: Boolean = false
    override var tickDelay: Int = 0
    override var isExecutingOnFirstTick: Boolean = false //TODO: ???
    protected var perm: PermissibleBase? = null
    protected var currentTick: Int = 0
    protected var inventory: CommandBlockInventory = CommandBlockInventory(this)

    override fun initBlockEntity() {
        super.initBlockEntity()
        if (this.mode == ICommandBlock.Companion.MODE_REPEATING) {
            this.scheduleUpdate()
        }
    }

    override fun loadNBT() {
        super.loadNBT()
        this.perm = PermissibleBase(this)
        this.currentTick = 0

        if (namedTag.contains(ICommandBlock.Companion.TAG_POWERED)) {
            this.isPowered = namedTag.getBoolean(ICommandBlock.Companion.TAG_POWERED)
        } else {
            this.isPowered = false
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_CONDITIONAL_MODE)) {
            this.conditionalMode = namedTag.getBoolean(ICommandBlock.Companion.TAG_CONDITIONAL_MODE)
        } else {
            this.conditionalMode = false
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_AUTO)) {
            this.isAuto = namedTag.getBoolean(ICommandBlock.Companion.TAG_AUTO)
        } else {
            this.isAuto = false
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_COMMAND)) {
            setCommand(namedTag.getString(ICommandBlock.Companion.TAG_COMMAND))
        } else {
            setCommand("")
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_LAST_EXECUTION)) {
            this.lastExecution = namedTag.getLong(ICommandBlock.Companion.TAG_LAST_EXECUTION)
        } else {
            this.lastExecution = 0
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_TRACK_OUTPUT)) {
            this.isTrackingOutput = namedTag.getBoolean(ICommandBlock.Companion.TAG_TRACK_OUTPUT)
        } else {
            this.isTrackingOutput = true
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_LAST_OUTPUT)) {
            this.lastOutput = namedTag.getString(ICommandBlock.Companion.TAG_LAST_OUTPUT)
        } else {
            this.lastOutput = null
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_LAST_OUTPUT_PARAMS)) {
            this.lastOutputParams = namedTag.getList<StringTag>(
                ICommandBlock.Companion.TAG_LAST_OUTPUT_PARAMS,
                StringTag::class.java
            )
        } else {
            this.lastOutputParams = ListTag()
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_LP_COMMAND_MODE)) {
            this.lastOutputCommandMode = namedTag.getInt(ICommandBlock.Companion.TAG_LP_COMMAND_MODE)
        } else {
            this.lastOutputCommandMode = 0
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_LP_CONDIONAL_MODE)) {
            this.isLastOutputCondionalMode = namedTag.getBoolean(ICommandBlock.Companion.TAG_LP_CONDIONAL_MODE)
        } else {
            this.isLastOutputCondionalMode = true
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_LP_REDSTONE_MODE)) {
            this.isLastOutputRedstoneMode = namedTag.getBoolean(ICommandBlock.Companion.TAG_LP_REDSTONE_MODE)
        } else {
            this.isLastOutputRedstoneMode = true
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_SUCCESS_COUNT)) {
            this.successCount = namedTag.getInt(ICommandBlock.Companion.TAG_SUCCESS_COUNT)
        } else {
            this.successCount = 0
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_CONDITION_MET)) {
            this.isConditionMet = namedTag.getBoolean(ICommandBlock.Companion.TAG_CONDITION_MET)
        } else {
            this.isConditionMet = false
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_TICK_DELAY)) {
            this.tickDelay = namedTag.getInt(ICommandBlock.Companion.TAG_TICK_DELAY)
        } else {
            this.tickDelay = 0
        }

        if (namedTag.contains(ICommandBlock.Companion.TAG_EXECUTE_ON_FIRST_TICK)) {
            this.isExecutingOnFirstTick = namedTag.getBoolean(ICommandBlock.Companion.TAG_EXECUTE_ON_FIRST_TICK)
        } else {
            this.isExecutingOnFirstTick = false
        }
    }

    override fun saveNBT() {
        super.saveNBT()
        namedTag.putBoolean(ICommandBlock.Companion.TAG_POWERED, this.isPowered)
        namedTag.putBoolean(ICommandBlock.Companion.TAG_CONDITIONAL_MODE, this.conditionalMode)
        namedTag.putBoolean(ICommandBlock.Companion.TAG_AUTO, this.isAuto)
        if (this.command != null && !command!!.isEmpty()) {
            namedTag.putString(ICommandBlock.Companion.TAG_COMMAND, command!!)
        }
        namedTag.putLong(ICommandBlock.Companion.TAG_LAST_EXECUTION, this.lastExecution)
        namedTag.putBoolean(ICommandBlock.Companion.TAG_TRACK_OUTPUT, this.isTrackingOutput)
        if (this.lastOutput != null && !lastOutput!!.isEmpty()) {
            namedTag.putString(
                ICommandBlock.Companion.TAG_LAST_OUTPUT,
                lastOutput!!
            )
        }
        if (this.lastOutputParams != null) {
            namedTag.putList(ICommandBlock.Companion.TAG_LAST_OUTPUT_PARAMS, this.lastOutputParams)
        }
        namedTag.putInt(ICommandBlock.Companion.TAG_LP_COMMAND_MODE, this.lastOutputCommandMode)
        namedTag.putBoolean(ICommandBlock.Companion.TAG_LP_CONDIONAL_MODE, this.isLastOutputCondionalMode)
        namedTag.putBoolean(ICommandBlock.Companion.TAG_LP_REDSTONE_MODE, this.isLastOutputRedstoneMode)
        namedTag.putInt(ICommandBlock.Companion.TAG_SUCCESS_COUNT, this.successCount)
        namedTag.putBoolean(ICommandBlock.Companion.TAG_CONDITION_MET, this.isConditionMet)
        namedTag.putInt(ICommandBlock.Companion.TAG_VERSION, ICommandBlock.Companion.CURRENT_VERSION)
        namedTag.putInt(ICommandBlock.Companion.TAG_TICK_DELAY, this.tickDelay)
        namedTag.putBoolean(ICommandBlock.Companion.TAG_EXECUTE_ON_FIRST_TICK, this.isExecutingOnFirstTick)
    }

    override val spawnCompound: CompoundTag
        get() {
            val nbt = super.getSpawnCompound()
                .putBoolean(ICommandBlock.Companion.TAG_CONDITIONAL_MODE, this.conditionalMode)
                .putBoolean(ICommandBlock.Companion.TAG_AUTO, this.isAuto)
                .putLong(ICommandBlock.Companion.TAG_LAST_EXECUTION, this.lastExecution)
                .putBoolean(ICommandBlock.Companion.TAG_TRACK_OUTPUT, this.isTrackingOutput)
                .putInt(ICommandBlock.Companion.TAG_LP_COMMAND_MODE, this.lastOutputCommandMode)
                .putBoolean(ICommandBlock.Companion.TAG_LP_CONDIONAL_MODE, this.isLastOutputCondionalMode)
                .putBoolean(ICommandBlock.Companion.TAG_LP_REDSTONE_MODE, this.isLastOutputRedstoneMode)
                .putInt(ICommandBlock.Companion.TAG_SUCCESS_COUNT, this.successCount)
                .putBoolean(ICommandBlock.Companion.TAG_CONDITION_MET, this.isConditionMet)
                .putInt(ICommandBlock.Companion.TAG_VERSION, ICommandBlock.Companion.CURRENT_VERSION)
                .putInt(ICommandBlock.Companion.TAG_TICK_DELAY, this.tickDelay)
                .putBoolean(
                    ICommandBlock.Companion.TAG_EXECUTE_ON_FIRST_TICK,
                    isExecutingOnFirstTick
                )
            if (this.command != null) {
                nbt.putString(ICommandBlock.Companion.TAG_COMMAND, command!!)
            }
            if (this.lastOutput != null) {
                nbt.putString(ICommandBlock.Companion.TAG_LAST_OUTPUT, lastOutput!!)
            }
            if (this.lastOutputParams != null) {
                nbt.putList(ICommandBlock.Companion.TAG_LAST_OUTPUT_PARAMS, this.lastOutputParams)
            }
            if (this.hasName()) {
                nbt.putString(ICommandBlock.Companion.TAG_CUSTOM_NAME, this.getName())
            }
            return nbt
        }

    override val isBlockEntityValid: Boolean
        get() {
            val blockId = this.levelBlock.id
            return blockId == BlockID.COMMAND_BLOCK || blockId == BlockID.CHAIN_COMMAND_BLOCK || blockId == BlockID.REPEATING_COMMAND_BLOCK
        }

    override fun getName(): String {
        return if (this.hasName()) namedTag.getString(ICommandBlock.Companion.TAG_CUSTOM_NAME) else "!"
    }

    override fun hasName(): Boolean {
        return namedTag.contains(ICommandBlock.Companion.TAG_CUSTOM_NAME)
    }

    override fun setName(name: String?) {
        if (Strings.isNullOrEmpty(name)) {
            namedTag.remove(ICommandBlock.Companion.TAG_CUSTOM_NAME)
        } else {
            namedTag.putString(ICommandBlock.Companion.TAG_CUSTOM_NAME, name!!)
        }
    }

    override fun onUpdate(): Boolean {
        if (this.mode != ICommandBlock.Companion.MODE_REPEATING) {
            return false
        }

        if (currentTick++ < this.tickDelay) {
            return true
        }

        this.execute()
        this.currentTick = 0

        return true
    }

    override fun execute(chain: Int): Boolean {
        var chain = chain
        if (!(getServer().settings.gameplaySettings()
                .enableCommandBlocks() && level.gameRules.getBoolean(GameRule.COMMAND_BLOCKS_ENABLED))
        ) {
            return false
        }
        if (this.levelBlock.getSide((this.levelBlock as Faceable).blockFace.opposite) is BlockCommandBlock) {
            if (this.isConditional && lastCB.getBlockEntity()
                    .getSuccessCount() == 0
            ) { //jump over because this CB is conditional and the last CB didn't succeed
                val next = this.levelBlock.getSide((this.levelBlock as Faceable).blockFace)
                if (next is BlockChainCommandBlock) {
                    next.blockEntity!!.trigger(++chain)
                }
                return true
            }
        }
        if (this.lastExecution != level.tick.toLong()) {
            this.setConditionMet()
            if (this.isConditionMet && (this.isAuto || this.isPowered)) {
                val cmd = this.getCommand()
                if (!Strings.isNullOrEmpty(cmd)) {
                    if (cmd.equals("Searge", ignoreCase = true)) {
                        this.lastOutput = "#itzlipofutzli"
                        this.successCount = 1
                    } else if (cmd.equals("Hello PNX!", ignoreCase = true)) {
                        this.lastOutput = "superice666\nlt_name\ndaoge_cmd\nCool_Loong\nzimzaza4"
                        this.successCount = 1
                    } else {
                        this.lastOutput = null
                        val event = CommandBlockExecuteEvent(this.levelBlock, cmd)
                        Server.getInstance().pluginManager.callEvent(event)
                        if (event.isCancelled) {
                            return false
                        }
                        this.successCount = Server.getInstance().executeCommand(this, cmd)
                    }
                }

                val block = this.levelBlock.getSide((this.levelBlock as Faceable).blockFace)
                if (block is BlockChainCommandBlock) {
                    block.blockEntity!!.trigger(++chain)
                }
            }

            this.lastExecution = level.tick.toLong()
            this.lastOutputCommandMode = this.mode
            this.isLastOutputCondionalMode = this.isConditional
            this.isLastOutputRedstoneMode = !this.isAuto
        } else {
            this.successCount = 0
        }

        this.levelBlockAround.forEach(Consumer { block: Block -> block.onUpdate(Level.BLOCK_UPDATE_REDSTONE) }) //update redstone
        return true
    }

    override val mode: Int
        get() {
            val block = this.levelBlock
            if (block.id === BlockID.REPEATING_COMMAND_BLOCK) {
                return ICommandBlock.Companion.MODE_REPEATING
            } else if (block.id === BlockID.CHAIN_COMMAND_BLOCK) {
                return ICommandBlock.Companion.MODE_CHAIN
            }
            return ICommandBlock.Companion.MODE_NORMAL
        }

    override fun getCommand(): String? {
        return this.command
    }

    override fun setCommand(command: String?) {
        this.command = command
        this.successCount = 0
    }

    override var isConditional: Boolean
        get() = this.conditionalMode
        set(conditionalMode) {
            this.conditionalMode = conditionalMode
            this.setConditionMet()
        }

    override fun setConditionMet(): Boolean {
        if (this.isConditional && this.levelBlock is BlockCommandBlock) {
            if (block.getSide(block.getBlockFace().getOpposite()) is BlockCommandBlock) {
                this.isConditionMet = (next.getBlockEntity() as BlockEntityCommandBlock).successCount > 0
            } else {
                this.isConditionMet = false
            }
        } else {
            this.isConditionMet = true
        }
        return this.isConditionMet
    }

    override fun setTrackOutput(track: Boolean) {
        this.isTrackingOutput = track
    }

    override fun getLastOutput(): String? {
        return this.lastOutput
    }

    override fun setLastOutput(output: String?) {
        if (Strings.isNullOrEmpty(output)) {
            this.lastOutput = null
        } else {
            this.lastOutput = output
        }
    }

    override fun setLastOutputParams(params: ListTag<StringTag>?) {
        this.lastOutputParams = params
    }

    override fun isPermissionSet(name: String): Boolean {
        return perm!!.isPermissionSet(name)
    }

    override fun isPermissionSet(permission: Permission): Boolean {
        return perm!!.isPermissionSet(permission)
    }

    override fun hasPermission(name: String): Boolean {
        return perm!!.hasPermission(name)
    }

    override fun hasPermission(permission: Permission): Boolean {
        return perm!!.hasPermission(permission)
    }

    override fun addAttachment(plugin: Plugin): PermissionAttachment {
        return perm!!.addAttachment(plugin)
    }

    override fun addAttachment(plugin: Plugin, name: String): PermissionAttachment {
        return perm!!.addAttachment(plugin, name)
    }

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean): PermissionAttachment {
        return perm!!.addAttachment(plugin, name, value)
    }

    override fun removeAttachment(attachment: PermissionAttachment) {
        perm!!.removeAttachment(attachment)
    }

    override fun recalculatePermissions() {
        perm!!.recalculatePermissions()
    }

    override fun getEffectivePermissions(): Map<String, PermissionAttachmentInfo> {
        return perm!!.effectivePermissions
    }

    override fun isPlayer(): Boolean {
        return false
    }

    override fun isEntity(): Boolean {
        return false
    }

    override fun getServer(): Server {
        return Server.getInstance()
    }

    override fun sendMessage(message: String) {
        this.sendMessage(TranslationContainer(message))
    }

    override fun sendMessage(message: TextContainer) {
        if (this.isTrackingOutput) {
            this.lastOutput = message.text
            if (message is TranslationContainer) {
                val newParams = ListTag<StringTag>()
                for (param in message.parameters) {
                    newParams.add(StringTag(param))
                }
                this.lastOutputParams = newParams
            }
        }
        if (level.gameRules.getBoolean(GameRule.COMMAND_BLOCK_OUTPUT)) {
            message.text =
                TextFormat.GRAY.toString() + "" + TextFormat.ITALIC + "[" + this.getName() + ": " + TextFormat.RESET +
                        (if (message.text != getServer().language[message.text]) "%" else "") + message.text + "]"
            val users = getServer().pluginManager.getPermissionSubscriptions(Server.BROADCAST_CHANNEL_ADMINISTRATIVE)
            for (user in users) {
                if (user is Player || user is ConsoleCommandSender) {
                    (user as CommandSender).sendMessage(message)
                }
            }
        }
    }

    override fun sendCommandOutput(container: CommandOutputContainer) {
        for (message in container.messages) {
            this.sendMessage(TranslationContainer(message.messageId, *message.parameters))
        }
    }

    override fun isOp(): Boolean {
        return true
    }

    override fun setOp(value: Boolean) {
    }

    override fun getInventory(): Inventory {
        return inventory
    }

    override fun close() {
        if (!closed) {
            for (player in HashSet(getInventory().viewers)) {
                player.removeWindow(this.getInventory())
            }
            super.close()
        }
    }
}
