package me.jrm_wrm.mob_gems.items;

import java.util.List;

import me.jrm_wrm.mob_gems.MobGems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MobGemItem extends Item {
    private EntityType<?> type;
    private int tint; 

    public static final int CAGE_RANGE = 10;
    private static final String TRANSLATION_ID = "mob_gem";

    public <T extends MobEntity> MobGemItem(EntityType<T> type, int tint) {  
        super(new FabricItemSettings()
            .group(MobGems.MOD_ITEM_GROUP)
            .maxCount(1)
            .rarity(Rarity.RARE)); 
          
        this.type = type;
        this.tint = tint;
    }

    public int getTint() {
        return tint;
    }

    public String getTypeKey() {
        return type.getTranslationKey();
    }

    public Text getTypeName() {
        return type.getName();
    }

    // all mob gems will have the same translation key
    @Override 
    public String getTranslationKey() {
        return "item."+MobGems.MOD_ID+"."+TRANSLATION_ID;
    }

    // gets the tooltip text from the lang file and appends the entity name
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(new TranslatableText(getTranslationKey() + ".tooltip").append(type.getName()));
    }

    /**
     * Called every tick if the mob gem is in a bracelet.
     * Don't override, but override onAugmenterTick() and onDiminisherTick() instead
     * {@param slot slot in the bracelet}
     */
    public void onBraceletTick(ItemStack bracelet, ItemStack gemStack, World world, Entity entity, int slot) {
        if (!(entity instanceof LivingEntity)) return;
        LivingEntity livingEntity = (LivingEntity) entity;

        // if not equipped, return
        if (bracelet != BraceletItem.getEquippedBracelet(livingEntity)) return;

        BraceletItem braceletItem = (BraceletItem) bracelet.getItem();
        if (braceletItem.isAugmenter) onAugmenterTick(bracelet, gemStack, world, livingEntity, slot); 
        else onDiminisherTick(bracelet, gemStack, world, livingEntity, slot);
    }

    /**
     * Called every tick if the mob gem is in an augmenter bracelet (gold)
     * Override to add passive behaviour while wearing
     * {@param slot slot in the bracelet}
     */
    public void onAugmenterTick(ItemStack bracelet, ItemStack gemStack, World world, LivingEntity livingEntity, int slot) {}

    /**
     * Called every tick if the mob gem is in a diminisher bracelet (iron)
     * Override to add passive behaviour while wearing
     * {@param slot slot in the bracelet}
     */
    public void onDiminisherTick(ItemStack bracelet, ItemStack gemStack, World world, LivingEntity livingEntity, int slot) {}

    /**
     * Called every tick if the mob gem is in a Gem Cage
     * Override to add passive behaviour while in a Gem Cage
     * {@param pos BlockPos of the Gem Cage}
     */
    public void onCageTick(ItemStack stack, World world, BlockPos pos) {}
    
}
