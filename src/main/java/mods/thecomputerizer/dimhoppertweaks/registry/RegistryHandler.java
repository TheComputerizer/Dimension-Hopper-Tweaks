package mods.thecomputerizer.dimhoppertweaks.registry;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import mods.thecomputerizer.dimhoppertweaks.common.commands.DHDebugCommands;
import mods.thecomputerizer.dimhoppertweaks.common.commands.RandomTP;
import mods.thecomputerizer.dimhoppertweaks.common.commands.SummonBoss;
import mods.thecomputerizer.dimhoppertweaks.common.skills.attack.SuperPets;
import mods.thecomputerizer.dimhoppertweaks.common.skills.building.ResistiveBuilder;
import mods.thecomputerizer.dimhoppertweaks.common.skills.defense.KnockbackImmunity;
import mods.thecomputerizer.dimhoppertweaks.common.skills.gathering.ExplosiveAura;
import mods.thecomputerizer.dimhoppertweaks.common.skills.gathering.LuckyAura;
import mods.thecomputerizer.dimhoppertweaks.common.skills.mining.ExpertMiner;
import mods.thecomputerizer.dimhoppertweaks.common.skills.research.ResearchSkill;
import mods.thecomputerizer.dimhoppertweaks.common.skills.research.TieredResearchTrait;
import mods.thecomputerizer.dimhoppertweaks.common.skills.voidskill.RefreshingPortals;
import mods.thecomputerizer.dimhoppertweaks.common.skills.voidskill.VoidCheater;
import mods.thecomputerizer.dimhoppertweaks.common.skills.voidskill.VoidSkill;
import mods.thecomputerizer.dimhoppertweaks.common.skills.voidskill.VoidWalker;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.LightningEnhancerEntity;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

import static mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry.STARGATE_ADDRESSER;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public final class RegistryHandler {
    public static final CreativeTabs DIM_HOPPER_TAB = new CreativeTabs(Constants.MODID) {
        @SideOnly(Side.CLIENT)
        @Nonnull
        public ItemStack createIcon() {
            return new ItemStack(STARGATE_ADDRESSER);
        }
    };

    @SuppressWarnings("unused")
    @SideOnly(Side.CLIENT)
    public static void onPostInit(FMLPostInitializationEvent event) {
        ParticleRegistry.postInit();
    }

    public static void onServerStarting(FMLServerStartingEvent event) {
        Constants.LOGGER.info("Registering commands");
        event.registerServerCommand(new DHDebugCommands());
        event.registerServerCommand(new RandomTP());
        event.registerServerCommand(new SummonBoss());
    }
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        register(event.getRegistry(),BlockRegistry.getBlocks());
        GameRegistry.registerTileEntity(LightningEnhancerEntity.class, Constants.res("tile.lightning_enhancer"));
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> entities) {
        EntityRegistry.register();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        register(event.getRegistry(),ItemRegistry.getItems());
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        register(event.getRegistry(),SoundRegistry.getSounds());
    }

    @SubscribeEvent
    public static void registerSkills(RegistryEvent.Register<Skill> event) {
        register(event.getRegistry(),new ResearchSkill(),new VoidSkill());
    }

    @SubscribeEvent
    public static void registerTraits(RegistryEvent.Register<Unlockable> event) {
        register(event.getRegistry(),new SuperPets(),new ResistiveBuilder(),new KnockbackImmunity(),new ExplosiveAura(),
                new LuckyAura(),new ExpertMiner(),new RefreshingPortals(),new VoidCheater(),new VoidWalker(),
                new TieredResearchTrait("oil",1,0),new TieredResearchTrait("oil",2,0),
                new TieredResearchTrait("oil",3,0),new TieredResearchTrait("factory",1,1),
                new TieredResearchTrait("factory",2,1),new TieredResearchTrait("factory",3,1),
                new TieredResearchTrait("psionic",1,2),new TieredResearchTrait("psionic",2,2),
                new TieredResearchTrait("psionic",3,2));
    }

    @SafeVarargs
    public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> registry, T ... entries) {
        registry.registerAll(entries);
    }
}
