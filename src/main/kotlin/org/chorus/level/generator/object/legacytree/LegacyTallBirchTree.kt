package org.chorus.level.generator.`object`.legacytree

/**
 * @author MagicDroidX (Nukkit Project)
 */
class LegacyTallBirchTree : LegacyBirchTree() {
    override fun placeObject(level: BlockManager, x: Int, y: Int, z: Int, random: RandomSourceProvider) {
        this.treeHeight = random.nextInt(3) + 10
        super.placeObject(level, x, y, z, random)
    }
}
