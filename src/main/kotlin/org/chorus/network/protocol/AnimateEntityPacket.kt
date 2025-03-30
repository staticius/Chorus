package org.chorus.network.protocol

import org.chorus.network.connection.util.HandleByteBuf
import org.chorus.network.protocol.types.ActorRuntimeID

data class AnimateEntityPacket(
    val animation: String,
    val nextState: String,
    val stopExpression: String,
    val stopExpressionVersion: Int,
    val controller: String,
    val blendOutTime: Float,
    val runtimeIDs: MutableList<ActorRuntimeID>,
) : DataPacket(), PacketEncoder {
    override fun encode(byteBuf: HandleByteBuf) {
        byteBuf.writeString(this.animation)
        byteBuf.writeString(this.nextState)
        byteBuf.writeString(this.stopExpression)
        byteBuf.writeInt(this.stopExpressionVersion)
        byteBuf.writeString(this.controller)
        byteBuf.writeFloatLE(this.blendOutTime)
        byteBuf.writeArray(this.runtimeIDs.toTypedArray()) {
            byteBuf.writeActorRuntimeID(it)
        }
    }

    data class Animation(
        val animation: String,
        val nextState: String,
        val stopExpression: String,
        val stopExpressionVersion: Int,
        val controller: String,
        val blendOutTime: Float,
    ) {
        companion object {
            const val DEFAULT_BLEND_OUT_TIME: Float = 0.0f
            const val DEFAULT_STOP_EXPRESSION: String = "query.any_animation_finished"
            const val DEFAULT_CONTROLLER: String = "__runtime_controller"
            const val DEFAULT_NEXT_STATE: String = "default"
            const val DEFAULT_STOP_EXPRESSION_VERSION: Int = 16777216
        }
    }

    override fun pid(): Int {
        return ProtocolInfo.ANIMATE_ENTITY_PACKET
    }

    override fun handle(handler: PacketHandler) {
        handler.handle(this)
    }

    companion object {
        fun fromAnimation(ani: Animation): AnimateEntityPacket {
            return AnimateEntityPacket(
                animation = ani.animation,
                nextState = ani.nextState,
                blendOutTime = ani.blendOutTime,
                stopExpression = ani.stopExpression,
                controller = ani.controller,
                stopExpressionVersion = ani.stopExpressionVersion,
                runtimeIDs = mutableListOf()
            )
        }
    }
}
