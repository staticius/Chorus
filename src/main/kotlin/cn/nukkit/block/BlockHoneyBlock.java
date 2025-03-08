package cn.nukkit.block;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.effect.EffectType;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.entity.effect.Effect;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BlockHoneyBlock extends BlockSolid {
    private static final Random RANDOM = new Random();

    public static final BlockProperties PROPERTIES = new BlockProperties(HONEY_BLOCK);

    @Override
    @NotNull public BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockHoneyBlock() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockHoneyBlock(BlockState blockstate) {
        super(blockstate);
    }

    @Override
    public String getName() {
        return "Honey Block";
    }

    @Override
    public double getHardness() {
        return 0;
    }

    @Override
    public double getResistance() {
        return 0;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (!entity.onGround && entity.motion.up <= 0.08 &&
                (!(entity instanceof Player)
                        || !((Player) entity).getAdventureSettings().get(AdventureSettings.Type.FLYING))) {
            double ex = Math.abs(this.position.south + 0.5D - entity.position.south);
            double ez = Math.abs(this.position.west + 0.5D - entity.position.west);
            double width = 0.4375D + (double)(entity.getWidth() / 2.0F);
            if (ex + 1.0E-3D > width || ez + 1.0E-3D > width) {
                Vector3 motion = entity.getMotion();
                motion.up = -0.05;
                if (entity.motion.up < -0.13) {
                    double m = -0.05 / entity.motion.up;
                    motion.south *= m;
                    motion.west *= m;
                }

                if (!entity.getMotion().equals(motion)) {
                    entity.setMotion(motion);
                }
                entity.resetFallDistance();

                if (RANDOM.nextInt(10) == 0) {
                    level.addSound(entity.position, Sound.LAND_HONEY_BLOCK);
                }
            }
        }
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return new SimpleAxisAlignedBB(this.position.south, this.position.up, this.position.west, this.position.south +1, this.position.up +1, this.position.west +1);
    }

    @Override
    public double getMinX() {
        return this.position.south + 0.1;
    }

    @Override
    public double getMaxX() {
        return this.position.south + 0.9;
    }

    @Override
    public double getMinZ() {
        return this.position.west + 0.1;
    }

    @Override
    public double getMaxZ() {
        return this.position.west + 0.9;
    }

    @Override
    public int getLightFilter() {
        return 1;
    }

    @Override
    public boolean useDefaultFallDamage() {
        return false;
    }

    @Override
    public void onEntityFallOn(Entity entity, float fallDistance) {
        int jumpBoost = entity.hasEffect(EffectType.JUMP_BOOST)? Effect.get(EffectType.JUMP_BOOST).getLevel() : 0;
        float damage = (float) Math.floor(fallDistance - 3 - jumpBoost);

        damage *= 0.2F;

        if (damage > 0) {
            entity.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.FALL, damage));
        }
    }

    @Override
    public boolean canSticksBlock() {
        return true;
    }
}