package me.jrm_wrm.mob_gems.status_effects;

import me.jrm_wrm.mob_gems.util.LivingEntityAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class CombustionStatusEffect extends StatusEffect {

    public CombustionStatusEffect() {
        super(StatusEffectType.HARMFUL, 0x666666);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        
        int duration = entity.getStatusEffect(this).getDuration();
        World world = entity.world;
        
        double x = entity.getX();
        double y = entity.getBodyY(0.5F);
        double z = entity.getZ();
        
        if (world.isClient) {
            x += world.random.nextDouble() * entity.getWidth() - entity.getWidth()/2;
            y += world.random.nextDouble() * entity.getHeight() - entity.getHeight()/2;
            z += world.random.nextDouble() * entity.getWidth() - entity.getWidth()/2;

            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);

            return;
        }

        if (duration == 1) {
            // don't destroy blocks if mob griefing is turned off
            Explosion.DestructionType destructionType = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) 
                ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;

            ((LivingEntityAccess) entity).setIgnited(false);
            entity.damage(DamageSource.explosion((LivingEntity)null), 1000);    
            world.createExplosion(null, entity.getX(), entity.getBodyY(0.5F), entity.getZ(), amplifier + 1, destructionType);
        }
    }

    // Play the fuse sound effect when the status effect gets applied
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1.0F, 0.5F);
        ((LivingEntityAccess) entity).setIgnited(true);
    }
    
}
