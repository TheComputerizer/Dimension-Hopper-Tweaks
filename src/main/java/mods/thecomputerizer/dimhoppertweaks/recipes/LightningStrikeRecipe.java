package mods.thecomputerizer.dimhoppertweaks.recipes;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.InvincibleEntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

import static java.lang.Integer.MAX_VALUE;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static net.minecraft.entity.EntityList.ENTITY_EGGS;
import static net.minecraft.init.Items.AIR;
import static net.minecraft.init.Items.SPAWN_EGG;
import static net.minecraft.item.ItemStack.EMPTY;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES;

public class LightningStrikeRecipe {

    private static final List<LightningStrikeRecipe> RECIPES = new ArrayList<>();
    private static double maxRange = 1d;

    public static void checkLightningStrike(EntityLightningBolt bolt, Entity entity) {
        if(RECIPES.isEmpty() || !(entity instanceof EntityItem)) return ;
        World world = bolt.getEntityWorld();
        Vec3d strikePos = bolt.getPositionVector();
        List<Entity> entities = null;
        for(LightningStrikeRecipe recipe : RECIPES) {
            EntityItem eItem = (EntityItem)entity;
            if(recipe.checkCatalyst(eItem,strikePos)) {
                if(Objects.isNull(entities)) {
                    entities = world.getEntitiesWithinAABB(Entity.class,new AxisAlignedBB(
                            strikePos.subtract(maxRange,maxRange,maxRange),strikePos.add(maxRange,maxRange,maxRange)));
                    entities.removeIf(item -> item==entity);
                }
                recipe.verifyInputs(world,strikePos,entities);
            }
        }
    }

    public static List<LightningStrikeRecipe> getRecipes() {
        return Collections.unmodifiableList(RECIPES);
    }

    @Getter private final int dimension;
    @Getter private final double range;
    private final ItemRef catalyst;
    private final EntityRef entity;
    private final List<ItemRef> inputs;
    private final List<ItemRef> outputs;

    private LightningStrikeRecipe(int dimension, double range, ItemRef catalyst, EntityRef entity,
            List<ItemRef> inputs, List<ItemRef> outputs) {
        this.dimension = dimension;
        this.range = range;
        this.catalyst = catalyst;
        this.entity = entity;
        this.inputs = inputs;
        this.outputs = outputs;
        if(isValid()) {
            RECIPES.add(this);
            if(this.range>maxRange) maxRange = this.range;
        }
    }

    public boolean checkCatalyst(EntityItem item, Vec3d strikePos) {
        return strikePos.distanceTo(item.getPositionVector())<=this.range && this.catalyst.matches(item.getItem());
    }
    
    public boolean checkEntity(Entity entity, Vec3d strikePos) {
        return Objects.isNull(this.entity) ||
               (strikePos.distanceTo(entity.getPositionVector())<=this.range && this.entity.checkMatch(entity));
    }
    
    public ItemStack getCatalystStack() {
        return this.catalyst.toStack();
    }
    
    public @Nullable Entity getEntity(World world, double x, double y, double z) {
        ResourceLocation id = getEntityID();
        EntityEntry entry = Objects.nonNull(id) && ENTITIES.containsKey(id) ? ENTITIES.getValue(id) : null;
        if(Objects.isNull(entry)) return null;
        LOGGER.info("Got EntityEntry for lightning strike recipe from {}",id);
        Entity entity = entry.newInstance(world);
        if(Objects.isNull(entity)) {
            LOGGER.error("Entity for lightning strike recipe ({}) initialized as null??",id);
            return null;
        }
        if(x!=0d || y!=0d || z!=0d) {
            entity.setLocationAndAngles(x,y,z,MathHelper.wrapDegrees(world.rand.nextFloat()*360f),0f);
            if(entity instanceof EntityLivingBase) {
                EntityLivingBase based = (EntityLivingBase)entity;
                based.rotationYawHead = entity.rotationYaw;
                based.renderYawOffset = entity.rotationYaw;
                if(based instanceof EntityLiving)
                    ((EntityLiving)based).onInitialSpawn(world.getDifficultyForLocation(based.getPosition()),null);
            }
        }
        return entity;
    }
    
    public @Nullable ResourceLocation getEntityID() {
        return Objects.nonNull(this.entity) ? this.entity.id : null;
    }
    
    public @Nonnull ItemStack getEntitySpawnEgg() {
        ResourceLocation id = getEntityID();
        if(Objects.nonNull(id) && ENTITY_EGGS.containsKey(id)) {
            ItemStack stack = new ItemStack(SPAWN_EGG);
            ItemMonsterPlacer.applyEntityIdToItemStack(stack,id);
            return stack;
        }
        return EMPTY;
    }

    public List<ItemStack> getInputStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for(ItemRef input : this.inputs) stacks.add(input.toStack());
        return stacks;
    }

    public List<ItemStack> getOutputStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for(ItemRef output : this.outputs) stacks.add(output.toStack());
        return stacks;
    }

    private boolean inputMatches(ItemRef input, Vec3d strikePos, Collection<Entity> entities) {
        for(Entity entity : entities) {
            if(entity instanceof EntityItem && strikePos.distanceTo(entity.getPositionVector())<=this.range &&
               input.matches(((EntityItem)entity).getItem()))
                return true;
        }
        return false;
    }

    public boolean isValid() {
        if(this.range<=0d) {
            LOGGER.error("Range `{}` for lightning strike recipe cannot be set to 0 or less! Recipe will be ignored.",this.range);
            return false;
        }
        if(this.inputs.isEmpty()) {
            LOGGER.error("Lightning strike recipe must have at least 1 valid input! Recipe will be ignored.");
            return false;
        }
        for(ItemRef input : this.inputs) {
            if(input.invalid()) {
                LOGGER.error("Failed to validate input for lightning strike recipe! Recipe will be ignored.");
                return false;
            }
        }
        if(this.outputs.isEmpty()) {
            LOGGER.error("Lightning strike recipe must have at least 1 valid output! Recipe will be ignored.");
            return false;
        }
        for(ItemRef output : this.outputs) {
            if(output.invalid()) {
                LOGGER.error("Failed to validate output for lightning strike recipe! Recipe will be ignored.");
                return false;
            }
        }
        if(Objects.nonNull(this.entity) && this.entity.invalid()) {
            LOGGER.error("Failed to validate entity for lightning strike recipe! Recipe will be ignored.");
            return false;
        }
        return true;
    }

    private void spawnOutputs(World world, Vec3d strikePos) {
        if(!world.isRemote)
            for(ItemRef output : this.outputs)
                output.spawn(world,strikePos);
    }

    public void verifyInputs(World world, Vec3d strikePos, List<Entity> entities) {
        if(this.dimension!=MAX_VALUE && this.dimension!=world.provider.getDimension()) return;
        for(ItemRef input : this.inputs)
            if(!inputMatches(input,strikePos,entities)) return;
        boolean matchedEntity = Objects.isNull(this.entity);
        for(Entity entity : entities) {
            matchedEntity = matchedEntity || checkEntity(entity,strikePos);
            if(matchedEntity) break;
        }
        if(matchedEntity) spawnOutputs(world,strikePos);
    }

    private static class EntityRef {
        
        private final ResourceLocation id;
        
        private EntityRef(ResourceLocation id) {
            this.id = id;
        }
        
        boolean checkMatch(Entity entity) {
            if(Objects.nonNull(entity)) {
                ResourceLocation entityID = EntityList.getKey(entity);
                if(Objects.nonNull(entityID) && entityID.equals(this.id)) {
                    entity.setDead();
                    return true;
                }
            }
            return false;
        }
        
        private boolean invalid() {
            if(Objects.isNull(this.id)) {
                LOGGER.error("ID in entity reference cannot be null!");
                return true;
            }
            return false;
        }
    }

    private static class ItemRef {

        private final Item item;
        private final int meta;
        private final int count;

        private ItemRef(ItemStack stack) {
            this(stack.getItem(),stack.getMetadata(),stack.getCount());
        }

        private ItemRef(Item item, int meta, int count) {
            this.item = item;
            this.meta = meta;
            this.count = count;
        }

        private boolean invalid() {
            if(Objects.isNull(this.item)) {
                LOGGER.error("Item in item reference cannot be null!");
                return true;
            }
            if(this.item==AIR) {
                LOGGER.error("Item in item reference cannot be set to air!");
                return true;
            }
            if(this.meta<0) {
                LOGGER.error("Meta value `{}` in item reference cannot be less than 0!",this.meta);
                return true;
            }
            if(this.meta>Short.MAX_VALUE) {
                LOGGER.error("Meta value `{}` in item reference cannot be greater than {}!",this.meta,Short.MAX_VALUE);
                return true;
            }
            if(this.count<0) {
                LOGGER.error("Count value `{}` in item reference cannot be less than 0!",this.count);
                return true;
            }
            return false;
        }

        private boolean matches(ItemStack stack) {
            return stack.getItem()==this.item &&
                    (this.meta==Short.MAX_VALUE || stack.getMetadata()==this.meta) &&
                    (stack.getCount()>=this.count);
        }

        private void spawn(World world, Vec3d pos) {
            EntityItem entity = new InvincibleEntityItem(world,pos.x,pos.y,pos.z,toStack());
            entity.setNoPickupDelay();
            entity.setNoDespawn();
            world.spawnEntity(entity);
        }

        public ItemStack toStack() {
            return new ItemStack(this.item,this.count,this.meta);
        }
    }

    @SuppressWarnings("unused")
    public static class Builder {

        private final Map<Integer,Supplier<?>> catalystSuppliers;
        private final List<Map<Integer,Supplier<?>>> inputSuppliers;
        private final List<Map<Integer,Supplier<?>>> outputSuppliers;
        private Supplier<ResourceLocation> entityIDSupplier;
        private int dimension;
        private double range;

        public Builder() {
            this.catalystSuppliers = new Int2ObjectOpenHashMap<>();
            this.inputSuppliers = new ArrayList<>();
            this.outputSuppliers = new ArrayList<>();
            this.range = 3d;
        }

        public Builder addInput(Supplier<ItemStack> stack) {
            this.inputSuppliers.add(addItem(stack));
            return this;
        }

        public Builder addInput(Supplier<Item> item, Supplier<Integer> meta, Supplier<Integer> count) {
            this.inputSuppliers.add(addItem(item,meta,count));
            return this;
        }
        
        public Builder addEntity(Supplier<ResourceLocation> id) {
            this.entityIDSupplier = id;
            return this;
        }

        private Map<Integer,Supplier<?>> addItem(Supplier<ItemStack> stack) {
            Map<Integer,Supplier<?>> builder = new Int2ObjectOpenHashMap<>();
            builder.put(3,stack);
            return builder;
        }

        private Map<Integer,Supplier<?>> addItem(Supplier<Item> item, Supplier<Integer> meta, Supplier<Integer> count) {
            Map<Integer,Supplier<?>> builder = new Int2ObjectOpenHashMap<>();
            builder.put(0,item);
            builder.put(1,meta);
            builder.put(2,count);
            return builder;
        }

        public Builder addOutput(Supplier<ItemStack> stack) {
            this.outputSuppliers.add(addItem(stack));
            return this;
        }

        public Builder addOutput(Supplier<Item> item, Supplier<Integer> meta, Supplier<Integer> count) {
            this.outputSuppliers.add(addItem(item,meta,count));
            return this;
        }

        public Builder setAnyDimension() {
            this.dimension = MAX_VALUE;
            return this;
        }

        public Builder setCatalyst(Supplier<ItemStack> stack) {
            this.catalystSuppliers.put(3,stack);
            return this;
        }

        public Builder setCatalyst(Supplier<Item> item, Supplier<Integer> meta, Supplier<Integer> count) {
            this.catalystSuppliers.put(0,item);
            this.catalystSuppliers.put(1,meta);
            this.catalystSuppliers.put(2,count);
            return this;
        }

        public Builder setDimension(int dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder setRange(double range) {
            this.range = range;
            return this;
        }

        public void build() {
            EntityRef entity = Objects.nonNull(this.entityIDSupplier) ?
                    new EntityRef(this.entityIDSupplier.get()) : null;
            new LightningStrikeRecipe(this.dimension,this.range,buildItemRef(this.catalystSuppliers),entity,
                    buildItemRefs(this.inputSuppliers),buildItemRefs(this.outputSuppliers));
        }

        private List<ItemRef> buildItemRefs(List<Map<Integer,Supplier<?>>> builders) {
            List<ItemRef> list = new ArrayList<>();
            for(Map<Integer,Supplier<?>> builder : builders) list.add(buildItemRef(builder));
            return list;
        }

        private ItemRef buildItemRef(Map<Integer,Supplier<?>> builder) {
            if(builder.containsKey(3)) {
                Object supplied = builder.get(3).get();
                if(supplied instanceof ItemStack) return new ItemRef((ItemStack)supplied);
            }
            Item item = supplyItem(builder.get(0));
            int meta = supplyInt(builder.get(1));
            int count = supplyInt(builder.get(2));
            return new ItemRef(item,meta,count);
        }

        private int supplyInt(@Nullable Supplier<?> supplier) {
            Object supplied = Objects.nonNull(supplier) ? supplier.get() : null;
            return supplied instanceof Number ? ((Number)supplied).intValue() : 0;
        }

        private Item supplyItem(@Nullable Supplier<?> supplier) {
            Object supplied = Objects.nonNull(supplier) ? supplier.get() : null;
            return supplied instanceof Item ? (Item)supplied : AIR;
        }
    }
}
