package mods.thecomputerizer.dimhoppertweaks.client.model;

import c4.conarm.client.models.ModelConstructsArmor;
import goblinbob.mobends.core.bender.EntityBender;
import goblinbob.mobends.core.bender.EntityBenderRegistry;
import goblinbob.mobends.core.client.model.ModelPartTransform;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.standard.client.model.armor.HumanoidLimbWrapper;
import goblinbob.mobends.standard.client.model.armor.HumanoidPartWrapper;
import goblinbob.mobends.standard.client.model.armor.IPartWrapper;
import goblinbob.mobends.standard.client.model.armor.MalformedArmorModelException;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.data.PlayerData;
import goblinbob.mobends.standard.previewer.PlayerPreviewer;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.HumanoidPartAccess;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class ModelConstructsArmorWrapper extends ModelBiped {

    private static final Map<EntityEquipmentSlot,Map<ModelConstructsArmor,ModelConstructsArmorWrapper>> INSTANCE_MAP = new EnumMap<>(EntityEquipmentSlot.class);

    public static ModelConstructsArmorWrapper getInstance(ModelConstructsArmor original, EntityEquipmentSlot slot) {
        Map<ModelConstructsArmor,ModelConstructsArmorWrapper> modelMap = INSTANCE_MAP.get(slot);
        if(Objects.isNull(modelMap)) {
            modelMap = new HashMap<>();
            INSTANCE_MAP.put(slot,modelMap);
        }
        ModelConstructsArmorWrapper instance = modelMap.get(original);
        if(Objects.isNull(instance)) {
            instance = new ModelConstructsArmorWrapper(original,slot);
            modelMap.put(original,instance);
        }
        return instance;
    }

    protected ModelConstructsArmor original;
    protected boolean mutated = true;
    protected EntityEquipmentSlot slot;
    protected boolean isActive = false;
    private final List<IPartWrapper> wrappers;
    protected ModelPartTransform bodyTransform;
    public ModelRenderer bipedPants;
    public ModelRenderer bipedLeftBoot;
    public ModelRenderer bipedRightBoot;
    private static final IPartWrapper.ModelPartSetter headSetter = (model,part) -> model.bipedHead = part;
    private static final IPartWrapper.ModelPartSetter headwearSetter = (model,part) -> model.bipedHeadwear = part;
    private static final IPartWrapper.ModelPartSetter bodySetter = (model,part) -> model.bipedBody = part;
    private static final IPartWrapper.ModelPartSetter leftArmSetter = (model,part) -> model.bipedLeftArm = part;
    private static final IPartWrapper.ModelPartSetter rightArmSetter = (model,part) -> model.bipedRightArm = part;
    private static final IPartWrapper.ModelPartSetter leftLegSetter = (model, part) -> model.bipedLeftLeg = part;
    private static final IPartWrapper.ModelPartSetter rightLegSetter = (model, part) -> model.bipedRightLeg = part;

    private ModelConstructsArmorWrapper(ModelConstructsArmor original, EntityEquipmentSlot slot) {
        this.original = original;
        this.slot = slot;
        this.bipedHead = original.headAnchor;
        this.bipedHeadwear = original.headAnchor;
        this.bipedBody = original.chestAnchor;
        this.bipedLeftArm = original.armLeftAnchor;
        this.bipedRightArm = original.armRightAnchor;
        this.bipedPants = original.pantsAnchor;
        this.bipedLeftLeg = original.legLeftAnchor;
        this.bipedRightLeg = original.legRightAnchor;
        this.bipedLeftBoot = original.bootLeftAnchor;
        this.bipedRightBoot= original.bootRightAnchor;
        this.bodyTransform = new ModelPartTransform();
        this.wrappers = new ArrayList<>();
        this.registerWrapper(original,this.bipedHead,headSetter,data -> data.head)
                .setParent(this.bodyTransform);
        this.registerWrapper(original,this.bipedHeadwear,headwearSetter,data -> data.head)
                .setParent(this.bodyTransform);
        this.registerWrapper(original,this.bipedLeftArm,leftArmSetter,data -> data.leftArm,data -> data.leftForeArm,
                4f,0.001f).offsetLower(0f,-4f,-2f).setParent(this.bodyTransform);
        this.registerWrapper(original,this.bipedRightArm, rightArmSetter,data -> data.rightArm, data -> data.rightForeArm,
                4f,0.001f).offsetLower(0f,-4f,-2f).setParent(this.bodyTransform);
        if(slot==EntityEquipmentSlot.LEGS) {
            this.registerWrapper(original, this.bipedPants, bodySetter, data -> data.body).offsetInner(0f,-12f,0f);
            this.registerWrapper(original,this.bipedLeftLeg,leftLegSetter,data -> data.leftLeg, data -> data.leftForeLeg,
                    6f,0f).offsetLower(0.75f,-6f,2f).offsetInner(0.75f,0f,0f);
            this.registerWrapper(original,this.bipedRightLeg,rightLegSetter,data -> data.rightLeg, data -> data.rightForeLeg,
                    6f,0f).offsetLower(-0.75f,-6f,2f).offsetInner(-0.75f,0f,0f);
        }
        else {
            this.registerWrapper(original,this.bipedBody,bodySetter,data -> data.body).offsetInner(0f,-12f,0f);
            this.registerWrapper(original,this.bipedLeftLeg,leftLegSetter,data -> data.leftLeg, data -> data.leftForeLeg,
                    6f,0f).offsetLower(0.75f,-6f,2f).offsetInner(0.75f,0f,0f);
            this.registerWrapper(original,this.bipedRightLeg,rightLegSetter,data -> data.rightLeg, data -> data.rightForeLeg,
                    6f,0f).offsetLower(-0.75f,-6f,2f).offsetInner(-0.75f,0f,0f);
        }
    }

    private HumanoidPartWrapper registerWrapper(
            ModelBiped vanillaModel, ModelRenderer vanillaPart, IPartWrapper.ModelPartSetter setter,
            IPartWrapper.DataPartSelector dataSelector) {
        HumanoidPartWrapper wrapper = new HumanoidPartWrapper(vanillaModel, vanillaPart, setter, dataSelector);
        this.wrappers.add(wrapper);
        return wrapper;
    }

    private HumanoidLimbWrapper registerWrapper(
            ModelBiped vanillaModel, ModelRenderer vanillaPart, IPartWrapper.ModelPartSetter setter,
            IPartWrapper.DataPartSelector data, IPartWrapper.DataPartSelector lowerData, float cutPlane,
            float inflation) {
        HumanoidLimbWrapper wrapper = new HumanoidLimbWrapper(vanillaModel,vanillaPart,setter,data,lowerData,cutPlane,inflation);
        this.wrappers.add(wrapper);
        return wrapper;
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(!this.mutated) throw new MalformedArmorModelException("Operating on a demutated armor wrapper.");
        else if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase)entity;
            EntityBender<EntityLivingBase> bender = EntityBenderRegistry.instance.getForEntity(entityLiving);
            if(Objects.nonNull(bender)) {
                EntityData<?> data = EntityDatabase.instance.get(entityLiving);
                if(data instanceof BipedEntityData) {
                    if(data instanceof PlayerData && PlayerPreviewer.isPreviewInProgress())
                        data = PlayerPreviewer.getPreviewData();
                    BipedEntityData<?> dataBiped = (BipedEntityData<?>)data;
                    this.bodyTransform.syncUp(dataBiped.body);
                    this.wrappers.forEach(group -> group.syncUp(dataBiped));
                    this.apply();
                    this.original.setModelAttributes(this);
                    setParts();
                    this.original.render(entity,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scale);
                    this.deapply();
                }
            }
        }
    }

    private void setParts() {
        this.bipedHead.showModel = this.slot == EntityEquipmentSlot.HEAD;
        this.bipedBody.showModel = this.slot == EntityEquipmentSlot.CHEST || this.slot == EntityEquipmentSlot.LEGS;
        this.bipedRightArm.showModel = this.slot == EntityEquipmentSlot.CHEST;
        this.bipedLeftArm.showModel = this.slot == EntityEquipmentSlot.CHEST;
        this.bipedLeftLeg.showModel = this.slot == EntityEquipmentSlot.LEGS || this.slot == EntityEquipmentSlot.FEET;
        this.bipedRightLeg.showModel = this.slot == EntityEquipmentSlot.LEGS || this.slot == EntityEquipmentSlot.FEET;
        this.bipedHeadwear.showModel = false;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                                  float headPitch, float scaleFactor, Entity entity) {}

    public void demutate() {
        this.deapply();
        this.wrappers.clear();
        this.mutated = false;
    }

    public void apply() {
        if(!this.isActive) {
            for(IPartWrapper wrapper : this.wrappers)
                ((HumanoidPartAccess)wrapper).dimhoppertweaks$apply(this);
            this.isActive = true;
        }
    }

    public void deapply() {
        if(this.isActive) {
            for(IPartWrapper wrapper : this.wrappers)
                ((HumanoidPartAccess)wrapper).dimhoppertweaks$deapply(this);
            this.isActive = false;
        }
    }

    public ModelConstructsArmor getOriginal() {
        return this.original;
    }
}
