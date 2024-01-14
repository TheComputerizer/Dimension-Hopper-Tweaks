package mods.thecomputerizer.dimhoppertweaks.client.model;

import c4.conarm.client.models.ModelConstructsArmor;
import goblinbob.mobends.core.bender.EntityBender;
import goblinbob.mobends.core.bender.EntityBenderRegistry;
import goblinbob.mobends.core.client.model.ModelPartTransform;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.standard.client.model.armor.*;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.data.PlayerData;
import goblinbob.mobends.standard.previewer.PlayerPreviewer;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.HumanoidPartAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.ModelConstructsArmorAccess;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class ModelConstructsArmorWrapper extends ModelBiped {

    private static final Map<EntityEquipmentSlot,Map<ModelConstructsArmor,ModelConstructsArmorWrapper>> INSTANCE_MAP = new EnumMap<>(EntityEquipmentSlot.class);
    private static final IPartWrapper.ModelPartSetter headSetter = (model,part) -> model.bipedHead = part;
    private static final IPartWrapper.ModelPartSetter bodySetter = (model,part) -> model.bipedBody = part;
    private static final IPartWrapper.ModelPartSetter leftArmSetter = (model,part) -> model.bipedLeftArm = part;
    private static final IPartWrapper.ModelPartSetter rightArmSetter = (model,part) -> model.bipedRightArm = part;
    private static final IPartWrapper.ModelPartSetter leftLegSetter = (model,part) -> model.bipedLeftLeg = part;
    private static final IPartWrapper.ModelPartSetter rightLegSetter = (model,part) -> model.bipedRightLeg = part;

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

    private final Map<String,Supplier<ModelRenderer>> partSuppliers;
    private final List<String> visibleParts;
    private final List<IPartWrapper> wrappers;
    protected ModelConstructsArmor original;
    protected EntityEquipmentSlot slot;
    protected boolean mutated = true;
    protected boolean isActive = false;
    protected ModelPartTransform bodyTransform;

    private ModelConstructsArmorWrapper(ModelConstructsArmor original, EntityEquipmentSlot slot) {
        this.original = original;
        this.slot = slot;
        this.partSuppliers = new HashMap<>();
        this.bipedHead = initPart("head",original.headAnchor,() -> this.bipedHead);
        this.bipedHeadwear = initPart("headwear",original.bipedHeadwear,() -> this.bipedHeadwear);
        this.bipedBody = initPart("body",slot==EntityEquipmentSlot.LEGS ? original.pantsAnchor :
                original.chestAnchor,() -> this.bipedBody);
        this.bipedLeftArm = initPart("leftarm",original.armLeftAnchor,() -> this.bipedLeftArm);
        this.bipedRightArm = initPart("rightarm",original.armRightAnchor,() -> this.bipedRightArm);
        this.bipedLeftLeg = initPart("leftleg",slot==EntityEquipmentSlot.FEET ? original.bootLeftAnchor :
                original.legLeftAnchor,() -> this.bipedLeftLeg);
        this.bipedRightLeg = initPart("rightleg",slot==EntityEquipmentSlot.FEET ? original.bootRightAnchor :
                original.legRightAnchor,() -> this.bipedRightLeg);
        this.bodyTransform = new ModelPartTransform();
        this.visibleParts = new ArrayList<>();
        this.wrappers = new ArrayList<>();
        registerSlotBasedWrappers();
    }

    private ModelRenderer initPart(String name, ModelRenderer ref, Supplier<ModelRenderer> supplier) {
        this.partSuppliers.put(name,supplier);
        return ref;
    }

    private void registerSlotBasedWrappers() {
        boolean isHead = this.slot==EntityEquipmentSlot.HEAD;
        boolean isChest = this.slot==EntityEquipmentSlot.CHEST;
        boolean isLegs = this.slot==EntityEquipmentSlot.LEGS;
        boolean isFeet = this.slot==EntityEquipmentSlot.FEET;
        if(isHead)
            this.registerWrapper(this.original,this.bipedHead,headSetter,data -> data.head,"head")
                    .setParent(this.bodyTransform);
        if(isChest || isLegs)
            this.registerWrapper(this.original,this.bipedBody,bodySetter,data -> data.body,"body")
                    .offsetInner(0f,-12f,0f);
        if(isChest) {
            this.registerWrapper(this.original,this.bipedLeftArm,leftArmSetter,data -> data.leftArm,
                            data -> data.leftForeArm,4f,0.001f,"leftarm")
                    .offsetLower(0f,-4f,-2f).setParent(this.bodyTransform);
            this.registerWrapper(this.original,this.bipedRightArm, rightArmSetter,data -> data.rightArm,
                            data -> data.rightForeArm,4f,0.001f,"rightarm")
                    .offsetLower(0f,-4f,-2f).setParent(this.bodyTransform);
        }
        if(isLegs || isFeet) {
            this.registerWrapper(this.original,this.bipedLeftLeg,leftLegSetter,data -> data.leftLeg,
                            data -> data.leftForeLeg,6f,0f,"leftleg")
                    .offsetLower(0.75f,-6f,2f).offsetInner(0.75f,0f,0f);
            this.registerWrapper(this.original,this.bipedRightLeg,rightLegSetter,data -> data.rightLeg,
                            data -> data.rightForeLeg,6f,0f,"rightleg")
                    .offsetLower(-0.75f,-6f,2f).offsetInner(-0.75f,0f,0f);
        }
    }

    private HumanoidPartWrapper registerWrapper(
            ModelBiped model, ModelRenderer part, IPartWrapper.ModelPartSetter setter,
            IPartWrapper.DataPartSelector data, String partName) {
        HumanoidPartWrapper wrapper = new HumanoidPartWrapper(model,part,setter,data);
        this.visibleParts.add(partName);
        this.wrappers.add(wrapper);
        return wrapper;
    }

    private HumanoidLimbWrapper registerWrapper(
            ModelBiped model, ModelRenderer part, IPartWrapper.ModelPartSetter setter,
            IPartWrapper.DataPartSelector data, IPartWrapper.DataPartSelector lowerData, float cutPlane,
            float inflation, String partName) {
        HumanoidLimbWrapper wrapper = new HumanoidLimbWrapper(model,part,setter,data,lowerData,cutPlane,inflation);
        this.visibleParts.add(partName);
        this.wrappers.add(wrapper);
        return wrapper;
    }

    public void render(Entity entity, float limbSwing, float swingAmount, float ageInTicks, float headYaw,
                       float headPitch, float scale) {
        if(!this.mutated) throw new MalformedArmorModelException("Operating on a demutated armor wrapper.");
        else if(entity instanceof EntityLivingBase) {
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
                    setParts();
                    this.original.setModelAttributes(this);
                    ((ModelConstructsArmorAccess)this.original).dimhoppertweaks$render(
                            entity,limbSwing,swingAmount,ageInTicks,headYaw,headPitch,scale);
                    this.deapply();
                }
            }
        }
    }

    private void setParts() {
        for(ModelRenderer part : this.original.boxList) part.showModel = false;
        for(Map.Entry<String,Supplier<ModelRenderer>> entry : this.partSuppliers.entrySet())
            if(this.visibleParts.contains(entry.getKey())) showChildren(entry.getValue().get());
    }

    private void showChildren(ModelRenderer parent) {
        parent.showModel = true;
        if(Objects.nonNull(parent.childModels))
            for(ModelRenderer child : parent.childModels)
                showChildren(child);
        if(parent instanceof PartContainer) showChildren(((PartContainer)parent).getModel());
    }

    @Override
    public void setRotationAngles(float limbSwing, float swingAmount, float ageInTicks, float headYaw, float headPitch,
                                  float scale, Entity entity) {}

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
