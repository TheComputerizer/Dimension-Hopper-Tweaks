package mods.thecomputerizer.dimhoppertweaks.registry;

import biomesoplenty.api.item.BOPItems;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import mods.thecomputerizer.dimhoppertweaks.common.commands.DHDebugCommands;
import mods.thecomputerizer.dimhoppertweaks.common.commands.RandomTP;
import mods.thecomputerizer.dimhoppertweaks.common.commands.SmiteStick;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.recipes.LightningStrikeRecipe;
import mods.thecomputerizer.dimhoppertweaks.registry.structures.AbstractStructure;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.LightningEnhancerEntity;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = DHTRef.MODID)
public final class RegistryHandler {

    public static final CreativeTabs DIM_HOPPER_TAB = new CreativeTabs(DHTRef.MODID) {
        @SideOnly(Side.CLIENT)
        public @Nonnull ItemStack createIcon() {
            return new ItemStack(ItemRegistry.STARGATE_ADDRESSER);
        }
    };

    @SuppressWarnings("unused")
    @SideOnly(Side.CLIENT)
    public static void onPostInit(FMLPostInitializationEvent event) {
        ParticleRegistry.postInit();
    }

    public static void onServerStarting(FMLServerStartingEvent event) {
        DHTRef.LOGGER.info("Registering commands");
        event.registerServerCommand(new DHDebugCommands());
        event.registerServerCommand(new RandomTP());
        event.registerServerCommand(new SmiteStick());
    }

    public static void addLightningStrikeRecipes() {
        new LightningStrikeRecipe.Builder().setCatalyst(() -> new ItemStack(ModBlocks.cellBlock))
                .addInput(() -> new ItemStack(BOPItems.blue_dye)).addInput(DelayedModAccess::cheese)
                .addOutput(() -> Items.DYE,() -> 2,() -> 1).setRange(5d).setDimension(7).build();
        DHTRef.LOGGER.info("Registered {} lightning strike recipe(s)",LightningStrikeRecipe.getRecipes().size());
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        register(event,BlockRegistry.getBlocks());
        GameRegistry.registerTileEntity(LightningEnhancerEntity.class, DHTRef.res("tile.lightning_enhancer"));
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        register(event,EntityRegistry.getEntityEntries());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        register(event,ItemRegistry.getItems());
    }

    @SubscribeEvent
    public static void updateMappings(RegistryEvent.MissingMappings<Item> event) {
        //register(event,ItemRegistry.getItems());
    }

    @SubscribeEvent
    public static void registerSkills(RegistryEvent.Register<Skill> event) {
        register(event,SkillRegistry.getSkills());
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        register(event,SoundRegistry.getSounds());
    }

    @SubscribeEvent
    public static void registerStructures(RegistryEvent.Register<AbstractStructure> event) {
        register(event,StructureRegistry.getStructures());
    }

    @SubscribeEvent
    public static void registerTraits(RegistryEvent.Register<Unlockable> event) {
        register(event,TraitRegistry.getTraits());
    }

    @SafeVarargs
    private static <E extends IForgeRegistryEntry<E>> void register(RegistryEvent.Register<E> event, E ... toRegister) {
        event.getRegistry().registerAll(toRegister);
    }
}
