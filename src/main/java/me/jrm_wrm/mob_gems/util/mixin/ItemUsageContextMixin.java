package me.jrm_wrm.mob_gems.util.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;

@Mixin(ItemUsageContext.class)
public interface ItemUsageContextMixin {
    @Accessor("stack")
	ItemStack getStack();

	@Accessor("stack")
	void setStack(ItemStack stack);  
}
