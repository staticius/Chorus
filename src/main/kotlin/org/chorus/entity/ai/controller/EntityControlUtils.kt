package org.chorus.entity.ai.controller

import cn.nukkit.entity.ai.memory.CoreMemoryTypes
import cn.nukkit.entity.ai.memory.IMemoryStorage
import cn.nukkit.math.*

/**
 * 一些通用的实体运动控制方法
 *
 *
 * Some general entity motion control methods
 */
interface EntityControlUtils {
    var lookTarget: Vector3?
        get() = memoryStorage!!.get<Vector3>(CoreMemoryTypes.Companion.LOOK_TARGET)
        set(lookTarget) {
            memoryStorage!!.put<Vector3>(CoreMemoryTypes.Companion.LOOK_TARGET, lookTarget)
        }

    var moveTarget: Vector3?
        get() = memoryStorage!!.get<Vector3>(CoreMemoryTypes.Companion.MOVE_TARGET)
        set(moveTarget) {
            memoryStorage!!.put<Vector3>(CoreMemoryTypes.Companion.MOVE_TARGET, moveTarget)
        }

    var moveDirectionStart: Vector3?
        get() = memoryStorage!!.get<Vector3>(CoreMemoryTypes.Companion.MOVE_DIRECTION_START)
        set(moveDirectionStart) {
            memoryStorage!!.put<Vector3>(
                CoreMemoryTypes.Companion.MOVE_DIRECTION_START,
                moveDirectionStart
            )
        }

    fun hasMoveDirection(): Boolean {
        return moveDirectionStart != null && moveDirectionEnd != null
    }

    var moveDirectionEnd: Vector3?
        get() = memoryStorage!!.get<Vector3>(CoreMemoryTypes.Companion.MOVE_DIRECTION_END)
        set(moveDirectionEnd) {
            memoryStorage!!.put<Vector3>(
                CoreMemoryTypes.Companion.MOVE_DIRECTION_END,
                moveDirectionEnd
            )
        }

    var isShouldUpdateMoveDirection: Boolean
        get() = memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.SHOULD_UPDATE_MOVE_DIRECTION)
        set(shouldUpdateMoveDirection) {
            memoryStorage!!.put<Boolean>(
                CoreMemoryTypes.Companion.SHOULD_UPDATE_MOVE_DIRECTION,
                shouldUpdateMoveDirection
            )
        }

    fun isEnablePitch(): Boolean {
        return memoryStorage!!.get<Boolean>(CoreMemoryTypes.Companion.ENABLE_PITCH)
    }

    fun setEnablePitch(enablePitch: Boolean) {
        memoryStorage!!.put<Boolean>(CoreMemoryTypes.Companion.ENABLE_PITCH, enablePitch)
    }

    //    暂时不使用
    //
    //
    //    public boolean isEnableYaw() {
    //        return getMemoryStorage().get(CoreMemoryTypes.ENABLE_YAW);
    //    }
    //
    //
    //
    //    public void setEnableYaw(boolean enableYaw) {
    //        getMemoryStorage().put(CoreMemoryTypes.ENABLE_YAW, enableYaw);
    //    }
    //
    //
    //
    //    public boolean isEnableHeadYaw() {
    //        return getMemoryStorage().get(CoreMemoryTypes.ENABLE_HEAD_YAW);
    //    }
    //
    //
    //
    //    public void setEnableHeadYaw(boolean enableHeadYaw) {
    //        getMemoryStorage().put(CoreMemoryTypes.ENABLE_HEAD_YAW, enableHeadYaw);
    //    }
    val memoryStorage: IMemoryStorage?
        //    暂时不使用
        get

    //    暂时不使用
    //
    //
    //    public boolean isEnableYaw() {
    //        return getMemoryStorage().get(CoreMemoryTypes.ENABLE_YAW);
    //    }
    //
    //
    //
    //    public void setEnableYaw(boolean enableYaw) {
    //        getMemoryStorage().put(CoreMemoryTypes.ENABLE_YAW, enableYaw);
    //    }
    //
    //
    //
    //    public boolean isEnableHeadYaw() {
    //        return getMemoryStorage().get(CoreMemoryTypes.ENABLE_HEAD_YAW);
    //    }
    //
    //
    //
    //    public void setEnableHeadYaw(boolean enableHeadYaw) {
    //        getMemoryStorage().put(CoreMemoryTypes.ENABLE_HEAD_YAW, enableHeadYaw);
    //    }
    fun getMemoryStorage(): IMemoryStorage?
}
