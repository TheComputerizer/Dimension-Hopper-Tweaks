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
import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
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
        addIronTerminalRecipe();
        addTerminalRecipe(0,"moreplates:electrical_steel_plate","immersiveengineering:material",2);
        addTerminalRecipe(1,"thermalfoundation:material",323,"moreplates:lead_stick");
        addTerminalRecipe(2,"thermalfoundation:material",321,"moreplates:tin_stick");
        addTerminalRecipe(3,"thermalfoundation:material",324,"lightningcraft:rod",4);
        addTerminalRecipe(4,"moreplates:enriched_gold_plate","silentgems:craftingmaterial",8);
        addTerminalRecipe(5,"thermalfoundation:material",320,"moreplates:copper_stick");
        addTerminalRecipe(6,"lightningcraft:plate",7,"lightningcraft:rod",7);
        addTerminalRecipe(7,"lightningcraft:plate",8,"lightningcraft:rod",8);
        addTerminalRecipe(8,"lightningcraft:plate",9,"lightningcraft:rod",9);
        addTerminalRecipe(9,"moreplates:awakened_draconium_plate","minecraft:dragon_egg");
        addTerminalRecipe(10,"moreplates:insanium_plate","zollerngalaxy:perddiamondblock");
        addTerminalRecipe(11,"extendedcrafting:material",19,"mysticalagradditions:dragon_egg_essence");
        addTerminalRecipe(12,"moreplates:infinity_plate","projectex:final_star_shard");
        
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
    
    private static void addIronTerminalRecipe() {
        ResourceLocation terminalRes = new ResourceLocation("lightningcraft","air_terminal");
        new Builder().setCatalyst(() -> ItemUtil.getStack("calculator:conductormast"))
                .addInput(() -> ItemUtil.getItem("moreplates:conductive_iron_plate"),() -> 0,() -> 5)
                .addInput(() -> ItemUtil.getItem("extendedcrafting:material"),() -> 3,() -> 1)
                .addOutput(() -> ItemUtil.getStack(terminalRes,0))
                .setRange(5d).setDimension(0).build();
    }
    
    private static void addTerminalRecipe(int metaIn, String plate, String extra) {
        addTerminalRecipe(metaIn,plate,0,extra,0);
    }
    
    private static void addTerminalRecipe(int metaIn, String plate, String extra, int extraMeta) {
        addTerminalRecipe(metaIn,plate,0,extra,extraMeta);
    }
    
    private static void addTerminalRecipe(int metaIn, String plate, int plateMeta, String extra) {
        addTerminalRecipe(metaIn,plate,plateMeta,extra,0);
    }
    
    private static void addTerminalRecipe(int metaIn, String plate, int plateMeta, String extra, int extraMeta) {
        ResourceLocation terminalRes = new ResourceLocation("lightningcraft","air_terminal");
        new Builder().setCatalyst(() -> ItemUtil.getStack(terminalRes,metaIn))
                .addInput(() -> ItemUtil.getItem(plate),() -> plateMeta,() -> 5)
                .addInput(() -> ItemUtil.getItem(extra),() -> extraMeta,() -> 1)
                .addOutput(() -> ItemUtil.getStack(terminalRes,metaIn+1))
                .setRange(5d).setDimension(0).build();
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
