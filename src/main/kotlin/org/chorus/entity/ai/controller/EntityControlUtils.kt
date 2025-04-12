package org.chorus.entity.ai.controller

import org.chorus.entity.ai.memory.CoreMemoryTypes
import org.chorus.entity.ai.memory.IMemoryStorage
import org.chorus.math.Vector3

interface EntityControlUtils {
    val memoryStorage: IMemoryStorage

    var lookTarget: Vector3?
        get() = memoryStorage.get(CoreMemoryTypes.LOOK_TARGET)
        set(lookTarget) {
            memoryStorage.set(CoreMemoryTypes.LOOK_TARGET, lookTarget)
        }

    var moveTarget: Vector3?
        get() = memoryStorage.get(CoreMemoryTypes.MOVE_TARGET)
        set(moveTarget) {
            memoryStorage.set(CoreMemoryTypes.MOVE_TARGET, moveTarget)
        }

    var moveDirectionStart: Vector3?
        get() = memoryStorage.get(CoreMemoryTypes.MOVE_DIRECTION_START)
        set(moveDirectionStart) {
            memoryStorage.set(CoreMemoryTypes.MOVE_DIRECTION_START, moveDirectionStart)
        }

    fun hasMoveDirection(): Boolean {
        return moveDirectionStart != null && moveDirectionEnd != null
    }

    var moveDirectionEnd: Vector3?
        get() = memoryStorage.get(CoreMemoryTypes.MOVE_DIRECTION_END)
        set(moveDirectionEnd) {
            memoryStorage.set(CoreMemoryTypes.MOVE_DIRECTION_END, moveDirectionEnd)
        }

    var isShouldUpdateMoveDirection: Boolean
        get() = memoryStorage.get(CoreMemoryTypes.SHOULD_UPDATE_MOVE_DIRECTION)
        set(shouldUpdateMoveDirection) {
            memoryStorage.set(CoreMemoryTypes.SHOULD_UPDATE_MOVE_DIRECTION, shouldUpdateMoveDirection)
        }

    fun isEnablePitch(): Boolean {
        return memoryStorage.get(CoreMemoryTypes.ENABLE_PITCH)
    }

    fun setEnablePitch(enablePitch: Boolean) {
        memoryStorage.set(CoreMemoryTypes.ENABLE_PITCH, enablePitch)
    }

    var isEnablePitch: Boolean
        get() = memoryStorage.get(CoreMemoryTypes.ENABLE_PITCH)
        set(enablePitch) {
            memoryStorage.set(CoreMemoryTypes.ENABLE_PITCH, enablePitch)
        }

    //    Unused
    //
    //
    //    public boolean isEnableYaw() {
    //        return memoryStorage.get(CoreMemoryTypes.ENABLE_YAW);
    //    }
    //
    //
    //
    //    public void setEnableYaw(boolean enableYaw) {
    //        memoryStorage.put(CoreMemoryTypes.ENABLE_YAW, enableYaw);
    //    }
    //
    //
    //
    //    public boolean isEnableHeadYaw() {
    //        return memoryStorage.get(CoreMemoryTypes.ENABLE_HEAD_YAW);
    //    }
    //
    //
    //
    //    public void setEnableHeadYaw(boolean enableHeadYaw) {
    //        memoryStorage.put(CoreMemoryTypes.ENABLE_HEAD_YAW, enableHeadYaw);
    //    }

    //    Unused
    //
    //
    //    public boolean isEnableYaw() {
    //        return memoryStorage.get(CoreMemoryTypes.ENABLE_YAW);
    //    }
    //
    //
    //
    //    public void setEnableYaw(boolean enableYaw) {
    //        memoryStorage.put(CoreMemoryTypes.ENABLE_YAW, enableYaw);
    //    }
    //
    //
    //
    //    public boolean isEnableHeadYaw() {
    //        return memoryStorage.get(CoreMemoryTypes.ENABLE_HEAD_YAW);
    //    }
    //
    //
    //
    //    public void setEnableHeadYaw(boolean enableHeadYaw) {
    //        memoryStorage.put(CoreMemoryTypes.ENABLE_HEAD_YAW, enableHeadYaw);
    //    }
}
