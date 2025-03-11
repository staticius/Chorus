package org.chorus.entity.mob

import org.chorus.Player
import org.chorus.command.NPCCommandSender
import org.chorus.dialog.element.ElementDialogButton
import org.chorus.dialog.element.ElementDialogButton.CmdLine
import org.chorus.dialog.element.ElementDialogButton.getData
import org.chorus.dialog.element.ElementDialogButton.getMode
import org.chorus.dialog.handler.FormDialogHandler
import org.chorus.dialog.response.FormResponseDialog
import org.chorus.dialog.window.FormWindowDialog
import org.chorus.dialog.window.FormWindowDialog.buttonJSONData
import org.chorus.dialog.window.FormWindowDialog.getButtons
import org.chorus.entity.*
import org.chorus.entity.ai.behavior.Behavior
import org.chorus.entity.ai.behavior.IBehavior
import org.chorus.entity.ai.behaviorgroup.BehaviorGroup
import org.chorus.entity.ai.behaviorgroup.IBehaviorGroup
import org.chorus.entity.ai.controller.*
import org.chorus.entity.ai.evaluator.ProbabilityEvaluator
import org.chorus.entity.ai.executor.LookAtTargetExecutor
import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.sensor.ISensor
import org.chorus.entity.ai.sensor.NearestPlayerSensor
import org.chorus.entity.data.EntityDataTypes
import org.chorus.event.entity.EntityDamageByEntityEvent
import org.chorus.event.entity.EntityDamageEvent
import org.chorus.item.*
import org.chorus.level.format.IChunk
import org.chorus.math.*
import org.chorus.nbt.tag.CompoundTag
import org.chorus.nbt.tag.StringTag
import org.chorus.network.protocol.NPCRequestPacket
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
                    org.chorus.Server.instance.executeCommand(NPCCommandSender(this, player), line.cmd_line)
                }
            }
            if (response.getRequestType() === NPCRequestPacket.RequestType.EXECUTE_OPENING_COMMANDS) {
                for (button: ElementDialogButton? in this.dialog.getButtons()) {
                    if (button.getMode() == ElementDialogButton.Mode.ON_ENTER) {
                        for (line: CmdLine? in button.getData()) {
                            org.chorus.Server.instance.executeCommand(NPCCommandSender(this, player), line.cmd_line)
                        }
                    }
                }
            }
            if (response.getRequestType() === NPCRequestPacket.RequestType.EXECUTE_CLOSING_COMMANDS) {
                for (button: ElementDialogButton? in this.dialog.getButtons()) {
                    if (button.getMode() == ElementDialogButton.Mode.ON_EXIT) {
                        for (line: CmdLine? in button.getData()) {
                            org.chorus.Server.instance.executeCommand(NPCCommandSender(this, player), line.cmd_line)
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
