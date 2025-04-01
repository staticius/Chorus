package org.chorus.entity.ai.controller

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.IMemoryStorage
import org.chorus.math.Vector3

interface EntityControlUtils {
    val memoryStorage: IMemoryStorage

    fun getMemoryStorage(): IMemoryStorage

    var lookTarget: Vector3?
        get() = memoryStorage[CoreMemoryTypes.LOOK_TARGET]
        set(lookTarget) {
            memoryStorage[CoreMemoryTypes.LOOK_TARGET] = lookTarget
        }

    var moveTarget: Vector3?
        get() = memoryStorage[CoreMemoryTypes.MOVE_TARGET]
        set(moveTarget) {
            memoryStorage[CoreMemoryTypes.MOVE_TARGET] = moveTarget
        }

    var moveDirectionStart: Vector3?
        get() = memoryStorage[CoreMemoryTypes.MOVE_DIRECTION_START]
        set(moveDirectionStart) {
            memoryStorage[CoreMemoryTypes.MOVE_DIRECTION_START] = moveDirectionStart
        }

    fun hasMoveDirection(): Boolean {
        return moveDirectionStart != null && moveDirectionEnd != null
    }

    var moveDirectionEnd: Vector3?
        get() = memoryStorage[CoreMemoryTypes.MOVE_DIRECTION_END]
        set(moveDirectionEnd) {
            memoryStorage[CoreMemoryTypes.MOVE_DIRECTION_END] = moveDirectionEnd
        }

    var isShouldUpdateMoveDirection: Boolean
        get() = memoryStorage[CoreMemoryTypes.SHOULD_UPDATE_MOVE_DIRECTION]
        set(shouldUpdateMoveDirection) {
            memoryStorage[CoreMemoryTypes.SHOULD_UPDATE_MOVE_DIRECTION] = shouldUpdateMoveDirection
        }

    fun isEnablePitch(): Boolean {
        return memoryStorage[CoreMemoryTypes.ENABLE_PITCH]
    }

    fun setEnablePitch(enablePitch: Boolean) {
        memoryStorage[CoreMemoryTypes.ENABLE_PITCH] = enablePitch
    }

    var isEnablePitch: Boolean
        get() = memoryStorage[CoreMemoryTypes.ENABLE_PITCH]
        set(enablePitch) {
            memoryStorage[CoreMemoryTypes.ENABLE_PITCH] = enablePitch
        }

    //    Unused
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

    //    Unused
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
}
