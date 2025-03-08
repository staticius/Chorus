package cn.nukkit.level.generator.terra.delegate

import cn.nukkit.entity.Entity
import com.dfsek.terra.api.util.vector.Vector3
import com.dfsek.terra.api.world.ServerWorld

class PNXEntity(private val nukkitEntity: Entity, private var world: ServerWorld) : com.dfsek.terra.api.entity.Entity {
    override fun position(): Vector3 {
        return Vector3.of(nukkitEntity.getX(), nukkitEntity.getY(), nukkitEntity.getZ())
    }

    override fun position(vector3: Vector3) {
        nukkitEntity.position.setX(vector3.x)
        nukkitEntity.position.setY(vector3.y)
        nukkitEntity.position.setZ(vector3.z)
    }

    override fun world(serverWorld: ServerWorld) {
        nukkitEntity.level = (serverWorld as PNXServerWorld).generatorWrapper.level
        world = serverWorld
    }

    override fun world(): ServerWorld {
        return world
    }

    override fun getHandle(): Any {
        return nukkitEntity
    }

    fun entity(): Entity {
        return nukkitEntity
    }
}
