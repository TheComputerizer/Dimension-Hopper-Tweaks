package mods.thecomputerizer.dimhoppertweaks.integration.naturesaura;

import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.chunk.IDrainSpotEffect;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import static de.ellpeck.naturesaura.items.ModItems.AURA_TROVE;

public class NaturesAuraSkillEffect implements IDrainSpotEffect {

    public static final ResourceLocation NAME = DHTRef.res("natures_aura_skill_effect");

    @Override
    public boolean appliesHere(Chunk chunk, IAuraChunk auraChunk, IAuraType type) {
        return true;
    }

    @Override
    public ItemStack getDisplayIcon() {
        return new ItemStack(AURA_TROVE);
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public int isActiveHere(EntityPlayer player, Chunk chunk, IAuraChunk auraChunk, BlockPos pos, Integer spot) {
        return SkillWrapper.makesChunksFast(player) ? 1 : -1;
    }

    @Override
    public void update(World world, Chunk chunk, IAuraChunk auraChunk, BlockPos pos, Integer spot) {
        int aura = DHTConfigHelper.auraInRadius(world,pos);
        if(DHTConfigHelper.canAddAura(aura)) {
            int gains = DHTConfigHelper.getAuraGains();
            DHTConfigHelper.devInfo("[{}] ADDING `{}` TO AREA WITH `{}` AURA",pos,gains,aura);
            auraChunk.storeAura(pos,gains,false,false);
        }
    }
}
