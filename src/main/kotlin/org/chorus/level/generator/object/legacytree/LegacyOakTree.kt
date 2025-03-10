package org.chorus.level.generator.`object`.legacytree


class LegacyOakTree : LegacyTreeGenerator() {
    override val type: WoodType
        get() = WoodType.OAK

    override fun placeObject(level: BlockManager, x: Int, y: Int, z: Int, random: RandomSourceProvider) {
        this.treeHeight = random.nextInt(3) + 4
        super.placeObject(level, x, y, z, random)
    }
}
