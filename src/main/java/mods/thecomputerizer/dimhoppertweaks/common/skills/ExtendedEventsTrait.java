package mods.thecomputerizer.dimhoppertweaks.common.skills;

import codersafterdark.reskillable.api.unlockable.Trait;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public abstract class ExtendedEventsTrait extends Trait {

    protected static final ResourceLocation AGILITY = new ResourceLocation("reskillable","agility");
    protected static final ResourceLocation ATTACK = new ResourceLocation("reskillable","attack");
    protected static final ResourceLocation BUILDING = new ResourceLocation("reskillable","building");
    protected static final ResourceLocation DEFENSE = new ResourceLocation("reskillable","defense");
    protected static final ResourceLocation FARMING = new ResourceLocation("reskillable","farming");
    protected static final ResourceLocation GATHERING = new ResourceLocation("reskillable","gathering");
    protected static final ResourceLocation MAGIC = new ResourceLocation("reskillable","magic");
    protected static final ResourceLocation MINING = new ResourceLocation("reskillable","mining");
    protected static final ResourceLocation RESEARCH = Constants.res("research");
    protected static final ResourceLocation VOID = Constants.res("void");

    private static String[] getReqs(String ... skills) {
        for(int i=0; i<skills.length; i++) {
            boolean isCustom = skills[i].startsWith("research") || skills[i].startsWith("void");
            skills[i] = (isCustom ? Constants.MODID : "reskillable")+":"+skills[i];
        }
        return skills;
    }

    public ExtendedEventsTrait(String name, int x, int y, ResourceLocation skillRes, int cost, String... requirements) {
        super(Constants.res(name),x,y,skillRes,cost,getReqs(requirements));
    }

    public void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent ev) {}

    public void onExplosionDetonate(ExplosionEvent.Detonate ev) {}

    public void onLivingKnockback(LivingKnockBackEvent ev) {}

    public void onLootingLevel(LootingLevelEvent ev) {}

    public void onTamedHurt(LivingHurtEvent ev) {}

    public void onTamedDamageOther(LivingDamageEvent ev) {}

    @SuppressWarnings("deprecation")
    public void onBlockPlaced(BlockEvent.PlaceEvent ev) {}

    public boolean shouldCancelNoDamiThresholds() {
        return false;
    }

    public void onShiftRightClickFood(EntityPlayer player, ItemFood food) {}

    public void onShiftRightClickPotion(EntityPlayer player, ItemStack potionStack) {}
}
