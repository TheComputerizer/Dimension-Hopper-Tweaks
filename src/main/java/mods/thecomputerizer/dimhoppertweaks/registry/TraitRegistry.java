package mods.thecomputerizer.dimhoppertweaks.registry;

import codersafterdark.reskillable.api.unlockable.Trait;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.attack.NoCooldowns;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.attack.SuperPets;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.building.ResistiveBuilder;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.defense.KnockbackImmunity;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.farming.HungryFarmer;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.farming.PotionMaster;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.gathering.ExplosiveAura;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.gathering.LuckyAura;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.magic.LivingBattery;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.magic.NaturesAura;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.mining.ExpertMiner;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.research.LimbonicGenerator;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.research.TieredResearchTrait;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.research.TokenGamble;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.voidtraits.RefreshingPortals;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.voidtraits.VoidCheater;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.voidtraits.VoidWalker;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class TraitRegistry {

    private static final List<Trait> ALL_TRAITS = new ArrayList<>();
    public static final Trait EXPERT_MINER = makeTrait(new ExpertMiner());
    public static final Trait EXPLOSIVE_AURA = makeTrait(new ExplosiveAura());
    public static final Trait FACTORY_MK_ONE = makeTrait(new TieredResearchTrait("factory",1,0,1));
    public static final Trait FACTORY_MK_TWO = makeTrait(new TieredResearchTrait("factory",2,0,1));
    public static final Trait FACTORY_MK_THREE = makeTrait(new TieredResearchTrait("factory",3,0,1));
    public static final Trait HUNGRY_FARMER = makeTrait(new HungryFarmer());
    public static final Trait KNOCKBACK_IMMUNITY = makeTrait(new KnockbackImmunity());
    public static final Trait LIGHTNING_MK_TWO = makeTrait(new TieredResearchTrait("lightning",2,-1,3));
    public static final Trait LIGHTNING_MK_THREE = makeTrait(new TieredResearchTrait("lightning",3,-1,3));
    public static final Trait LIGHTNING_MK_FOUR = makeTrait(new TieredResearchTrait("lightning",4,-1,3));
    public static final Trait LIGHTNING_MK_FIVE = makeTrait(new TieredResearchTrait("lightning",5,-1,3));
    public static final Trait LIMBONIC_GENERATOR = makeTrait(new LimbonicGenerator());
    public static final Trait LIVING_BATTERY = makeTrait(new LivingBattery());
    public static final Trait LUCKY_AURA = makeTrait(new LuckyAura());
    public static final Trait NATURES_AURA = makeTrait(new NaturesAura());
    public static final Trait NO_COOLDOWNS = makeTrait(new NoCooldowns());
    public static final Trait OIL_MK_ONE = makeTrait(new TieredResearchTrait("oil",1,0,0));
    public static final Trait OIL_MK_TWO = makeTrait(new TieredResearchTrait("oil",2,0,0));
    public static final Trait OIL_MK_THREE = makeTrait(new TieredResearchTrait("oil",3,0,0));
    public static final Trait POTION_MASTER = makeTrait(new PotionMaster());
    public static final Trait PSIONIC_MK_ONE = makeTrait(new TieredResearchTrait("psionic",1,0,2));
    public static final Trait PSIONIC_MK_TWO = makeTrait(new TieredResearchTrait("psionic",2,0,2));
    public static final Trait PSIONIC_MK_THREE = makeTrait(new TieredResearchTrait("psionic",3,0,2));
    public static final Trait REFRESHING_PORTALS = makeTrait(new RefreshingPortals());
    public static final Trait RESISTIVE_BUILDER = makeTrait(new ResistiveBuilder());
    public static final Trait SUPER_PETS = makeTrait(new SuperPets());
    public static final Trait TOKEN_GAMBLE = makeTrait(new TokenGamble());
    public static final Trait VOID_CHEATER = makeTrait(new VoidCheater());
    public static final Trait VOID_WALKER = makeTrait(new VoidWalker());

    private static <S extends Trait> S makeTrait(S trait) {
        ALL_TRAITS.add(trait);
        return trait;
    }

    public static Trait[] getTraits() {
        return ALL_TRAITS.toArray(new Trait[0]);
    }
}
