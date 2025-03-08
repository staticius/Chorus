package cn.nukkit.entity.mob

import cn.nukkit.Player
import cn.nukkit.command.NPCCommandSender
import cn.nukkit.dialog.element.ElementDialogButton
import cn.nukkit.dialog.element.ElementDialogButton.CmdLine
import cn.nukkit.dialog.element.ElementDialogButton.getData
import cn.nukkit.dialog.element.ElementDialogButton.getMode
import cn.nukkit.dialog.handler.FormDialogHandler
import cn.nukkit.dialog.response.FormResponseDialog
import cn.nukkit.dialog.window.FormWindowDialog
import cn.nukkit.dialog.window.FormWindowDialog.buttonJSONData
import cn.nukkit.dialog.window.FormWindowDialog.getButtons
import cn.nukkit.entity.*
import cn.nukkit.entity.ai.behavior.Behavior
import cn.nukkit.entity.ai.behavior.IBehavior
import cn.nukkit.entity.ai.behaviorgroup.BehaviorGroup
import cn.nukkit.entity.ai.behaviorgroup.IBehaviorGroup
import cn.nukkit.entity.ai.controller.*
import cn.nukkit.entity.ai.evaluator.ProbabilityEvaluator
import cn.nukkit.entity.ai.executor.LookAtTargetExecutor
import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.sensor.ISensor
import cn.nukkit.entity.ai.sensor.NearestPlayerSensor
import cn.nukkit.entity.data.EntityDataTypes
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.item.*
import cn.nukkit.level.format.IChunk
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.StringTag
import cn.nukkit.network.protocol.NPCRequestPacket
import java.util.Set

/**
 * @author good777LUCKY
 */
class EntityNPC(chunk: IChunk?, nbt: CompoundTag) : EntityMob(chunk, nbt), EntityInteractable {
    override fun getIdentifier(): String {
        return EntityID.Companion.NPC
    }

    protected var dialog: FormWindowDialog? = null

    init {
        nbt.putIfNull(TAG_RAWTEXT_NAME, StringTag("NPC"))
    }


    override fun getWidth(): Float {
        return 0.6f
    }

    override fun getHeight(): Float {
        return 2.1f
    }

    override fun canDoInteraction(): Boolean {
        return true
    }

    override fun getInteractButtonText(player: Player): String {
        return if (player.isCreative()) "action.interact.edit" else "action.interact.talk"
    }

    override fun getOriginalName(): String {
        return "NPC"
    }

    override fun canCollide(): Boolean {
        return false
    }

    override fun requireBehaviorGroup(): IBehaviorGroup {
        return BehaviorGroup(
            this.tickSpread,
            setOf<IBehavior>(),
            Set.of<IBehavior>(
                Behavior(
                    LookAtTargetExecutor(CoreMemoryTypes.Companion.NEAREST_PLAYER, 100),
                    ProbabilityEvaluator(2, 100)
                )
            ),
            Set.of<ISensor>(NearestPlayerSensor(6.0, 0.0, 20)),
            Set.of<IController>(LookController(true, false)),
            null,
            this
        )
    }

    override fun getExperienceDrops(): Int {
        return 0
    }

    public override fun initEntity() {
        super.initEntity()
        this.setMaxHealth(Int.MAX_VALUE) // Should be Float max value
        this.setHealth(20f)
        this.setNameTagVisible(true)
        this.setNameTagAlwaysVisible(true)
        this.setMovementSpeed(0.5f)

        this.dialog = FormWindowDialog(
            if (namedTag!!.contains(TAG_RAWTEXT_NAME)) namedTag!!.getString(TAG_RAWTEXT_NAME) else this.getNameTag(),
            if (namedTag!!.contains(TAG_INTERACTIVE_TEXT)) namedTag!!.getString(TAG_INTERACTIVE_TEXT) else "",
            this
        )

        if (!namedTag!!.getString(TAG_ACTIONS).isEmpty()) dialog!!.buttonJSONData =
            namedTag!!.getString(TAG_ACTIONS)

        dialog!!.addHandler(FormDialogHandler { player: Player?, response: FormResponseDialog? ->
            if (response.getRequestType() === NPCRequestPacket.RequestType.SET_ACTIONS) {
                if (!response.getData().isEmpty()) {
                    this.dialog.buttonJSONData = response.getData()
                    this.setDataProperty(EntityDataTypes.Companion.ACTIONS, response.getData())
                }
            }
            if (response.getRequestType() === NPCRequestPacket.RequestType.SET_INTERACTION_TEXT) {
                this.dialog.content = response.getData()
                this.setDataProperty(EntityDataTypes.Companion.INTERACT_TEXT, response.getData())
            }
            if (response.getRequestType() === NPCRequestPacket.RequestType.SET_NAME) {
                this.dialog.title = response.getData()
                this.setNameTag(response.getData())
            }
            if (response.getRequestType() === NPCRequestPacket.RequestType.SET_SKIN) {
                this.variant = response.getSkinType()
            }
            if (response.getRequestType() === NPCRequestPacket.RequestType.EXECUTE_ACTION) {
                val clickedButton: ElementDialogButton? = response.getClickedButton()
                for (line: CmdLine? in clickedButton.getData()) {
                    cn.nukkit.Server.getInstance().executeCommand(NPCCommandSender(this, player), line.cmd_line)
                }
            }
            if (response.getRequestType() === NPCRequestPacket.RequestType.EXECUTE_OPENING_COMMANDS) {
                for (button: ElementDialogButton? in this.dialog.getButtons()) {
                    if (button.getMode() == ElementDialogButton.Mode.ON_ENTER) {
                        for (line: CmdLine? in button.getData()) {
                            cn.nukkit.Server.getInstance().executeCommand(NPCCommandSender(this, player), line.cmd_line)
                        }
                    }
                }
            }
            if (response.getRequestType() === NPCRequestPacket.RequestType.EXECUTE_CLOSING_COMMANDS) {
                for (button: ElementDialogButton? in this.dialog.getButtons()) {
                    if (button.getMode() == ElementDialogButton.Mode.ON_EXIT) {
                        for (line: CmdLine? in button.getData()) {
                            cn.nukkit.Server.getInstance().executeCommand(NPCCommandSender(this, player), line.cmd_line)
                        }
                    }
                }
            }
        })
        dialog!!.bindEntity = this
    }

    override fun saveNBT() {
        super.saveNBT()

        namedTag!!.putString(
            TAG_RAWTEXT_NAME,
            dialog!!.title!!
        )
        namedTag!!.putString(
            TAG_INTERACTIVE_TEXT,
            dialog!!.content!!
        )
        namedTag!!.putString(
            TAG_ACTIONS,
            dialog!!.buttonJSONData!!
        )
    }

    override fun onInteract(player: Player, item: Item, clickedPos: Vector3): Boolean {
        // For creative mode players, the NPC's dialog sent must have an empty sceneName; otherwise, the client will not allow the dialog box content to be modified.
        // Additionally, we do not need to record the dialog box sent to creative mode players. Firstly, because we cannot clear it, and secondly, there is no need to do so.
        player.showDialogWindow(this.dialog, !player.isCreative())
        return false
    }

    override fun kill() {
        this.health = 0f
        this.scheduleUpdate()

        for (passenger: Entity? in ArrayList(this.passengers)) {
            dismountEntity(passenger!!)
        }
    }

    override fun attack(source: EntityDamageEvent): Boolean {
        if (source is EntityDamageByEntityEvent && source.damager is Player && damager.isCreative()) {
            this.kill()
        }
        return false
    }

    fun getDialog(): FormWindowDialog? {
        return dialog
    }

    companion object {
        const val TAG_ACTIONS: String = "Actions"
        const val TAG_INTERACTIVE_TEXT: String = "InterativeText"
        const val TAG_PLAYER_SCENE_MAPPING: String = "PlayerSceneMapping"
        const val TAG_RAWTEXT_NAME: String = "RawtextName"
    }
}
