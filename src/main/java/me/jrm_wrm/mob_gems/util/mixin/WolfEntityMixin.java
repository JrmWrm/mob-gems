package me.jrm_wrm.mob_gems.util.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.jrm_wrm.mob_gems.items.BraceletItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntity {
 
	protected WolfEntityMixin() {
		super(null, null);
	}
 
	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "tickAngerLogic"))
	public void stopAngerOnTickMove(CallbackInfo info) {
 
		WolfEntity wolf = (WolfEntity) (Object) this;
		LivingEntity target = wolf.getTarget();
 
		if (target != null) {
			ItemStack bracelet = BraceletItem.getEquippedBracelet(target);
			if (bracelet != ItemStack.EMPTY) {
				if (!((BraceletItem) bracelet.getItem()).isAugmenter) {
					wolf.stopAnger();
					Optional<Goal> revengeGoal = targetSelector.getRunningGoals().map(goal -> goal.getGoal())
							.filter(goal -> goal instanceof RevengeGoal).findFirst();
                    revengeGoal.ifPresent(Goal::stop);
				}
			}
		}
	}
 
}
