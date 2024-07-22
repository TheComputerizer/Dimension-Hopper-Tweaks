package mods.thecomputerizer.dimhoppertweaks.registry.traits;

import codersafterdark.reskillable.api.unlockable.Trait;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ExplosionEvent.Detonate;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;
import static net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START;
import static org.lwjgl.opengl.GL11.GL_QUADS;

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
            skills[i] = (isCustom ? MODID : "reskillable")+":"+skills[i];
        }
        return skills;
    }

    private double frameHeight;
    private double v;

    public ExtendedEventsTrait(String name, int x, int y, ResourceLocation skillRes, int cost, String... requirements) {
        super(DHTRef.res(name),x,y,skillRes,cost,getReqs(requirements));
        this.frameHeight = 1d;
        this.v = 1d;
    }

    @SubscribeEvent
    public void animationTick(ClientTickEvent event) {
        if(event.phase==START) {
            this.v-=this.frameHeight;
            if(this.v<0d) this.v+=1d;
        }
    }

    public void draw(int x, int y) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();
        buf.begin(GL_QUADS,POSITION_TEX);
        buf.pos(x+5d,y+21d,0d).tex(0d,this.v+this.frameHeight).endVertex();
        buf.pos(x+21d,y+21d,0d).tex(1d,this.v+this.frameHeight).endVertex();
        buf.pos(x+21d,y+5d,0d).tex(1d,this.v).endVertex();
        buf.pos(x+5d,y+5d,0d).tex(0d,this.v).endVertex();
        tessellator.draw();
    }

    public void onChangeDimensions(PlayerChangedDimensionEvent ev) {}

    public void onExplosionDetonate(Detonate ev) {}

    public void onLivingKnockback(LivingKnockBackEvent ev) {}

    public void onLootingLevel(LootingLevelEvent ev) {}

    public void onTamedHurt(LivingHurtEvent ev) {}

    public void onTamedDamageOther(LivingDamageEvent ev) {}

    @SuppressWarnings("deprecation")
    public void onBlockPlaced(PlaceEvent ev) {}

    public boolean shouldCancelNoDamiThresholds() {
        return false;
    }

    public void onShiftRightClickFood(EntityPlayer player, ItemFood food) {}

    public void onShiftRightClickPotion(EntityPlayer player, ItemStack potionStack) {}

    public void onSetTargetToTamed(EntityPlayer player, EntityLiving attacker) {}

    public void onFinishUsingItem(EntityPlayer player, ItemStack stack) {}
    
    public void onXPPickup(EntityPlayer player, EntityXPOrb xp) {}

    public void setAnimationFrames(int frames) {
        if(frames<=0) frames = 1;
        if(frames>1) EVENT_BUS.register(this);
        this.frameHeight = 1d/((double)frames);
        this.v = 1d-this.frameHeight;
    }
    
    protected void setIcon(String namespace, String category, String path) {
        setIcon(new ResourceLocation(namespace,"textures/"+category+"/"+path+".png"));
    }
    
    protected void setIcon(String category, String path) {
        setIcon(new ResourceLocation("textures/"+category+"/"+path+".png"));
    }
}