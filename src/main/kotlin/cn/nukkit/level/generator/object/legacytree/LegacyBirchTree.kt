package cn.nukkit.level.generator.`object`.legacytree

/**
 * @author MagicDroidX (Nukkit Project)
 */
open class LegacyBirchTree : LegacyTreeGenerator() {
    override val type: WoodType
        get() = WoodType.BIRCH

    override fun placeObject(level: BlockManager, x: Int, y: Int, z: Int, random: RandomSourceProvider) {
        this.treeHeight = random.nextInt(2) + 5
        super.placeObject(level, x, y, z, random)
    }
}
