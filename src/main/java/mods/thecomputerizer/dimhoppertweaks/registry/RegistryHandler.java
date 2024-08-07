package mods.thecomputerizer.dimhoppertweaks.registry;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import mods.thecomputerizer.dimhoppertweaks.common.commands.DHDebugCommands;
import mods.thecomputerizer.dimhoppertweaks.common.commands.RandomTP;
import mods.thecomputerizer.dimhoppertweaks.common.commands.SmiteStick;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.recipes.LightningStrikeRecipe;
import mods.thecomputerizer.dimhoppertweaks.recipes.LightningStrikeRecipe.Builder;
import mods.thecomputerizer.dimhoppertweaks.registry.structures.AbstractStructure;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.LightningEnhancerEntity;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.tslat.aoa3.item.misc.Realmstone;

import javax.annotation.Nonnull;

import static biomesoplenty.api.item.BOPItems.blue_dye;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry.STARGATE_ADDRESSER;
import static net.minecraft.init.Items.DYE;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;
import static net.tslat.aoa3.common.registration.ItemRegister.*;
import static vazkii.botania.common.block.ModBlocks.cellBlock;

@EventBusSubscriber(modid = MODID)
public final class RegistryHandler {

    public static final CreativeTabs DIM_HOPPER_TAB = new CreativeTabs(MODID) {
        @SideOnly(CLIENT)
        public @Nonnull ItemStack createIcon() {
            return new ItemStack(STARGATE_ADDRESSER);
        }
    };

    @SuppressWarnings("unused")
    @SideOnly(CLIENT)
    public static void onPostInit(FMLPostInitializationEvent event) {
        ParticleRegistry.postInit();
    }

    public static void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("Registering commands");
        event.registerServerCommand(new DHDebugCommands());
        event.registerServerCommand(new RandomTP());
        event.registerServerCommand(new SmiteStick());
    }

    public static void addLightningStrikeRecipes() {
        new Builder().setCatalyst(() -> new ItemStack(cellBlock))
                .addInput(() -> new ItemStack(blue_dye)).addInput(DelayedModAccess::cheese)
                .addOutput(() -> DYE,() -> 2,() -> 1).setRange(5d).setDimension(7).build();
        for(Realmstone realmstone : new Realmstone[]{ABYSS_REALMSTONE,NETHER_REALMSTONE,ANCIENT_CAVERN_REALMSTONE,
                BARATHOS_REALMSTONE,BOREAN_REALMSTONE,CANDYLAND_REALMSTONE,CELEVE_REALMSTONE,CREEPONIA_REALMSTONE,
                CRYSTEVIA_REALMSTONE,DEEPLANDS_REALMSTONE,DUSTOPIA_REALMSTONE,GARDENCIA_REALMSTONE,GRECKON_REALMSTONE,
                HAVEN_REALMSTONE,IMMORTALLIS_REALMSTONE,IROMINE_REALMSTONE,LELYETIA_REALMSTONE,LUNALUS_REALMSTONE,
                MYSTERIUM_REALMSTONE,PRECASIA_REALMSTONE,RUNANDOR_REALMSTONE,SHYRELANDS_REALMSTONE,VOX_PONDS_REALMSTONE})
            new Builder().setCatalyst(() -> new ItemStack(realmstone)).addInput(DelayedModAccess::cheese)
                            .addOutput(() -> new ItemStack(BLANK_REALMSTONE)).setRange(5d).setDimension(-6).build();
        addFunnySolarPanelRecipe(1,"lonsdaleite");
        addFunnySolarPanelRecipe(2,"litherite");
        addFunnySolarPanelRecipe(3,"erodium");
        addFunnySolarPanelRecipe(4,"kyronite");
        addFunnySolarPanelRecipe(5,"pladium");
        addFunnySolarPanelRecipe(6,"ionite");
        addFunnySolarPanelRecipe(7,"aethium");
        LOGGER.info("Registered {} lightning strike recipe(s)",LightningStrikeRecipe.getRecipes().size());
    }
    
    @SuppressWarnings("DataFlowIssue")
    private static void addFunnySolarPanelRecipe(int solarTier, String crystalName) {
        ResourceLocation solarRes = new ResourceLocation("solarflux","solar_panel_"+solarTier);
        ResourceLocation crystalRes = new ResourceLocation("environmentaltech",crystalName+"_tiles");
        ResourceLocation catalyst = solarTier<=3 ?
                new ResourceLocation("xreliquary","witherless_rose") :
                (solarTier<=6 ? new ResourceLocation("overloaded","nether_star_block")
                        : new ResourceLocation("draconicevolution","energy_storage_core"));
        int dimension = solarTier<=3 ? 20 : (solarTier<=6 ? -6 : -2544);
        new Builder().setCatalyst(() -> new ItemStack(ITEMS.getValue(catalyst)))
                .addInput(() -> new ItemStack(ITEMS.getValue(crystalRes)))
                .addOutput(() -> new ItemStack(ITEMS.getValue(solarRes)))
                .setRange(5d).setDimension(dimension).build();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        register(event,BlockRegistry.getBlocks());
        GameRegistry.registerTileEntity(LightningEnhancerEntity.class,DHTRef.res("tile.lightning_enhancer"));
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
