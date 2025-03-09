package cn.nukkit.dispenser

import cn.nukkit.block.BlockDispenser
import cn.nukkit.block.BlockUndyedShulkerBox
import cn.nukkit.item.Item
import cn.nukkit.level.vibration.VibrationEvent
import cn.nukkit.level.vibration.VibrationType
import cn.nukkit.math.BlockFace

class ShulkerBoxDispenseBehavior : DefaultDispenseBehavior() {
    override fun dispense(block: BlockDispenser, face: BlockFace, item: Item): Item? {
        val target = block.getSide(face)

        if (!target.canBeReplaced()) {
            success = false
            return null
        }

        val shulkerBox = item.block.clone() as BlockUndyedShulkerBox
        shulkerBox.level = block.level
        shulkerBox.layer = 0
        shulkerBox.position.x = target.position.x
        shulkerBox.position.y = target.position.y
        shulkerBox.position.z = target.position.z

        val shulkerBoxFace = if (shulkerBox.down().isTransparent) face else BlockFace.UP

        if (shulkerBox.place(item, target, target.getSide(shulkerBoxFace.opposite), shulkerBoxFace, 0.0, 0.0, 0.0, null)
                .also { success = it }
        ) {
            block.level.updateComparatorOutputLevel(target.position)
            block.level.vibrationManager.callVibrationEvent(
                VibrationEvent(
                    this,
                    target.position.add(0.5, 0.5, 0.5),
                    VibrationType.BLOCK_PLACE
                )
            )
        }

        return null
    }
}
