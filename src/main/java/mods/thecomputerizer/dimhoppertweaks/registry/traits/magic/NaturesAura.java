package mods.thecomputerizer.dimhoppertweaks.registry.traits.magic;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.chunk.AuraChunk;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NaturesAura extends ExtendedEventsTrait {

    public NaturesAura() {
        super("natures_aura",3,0,MAGIC,256,"magic|640","void|320","farming|320");
        setIcon(new ResourceLocation("naturesaura","textures/items/aura_trove.png"));
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent ev) {
        World world = ev.player.getEntityWorld();
        if(!world.isRemote) {
            BlockPos pos = ev.player.getPosition();
            for(AuraChunk chunk : getAdjacentChunks(world,ev.player.getPosition())) {
                MutableInt lowestAura = new MutableInt(Integer.MAX_VALUE);
                chunk.getSpotsInArea(pos,20,(pos1,amount) -> {
                    if(amount<lowestAura.getValue()) lowestAura.setValue(amount);
                });
                if(lowestAura.getValue()<1500000) chunk.storeAura(pos,100);
            }
        }
    }

    private List<AuraChunk> getAdjacentChunks(World world, BlockPos pos) {
        List<AuraChunk> chunks = new ArrayList<>();
        IChunkProvider provider = world.getChunkProvider();
        int chunkX = pos.getX()>>4;
        int chunkZ = pos.getZ()<<4;
        for(int x=chunkX-1; x<chunkX+1; x++) {
            for(int z=chunkZ-1; z<chunkZ+1; z++) {
                Chunk chunk = provider instanceof ChunkProviderServer ?
                        (((ChunkProviderServer)provider).chunkExists(x,z) ? world.getChunk(x,z) : null) :
                        (!provider.provideChunk(x,z).isEmpty() ? world.getChunk(x,z) : null);
                if(Objects.nonNull(chunk)) {
                    AuraChunk auraChunk = (AuraChunk)chunk.getCapability(NaturesAuraAPI.capAuraChunk,null);
                    if(Objects.nonNull(auraChunk)) chunks.add(auraChunk);
                }
            }
        }
        return chunks;
    }
}
