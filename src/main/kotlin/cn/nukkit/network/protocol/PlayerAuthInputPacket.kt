package cn.nukkit.network.protocol

import cn.nukkit.inventory.InventoryType.Companion.from
import cn.nukkit.math.*
import cn.nukkit.nbt.tag.ListTag.size
import cn.nukkit.network.connection.util.HandleByteBuf
import cn.nukkit.network.protocol.types.*
import cn.nukkit.network.protocol.types.itemstack.request.ItemStackRequest
import lombok.*
import java.util.*

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class PlayerAuthInputPacket : DataPacket() {
    var yaw: Float = 0f
    var pitch: Float = 0f
    var headYaw: Float = 0f
    var position: Vector3f? = null
    var motion: Vector2? = null
    var inputData: MutableSet<AuthInputAction> = EnumSet.noneOf(AuthInputAction::class.java)
    var inputMode: InputMode? = null
    var playMode: ClientPlayMode? = null
    var interactionModel: AuthInteractionModel? = null
    var interactRotation: Vector2f? = null
    var tick: PlayerInputTick? = null
    var delta: Vector3f? = null

    /**
     * [.inputData] must contain [AuthInputAction.PERFORM_ITEM_STACK_REQUEST] in order for this to not be null.
     *
     * @since v428
     */
    var itemStackRequest: ItemStackRequest? = null
    val blockActionData: MutableMap<PlayerActionType, PlayerBlockActionData> = EnumMap(
        PlayerActionType::class.java
    )
    var analogMoveVector: Vector2f? = null

    /**
     * @since 649
     */
    var predictedVehicle: Long = 0

    /**
     * @since 662
     */
    var vehicleRotation: Vector2f? = null
    var cameraOrientation: Vector3f? = null

    /**
     * @since 766
     */
    var rawMoveVector: Vector2f? = null

    override fun decode(byteBuf: HandleByteBuf) {
        this.pitch = byteBuf.readFloatLE()
        this.yaw = byteBuf.readFloatLE()
        this.position = byteBuf.readVector3f()
        this.motion = Vector2(byteBuf.readFloatLE().toDouble(), byteBuf.readFloatLE().toDouble())
        this.headYaw = byteBuf.readFloatLE()

        val inputData = byteBuf.readUnsignedBigVarInt(AuthInputAction.Companion.size())
        for (i in 0..<AuthInputAction.Companion.size()) {
            if (inputData.testBit(i)) {
                this.inputData.add(AuthInputAction.Companion.from(i))
            }
        }

        this.inputMode = InputMode.Companion.fromOrdinal(byteBuf.readUnsignedVarInt())
        this.playMode = ClientPlayMode.Companion.fromOrdinal(byteBuf.readUnsignedVarInt())
        this.interactionModel = AuthInteractionModel.Companion.fromOrdinal(byteBuf.readUnsignedVarInt())

        this.interactRotation = byteBuf.readVector2f()

        this.tick = byteBuf.readPlayerInputTick()
        this.delta = byteBuf.readVector3f()

        if (this.inputData.contains(AuthInputAction.PERFORM_ITEM_STACK_REQUEST)) {
            this.itemStackRequest = byteBuf.readItemStackRequest()
        }

        if (this.inputData.contains(AuthInputAction.PERFORM_BLOCK_ACTIONS)) {
            val arraySize = byteBuf.readVarInt()
            for (i in 0..<arraySize) {
                val type: PlayerActionType = PlayerActionType.Companion.from(byteBuf.readVarInt())
                when (type) {
                    PlayerActionType.START_DESTROY_BLOCK, PlayerActionType.ABORT_DESTROY_BLOCK, PlayerActionType.CRACK_BLOCK, PlayerActionType.PREDICT_DESTROY_BLOCK, PlayerActionType.CONTINUE_DESTROY_BLOCK -> blockActionData[type] =
                        PlayerBlockActionData(type, byteBuf.readSignedBlockPosition(), byteBuf.readVarInt())

                    else -> blockActionData[type] = PlayerBlockActionData(type, null, -1)
                }
            }
        }

        if (this.inputData.contains(AuthInputAction.IN_CLIENT_PREDICTED_IN_VEHICLE)) {
            this.vehicleRotation = byteBuf.readVector2f()
            this.predictedVehicle = byteBuf.readVarLong()
        }

        // since 1.19.70-r1, v575
        this.analogMoveVector = byteBuf.readVector2f()

        this.cameraOrientation = byteBuf.readVector3f()

        this.rawMoveVector = byteBuf.readVector2f()
    }

    override fun encode(byteBuf: HandleByteBuf) {
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.PLAYER_AUTH_INPUT_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}