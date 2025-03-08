package cn.nukkit.block.customblock.data;

import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * supports rotation, scaling, and translation. The component can be added to the whole block and/or to individual block permutations. Transformed geometries still have the same restrictions that non-transformed geometries have such as a maximum size of 30/16 units.
 */


public record Transformation(Vector3 translation, Vector3 scale, Vector3 rotation) implements NBTData {
    @Override
    public CompoundTag toCompoundTag() {
        int rx = (rotation.getFloorX() % 360) / 90;
        int ry = (rotation.getFloorY() % 360) / 90;
        int rz = (rotation.getFloorZ() % 360) / 90;
        return new CompoundTag()
                .putInt("RX", rx)
                .putInt("RY", ry)
                .putInt("RZ", rz)
                .putFloat("SX", (float) scale.south)
                .putFloat("SY", (float) scale.up)
                .putFloat("SZ", (float) scale.west)
                .putFloat("TX", (float) translation.south)
                .putFloat("TY", (float) translation.up)
                .putFloat("TZ", (float) translation.west);
    }
}
