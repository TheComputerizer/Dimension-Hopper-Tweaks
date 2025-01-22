package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mobends;

import goblinbob.mobends.core.bender.EntityBender;
import goblinbob.mobends.core.bender.EntityBenderRegistry;
import goblinbob.mobends.core.client.model.ModelPartTransform;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.standard.client.model.armor.ArmorModelFactory;
import goblinbob.mobends.standard.client.model.armor.ArmorWrapper;
import goblinbob.mobends.standard.client.model.armor.IPartWrapper;
import goblinbob.mobends.standard.client.model.armor.MalformedArmorModelException;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.data.PlayerData;
import goblinbob.mobends.standard.previewer.PlayerPreviewer;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IArmorWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.mod_access.ModelArmorInfinityAccess;
import morph.avaritia.client.render.entity.ModelArmorInfinity;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Objects;

import static goblinbob.mobends.core.bender.EntityBenderRegistry.instance;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(value = ArmorWrapper.class, remap = false)
public abstract class MixinArmorWrapper implements IArmorWrapper {

    @Shadow protected ModelBiped original;
    @Shadow protected boolean mutated;
    @Shadow protected ModelPartTransform bodyTransform;
    @Shadow protected List<IPartWrapper> partWrappers;
    @Shadow public abstract void apply();
    @Shadow public abstract void deapply();

    /**
     * @author The_Computerizer
     * @reason Account for armor sets like infinity armor that have sub renders
     */
    @Overwrite(remap = true)
    public void render(Entity entity, float limbSwing, float swingAmount, float ageInTicks, float headYaw,
                       float headPitch, float scale) {
        if(!this.mutated) throw new MalformedArmorModelException("Operating on a demutated armor wrapper.");
        else if(entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase)entity;
            EntityBender<EntityLivingBase> bender = instance.getForEntity(living);
            if(Objects.nonNull(bender)) {
                EntityData<?> data = EntityDatabase.instance.get(living);
                if(data instanceof BipedEntityData) {
                    if(data instanceof PlayerData && PlayerPreviewer.isPreviewInProgress())
                        data = PlayerPreviewer.getPreviewData();
                    BipedEntityData<?> dataBiped = (BipedEntityData<?>)data;
                    this.bodyTransform.syncUp(dataBiped.body);
                    this.partWrappers.forEach(group -> group.syncUp(dataBiped));
                    this.apply();
                    this.original.setModelAttributes((ArmorWrapper)(Object)this);
                    ArmorWrapper overlay = this.original instanceof ModelArmorInfinity ?
                            (ArmorWrapper)ArmorModelFactory.getArmorModel(((ModelArmorInfinityAccess)this.original)
                                    .getOverlay(),true) : null;
                    ArmorWrapper invulOverlay = this.original instanceof ModelArmorInfinity ?
                            (ArmorWrapper)ArmorModelFactory.getArmorModel(((ModelArmorInfinityAccess)this.original)
                                    .getInvulnOverlay(),true) : null;
                    if(Objects.nonNull(overlay))
                        ((IArmorWrapper)overlay).dimhoppertweaks$preRenderSplit(entity);
                    if(Objects.nonNull(invulOverlay))
                        ((IArmorWrapper)invulOverlay).dimhoppertweaks$preRenderSplit(entity);
                    this.original.render(entity,limbSwing,swingAmount,ageInTicks,headYaw,headPitch,scale);
                    this.deapply();
                    if(Objects.nonNull(overlay))
                        ((IArmorWrapper)overlay).dimhoppertweaks$postRenderSplit();
                    if(Objects.nonNull(invulOverlay))
                        ((IArmorWrapper)invulOverlay).dimhoppertweaks$postRenderSplit();
                }
            }
        }
    }

    public void dimhoppertweaks$preRenderSplit(Entity entity) {
        if(!this.mutated) throw new MalformedArmorModelException("Operating on a demutated armor wrapper.");
        else if(entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase)entity;
            EntityBender<EntityLivingBase> bender = EntityBenderRegistry.instance.getForEntity(living);
            if(Objects.nonNull(bender)) {
                EntityData<?> data = EntityDatabase.instance.get(living);
                if(data instanceof BipedEntityData) {
                    if(data instanceof PlayerData && PlayerPreviewer.isPreviewInProgress())
                        data = PlayerPreviewer.getPreviewData();
                    BipedEntityData<?> dataBiped = (BipedEntityData<?>)data;
                    this.bodyTransform.syncUp(dataBiped.body);
                    this.partWrappers.forEach(group -> group.syncUp(dataBiped));
                    this.apply();
                    this.original.setModelAttributes((ArmorWrapper)(Object)this);
                    this.original.isChild = living.isChild();
                }
            }
        }
    }

    public void dimhoppertweaks$postRenderSplit() {
        this.deapply();
    }
}