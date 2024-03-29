package mods.thecomputerizer.dimhoppertweaks.registry.traits;

import codersafterdark.reskillable.api.unlockable.Trait;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public abstract class ExtendedEventsTrait extends Trait {

    protected static final ResourceLocation AGILITY = new ResourceLocation("reskillable","agility");
    protected static final ResourceLocation ATTACK = new ResourceLocation("reskillable","attack");
    protected static final ResourceLocation BUILDING = new ResourceLocation("reskillable","building");
    protected static final ResourceLocation DEFENSE = new ResourceLocation("reskillable","defense");
    protected static final ResourceLocation FARMING = new ResourceLocation("reskillable","farming");
    protected static final ResourceLocation GATHERING = new ResourceLocation("reskillable","gathering");
    protected static final ResourceLocation MAGIC = new ResourceLocation("reskillable","magic");
    protected static final ResourceLocation MINING = new ResourceLocation("reskillable","mining");
    protected static final ResourceLocation RESEARCH = DHTRef.res("research");
    protected static final ResourceLocation VOID = DHTRef.res("void");

    private static String[] getReqs(String ... skills) {
        for(int i=0; i<skills.length; i++) {
            boolean isCustom = skills[i].startsWith("research") || skills[i].startsWith("void");
            skills[i] = (isCustom ? DHTRef.MODID : "reskillable")+":"+skills[i];
        }
        return skills;
    }

    private int animationFrames = 1;
    private float animationFrameFactor = 16f;
    private int curFrame = 1;

    public ExtendedEventsTrait(String name, int x, int y, ResourceLocation skillRes, int cost, String... requirements) {
        super(DHTRef.res(name),x,y,skillRes,cost,getReqs(requirements));
    }

    @SubscribeEvent
    public void animationTick(ClientTickEvent event) {
        if(event.phase==Phase.START) {
            this.curFrame++;
            if(this.curFrame>this.animationFrames) this.curFrame = 1;
        }
    }

    public void draw(int x, int y) {
        float minV = (float)(this.curFrame-1)/this.animationFrameFactor;
        float maxV = minV+this.animationFrameFactor;
        Gui.drawModalRectWithCustomSizedTexture(x+5,y+5,0f,minV,16,16,16f,maxV);
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

    public void onSetTargetToTamed(EntityPlayer player, EntityLiving attacker) {}

    public void onFinishUsingItem(EntityPlayer player, ItemStack stack) {}

    public ExtendedEventsTrait setAnimationFrames(int frames) {
        if(frames>1) MinecraftForge.EVENT_BUS.register(this);
        this.animationFrames = frames;
        this.animationFrameFactor = 16f/(float)frames;
        this.curFrame = 1;
        return this;
    }
}
