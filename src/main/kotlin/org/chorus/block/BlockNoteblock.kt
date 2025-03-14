package org.chorus.block

import org.chorus.Player
import org.chorus.block.BlockEntityHolder.blockEntity
import org.chorus.blockentity.BlockEntity
import org.chorus.blockentity.BlockEntityMusic.changePitch
import org.chorus.blockentity.BlockEntityMusic.isPowered
import org.chorus.blockentity.BlockEntityMusic.pitch
import org.chorus.event.player.PlayerInteractEvent
import org.chorus.item.Item
import org.chorus.item.ItemTool
import org.chorus.level.Level
import org.chorus.level.vibration.VibrationEvent
import org.chorus.math.BlockFace
import org.chorus.math.Vector3
import org.chorus.nbt.tag.CompoundTag.getByte

class BlockNoteblock @JvmOverloads constructor(blockstate: BlockState? = Companion.properties.defaultState) :
    BlockSolid(blockstate), RedstoneComponent, BlockEntityHolder<BlockEntityMusic?> {
    override val name: String
        get() = "Note Block"

    override val toolType: Int
        get() = ItemTool.TYPE_AXE

    override val blockEntityClass: Class<out Any>
        get() = BlockEntityMusic::class.java

    override val blockEntityType: String
        get() = BlockEntity.MUSIC

    override val hardness: Double
        get() = 0.8

    override val resistance: Double
        get() = 0.8

    override fun canBeActivated(): Boolean {
        return true
    }

    override fun onActivate(
        item: Item,
        player: Player?,
        blockFace: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float
    ): Boolean {
        if (player != null && player.isSneaking() || (up()!!.isAir && blockFace == BlockFace.UP && item.isBlock() && item.getBlock() is BlockHead)) {
            return false
        }
        this.increaseStrength()
        this.emitSound(player)
        return true
    }

    override fun place(
        item: Item,
        block: Block,
        target: Block,
        face: BlockFace,
        fx: Double,
        fy: Double,
        fz: Double,
        player: Player?
    ): Boolean {
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null
    }

    override fun onTouch(
        vector: Vector3,
        item: Item,
        face: BlockFace,
        fx: Float,
        fy: Float,
        fz: Float,
        player: Player?,
        action: PlayerInteractEvent.Action
    ) {
        onUpdate(Level.BLOCK_UPDATE_TOUCH)
        if (player != null && action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && player.isSurvival) {
            this.emitSound(player)
        }
    }

    val strength: Int
        get() {
            val blockEntity: BlockEntityMusic? = this.blockEntity
            return if (blockEntity != null) blockEntity.pitch else 0
        }

    fun increaseStrength() {
        getOrCreateBlockEntity().changePitch()
    }

    val instrument: Instrument
        get() = when (this.down()) {
            -> Instrument.GUITAR
            -> Instrument.SNARE_DRUM
            -> Instrument.SNARE_DRUM
            -> Instrument.SNARE_DRUM
            -> Instrument.SNARE_DRUM
            -> Instrument.SNARE_DRUM
            -> Instrument.SNARE_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BASS_DRUM
            -> Instrument.BELLS
            -> Instrument.FLUTE
            -> Instrument.FLUTE
            -> Instrument.FLUTE
            -> Instrument.FLUTE
            -> Instrument.FLUTE
            -> Instrument.FLUTE
            -> Instrument.FLUTE
            -> Instrument.FLUTE
            -> Instrument.CHIMES
            -> Instrument.XYLOPHONE
            -> Instrument.IRON_XYLOPHONE
            -> Instrument.COW_BELL
            -> Instrument.DIDGERIDOO
            -> Instrument.BIT
            -> Instrument.BANJO
            -> Instrument.PLING
            else -> {
                if (up() is BlockHead) {
                    var meta = 0
                    if (skull.blockEntity != null) {
                        meta = skull.blockEntity.namedTag.getByte("SkullType").toInt()
                    }
                    when (meta) {
                        0, 3 -> Instrument.SKELETON
                        1 -> Instrument.WITHER_SKELETON
                        2 -> Instrument.ZOMBIE
                        4 -> Instrument.CREEPER
                        5 -> Instrument.ENDER_DRAGON
                        else -> Instrument.PIGLIN
                    }
                } else {
                    Instrument.HARP
                }
            }
        }

    @JvmOverloads
    fun emitSound(player: Player? = null) {
        level.vibrationManager.callVibrationEvent(
            VibrationEvent(
                player
                    ?: this, add(0.5, 0.5, 0.5).position, VibrationType.BLOCK_CHANGE
            )
        )

        val instrument = this.instrument

        level.addLevelSoundEvent(
            this.position,
            LevelSoundEventPacket.SOUND_NOTE,
            instrument.ordinal shl 8 or this.strength
        )

        val pk: BlockEventPacket = BlockEventPacket()
        pk.x = position.floorX
        pk.y = position.floorY
        pk.z = position.floorZ
        pk.type = instrument.ordinal
        pk.value = this.strength
        level.addChunkPacket(position.floorX shr 4, position.floorZ shr 4, pk)
    }

    override fun onUpdate(type: Int): Int {
        if (type == Level.BLOCK_UPDATE_REDSTONE) {
            // We can't use getOrCreateBlockEntity(), because the update method is called on block place,
            // before the "real" BlockEntity is set. That means, if we'd use the other method here,
            // it would create two BlockEntities.
            val music: BlockEntityMusic = blockEntity ?: return 0

            if (this.isGettingPower) {
                if (!music.isPowered) {
                    this.emitSound()
                }
                music.isPowered = true
            } else {
                music.isPowered = false
            }
        }
        return super.onUpdate(type)
    }

    enum class Instrument(sound: Sound) {
        HARP(Sound.NOTE_HARP),
        BASS_DRUM(Sound.NOTE_BD),
        SNARE_DRUM(Sound.NOTE_SNARE),
        CLICKS_AND_STICKS(Sound.NOTE_HAT),
        BASS(Sound.NOTE_BASS),
        BELLS(Sound.NOTE_BELL),
        FLUTE(Sound.NOTE_FLUTE),
        CHIMES(Sound.NOTE_CHIME),
        GUITAR(Sound.NOTE_GUITAR),
        XYLOPHONE(Sound.NOTE_XYLOPHONE),
        IRON_XYLOPHONE(Sound.NOTE_IRON_XYLOPHONE),
        COW_BELL(Sound.NOTE_COW_BELL),
        DIDGERIDOO(Sound.NOTE_DIDGERIDOO),
        BIT(Sound.NOTE_BIT),
        BANJO(Sound.NOTE_BANJO),
        PLING(Sound.NOTE_PLING),
        SKELETON(Sound.NOTE_SKELETON),
        WITHER_SKELETON(Sound.NOTE_WITHERSKELETON),
        ZOMBIE(Sound.NOTE_ZOMBIE),
        CREEPER(Sound.NOTE_CREEPER),
        ENDER_DRAGON(Sound.NOTE_ENDERDRAGON),
        PIGLIN(Sound.NOTE_PIGLIN);

        private val sound: Sound = sound

        fun getSound(): Sound {
            return sound
        }
    }

    companion object {
        val properties: BlockProperties = BlockProperties(BlockID.NOTEBLOCK)

    }
}
