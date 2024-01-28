package mods.thecomputerizer.dimhoppertweaks.integration.naturesaura;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.chunk.IDrainSpotEffect;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import de.ellpeck.naturesaura.items.ModItems;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.registry.TraitRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class NaturesAuraSkillEffect implements IDrainSpotEffect {

    public static final ResourceLocation NAME = DHTRef.res("natures_aura_skill_effect");

    @Override
    public boolean appliesHere(Chunk chunk, IAuraChunk auraChunk, IAuraType type) {
        return true;
    }

    @Override
    public ItemStack getDisplayIcon() {
        return new ItemStack(ModItems.AURA_TROVE);
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public int isActiveHere(EntityPlayer player, Chunk chunk, IAuraChunk auraChunk, BlockPos pos, Integer spot) {
        boolean isActive = SkillWrapper.hasTrait(PlayerDataHandler.get(player),"magic",TraitRegistry.NATURES_AURA);
        return isActive ? 1 : -1;
    }

    @Override
    public void update(World world, Chunk chunk, IAuraChunk auraChunk, BlockPos pos, Integer spot) {
        if(IAuraChunk.getAuraInArea(world,pos,30)<=DHTConfigHelper.getAuraCap()) {
            int aura = DHTConfigHelper.getAuraGains();
            DHTConfigHelper.devInfo("STORING {}} AURA AT POS ({},{},{})",aura,pos.getX(),pos.getY(),pos.getZ());
            auraChunk.storeAura(pos,aura,false,false);
        }
    }
}
