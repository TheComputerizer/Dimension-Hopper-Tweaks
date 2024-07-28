package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import gcewing.sg.block.SGBlock;
import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.ForgeHooksClient;
import org.dave.compactmachines3.world.ProxyWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.instance;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.BLOCK;
import static net.minecraft.init.Biomes.PLAINS;
import static net.minecraft.init.Blocks.AIR;
import static net.minecraft.item.ItemStack.EMPTY;
import static net.minecraft.util.BlockRenderLayer.*;
import static net.minecraft.world.WorldType.FLAT;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public abstract class ProxyWorldRender {

    protected final Map<BlockPos,IBlockState> relativePosMap;
    protected final List<Entity> renderableEntities;
    protected final ProxyWorld proxyWorld;
    protected final IBlockAccess blockAccess;
    private int glListId = -1;

    protected ProxyWorldRender() {
        this.relativePosMap = new HashMap<>();
        this.renderableEntities = new ArrayList<>();
        this.proxyWorld = new ProxyWorld();
        this.blockAccess = getBlockAccess(this.proxyWorld);
        this.proxyWorld.setFakeWorld(this.blockAccess);
    }


    protected IBlockAccess getBlockAccess(final ProxyWorld proxyWorld) {
        return new IBlockAccess() {
            @Nullable
            public TileEntity getTileEntity(BlockPos pos) {
                IBlockState state = this.getBlockState(pos);
                if (!state.getBlock().hasTileEntity(state)) return null;
                else {
                    TileEntity tile = state.getBlock().createTileEntity(proxyWorld,state);
                    if(Objects.nonNull(tile))
                        tile.setWorld(proxyWorld);
                    return tile;
                }
            }

            public int getCombinedLight(BlockPos pos, int lightValue) {
                return 255;
            }

            public IBlockState getBlockState(BlockPos pos) {
                return ProxyWorldRender.this.relativePosMap.getOrDefault(pos,AIR.getDefaultState());
            }

            public boolean isAirBlock(BlockPos pos) {
                IBlockState blockState = this.getBlockState(pos);
                return blockState.getBlock().isAir(blockState, this, pos);
            }

            public Biome getBiome(BlockPos pos) {
                return PLAINS;
            }

            public int getStrongPower(BlockPos pos, EnumFacing direction) {
                return 0;
            }

            public WorldType getWorldType() {
                return FLAT;
            }

            public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
                return this.getBlockState(pos).isSideSolid(this,pos,side);
            }
        };
    }

    protected IBlockState getState(String res, int meta) {
        Item item = ITEMS.getValue(new ResourceLocation(res));
        return getState(item,meta);
    }
    
    protected IBlockState getState(ItemStack stack) {
        return stack==EMPTY ? getState((Item)null,0) : getState(stack.getItem(),stack.getMetadata());
    }

    protected IBlockState getState(@Nullable Item item, int meta) {
        return Objects.nonNull(item) ? Block.getBlockFromItem(item).getStateFromMeta(meta) : AIR.getDefaultState();
    }

    protected void addRenderableEntity(Entity entity) {
        this.renderableEntities.add(entity);
    }

    protected void addStatePos(BlockPos pos, IBlockState state) {
        this.relativePosMap.put(pos,state);
    }

    public void render(float partialTick) {
        if(this.glListId==-1) initializeDisplayList();
        GlStateManager.callList(this.glListId);
        ForgeHooksClient.setRenderLayer(SOLID);
        TileEntityRendererDispatcher renderer = instance;
        renderer.renderEngine = Minecraft.getMinecraft().renderEngine;
        for(BlockPos relativePos : this.relativePosMap.keySet()) {
            TileEntity tile = this.proxyWorld.getTileEntity(relativePos);
            if(Objects.nonNull(tile)) {
                tile.setWorld(this.proxyWorld);
                tile.setPos(relativePos);
                if(tile instanceof ITickable) ((ITickable)tile).update();
                updateTileExtra(tile);
                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();
                try {
                    renderer.render(tile,relativePos.getX(),relativePos.getY(),relativePos.getZ(),partialTick);
                } catch (Exception ex) {
                    DHTRef.LOGGER.error("Could not render ancient stargate tile entity {}!",tile.getClass(),ex);
                }
                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }
        }
        for(Entity entity : this.renderableEntities) {
            Vec3d vec = entity.getPositionVector();
            Minecraft.getMinecraft().getRenderManager().renderEntity(entity,vec.x,vec.y,vec.z,0f,partialTick,false);
        }
        ForgeHooksClient.setRenderLayer(null);
    }

    protected abstract void updateTileExtra(TileEntity tile);

    private void initializeDisplayList() {
        BlockPos pos = new BlockPos(0,1,0);
        setUpState(this.relativePosMap.get(pos),pos);
        this.glListId = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.glListId,4864);
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        buffer.begin(7,BLOCK);
        GlStateManager.disableAlpha();
        this.renderLayer(dispatcher,buffer,SOLID);
        GlStateManager.enableAlpha();
        this.renderLayer(dispatcher,buffer,CUTOUT_MIPPED);
        this.renderLayer(dispatcher,buffer,CUTOUT);
        GlStateManager.shadeModel(7424);
        this.renderLayer(dispatcher,buffer,TRANSLUCENT);
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
        GlStateManager.glEndList();
    }

    protected abstract void setUpState(IBlockState state, BlockPos pos);

    private void renderLayer(BlockRendererDispatcher dispatcher, BufferBuilder buffer, BlockRenderLayer layer) {
        for(BlockPos relativePos : this.relativePosMap.keySet()) {
            IBlockState state = this.proxyWorld.getBlockState(relativePos);
            Block block = state.getBlock();
            if(block.canRenderInLayer(state,layer) && !(block instanceof SGBlock)) {
                ForgeHooksClient.setRenderLayer(layer);
                try {
                    dispatcher.renderBlock(state,relativePos,this.blockAccess,buffer);
                } catch (Exception ex) {
                    LOGGER.error("Caught unknown error while rendering ancient stargate preview!",ex);
                }
                ForgeHooksClient.setRenderLayer(null);
            }
        }
    }
}
