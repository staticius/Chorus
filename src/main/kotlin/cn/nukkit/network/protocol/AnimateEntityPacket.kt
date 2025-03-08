package cn.nukkit.network.protocol

import cn.nukkit.network.connection.util.HandleByteBuf
import lombok.*

/**
 * @author IWareQ
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class AnimateEntityPacket : DataPacket() {
    var animation: String? = null
    var nextState: String? = null
    var stopExpression: String? = null
    var stopExpressionVersion: Int = 0
    var controller: String? = null
    var blendOutTime: Float = 0f
    var entityRuntimeIds: MutableList<Long> = ArrayList()

    override fun decode(byteBuf: HandleByteBuf) {
        this.animation = byteBuf.readString()
        this.nextState = byteBuf.readString()
        this.stopExpression = byteBuf.readString()
        this.stopExpressionVersion = byteBuf.readInt()
        this.controller = byteBuf.readString()
        this.blendOutTime = byteBuf.readFloatLE()
        var i = 0
        while (i < byteBuf.readUnsignedVarInt()) {
            entityRuntimeIds.add(byteBuf.readEntityRuntimeId())
            i++
        }
    }

    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(animation!!)
        byteBuf.writeString(nextState!!)
        byteBuf.writeString(stopExpression!!)
        byteBuf.writeInt(this.stopExpressionVersion)
        byteBuf.writeString(controller!!)
        byteBuf.writeFloatLE(this.blendOutTime)
        byteBuf.writeUnsignedVarInt(entityRuntimeIds.size)
        for (entityRuntimeId in this.entityRuntimeIds) {
            byteBuf.writeEntityRuntimeId(entityRuntimeId)
        }
    }

    /**
     * 从 [Animation] 对象中解析数据并写入到包
     */
    fun parseFromAnimation(ani: Animation) {
        this.animation = ani.animation
        this.nextState = ani.nextState
        this.blendOutTime = ani.blendOutTime
        this.stopExpression = ani.stopExpression
        this.controller = ani.controller
        this.stopExpressionVersion = ani.stopExpressionVersion
    }

    /**
     * 包含一个实体动画的信息的记录类<br></br>
     * 用于[cn.nukkit.network.protocol.AnimateEntityPacket]网络包
     */
    @Builder
    class Animation {
        val animation: String? = null

        @Builder.Default
        val nextState: String = DEFAULT_NEXT_STATE

        @Builder.Default
        val blendOutTime: Float = DEFAULT_BLEND_OUT_TIME

        @Builder.Default
        val stopExpression: String = DEFAULT_STOP_EXPRESSION

        @Builder.Default
        val controller: String = DEFAULT_CONTROLLER

        @Builder.Default
        val stopExpressionVersion: Int = DEFAULT_STOP_EXPRESSION_VERSION

        companion object {
            const val DEFAULT_BLEND_OUT_TIME: Float = 0.0f
            const val DEFAULT_STOP_EXPRESSION: String = "query.any_animation_finished"
            const val DEFAULT_CONTROLLER: String = "__runtime_controller"
            const val DEFAULT_NEXT_STATE: String = "default"
            const val DEFAULT_STOP_EXPRESSION_VERSION: Int = 16777216
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.Companion.ANIMATE_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }
}
