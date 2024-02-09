package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import gcewing.sg.BaseOrientation;
import gcewing.sg.SGCraft;
import gcewing.sg.block.SGBaseBlock;
import gcewing.sg.block.SGBlock;
import gcewing.sg.tileentity.SGBaseTE;
import gcewing.sg.util.SGState;
import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ISGBaseTE;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.dave.compactmachines3.world.ProxyWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "deprecation"})
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AncientStargateRenderer {

    private static final Map<Integer,Supplier<AncientStargateRenderer>> ANCIENT_GATE_RENDERS = makeDimensionRenders();

    private static Map<Integer,Supplier<AncientStargateRenderer>> makeDimensionRenders() {
        Map<Integer,Supplier<AncientStargateRenderer>> renders = new HashMap<>();
        renders.put(-28,() -> new AncientStargateRenderer(2,StargateJeiPlugin.getCornerStack(-28))); //moon
        renders.put(-1502,() -> new AncientStargateRenderer(3,StargateJeiPlugin.getCornerStack(-1502))); //phobos
        renders.put(-31,() -> new AncientStargateRenderer(3,StargateJeiPlugin.getCornerStack(-31))); //venus
        renders.put(-30,() -> new AncientStargateRenderer(3,StargateJeiPlugin.getCornerStack(-30))); //asteroids
        renders.put(-29,() -> new AncientStargateRenderer(3,StargateJeiPlugin.getCornerStack(-29))); //mars
        renders.put(0,() -> new AncientStargateRenderer(3,StargateJeiPlugin.getCornerStack(0))); //overworld
        renders.put(816,() -> new AncientStargateRenderer(4,StargateJeiPlugin.getCornerStack(816))); //lunalus
        renders.put(-1506,() -> new AncientStargateRenderer(5,StargateJeiPlugin.getCornerStack(-1506))); //ganymede
        renders.put(-1501,() -> new AncientStargateRenderer(5,StargateJeiPlugin.getCornerStack(-1501))); //europa
        renders.put(-1500,() -> new AncientStargateRenderer(5,StargateJeiPlugin.getCornerStack(-1500))); //io
        renders.put(-15,() -> new AncientStargateRenderer(5,StargateJeiPlugin.getCornerStack(-15))); //jupiter
        renders.put(-1510,() -> new AncientStargateRenderer(6,StargateJeiPlugin.getCornerStack(-1510))); //titania
        renders.put(-1509,() -> new AncientStargateRenderer(6,StargateJeiPlugin.getCornerStack(-1509))); //oberon
        renders.put(-1508,() -> new AncientStargateRenderer(6,StargateJeiPlugin.getCornerStack(-1508))); //titan
        renders.put(-16,() -> new AncientStargateRenderer(6,StargateJeiPlugin.getCornerStack(-16))); //saturn
        renders.put(66,() -> new AncientStargateRenderer(8,StargateJeiPlugin.getCornerStack(66))); //erebus
        renders.put(-19,() -> new AncientStargateRenderer(9,StargateJeiPlugin.getCornerStack(-19))); //pluto
        return renders;
    }

    public static @Nullable AncientStargateRenderer getForDimension(int dimension) {
        Supplier<AncientStargateRenderer> supplier = ANCIENT_GATE_RENDERS.get(dimension);
        return Objects.nonNull(supplier) ? supplier.get() : null;
    }

    private final Map<BlockPos,IBlockState> relativePosMap;
    private final IBlockState stargateBaseState;
    private final IBlockState stargateRingState1;
    private final IBlockState stargateRingState2;
    private final IBlockState heavyDutyState;
    private final IBlockState cornerState;
    private final ProxyWorld proxyWorld;
    private final IBlockAccess blockAccess;
    private int glListId = -1;

    public AncientStargateRenderer(int heavyDutyLevel, ItemStack cornerStack) {
        this.relativePosMap = new HashMap<>();
        this.heavyDutyState = getState("contenttweaker:rocket_block_"+heavyDutyLevel,0);
        this.stargateBaseState = getState("sgcraft:stargatebase",0).withProperty(BaseOrientation.Orient4WaysByState.FACING,EnumFacing.NORTH);
        this.stargateRingState1 = getState("sgcraft:stargatering",0);
        this.stargateRingState2 = getState("sgcraft:stargatering",1);
        this.cornerState = Block.getBlockFromItem(cornerStack.getItem()).getStateFromMeta(cornerStack.getMetadata());
        this.proxyWorld = new ProxyWorld();
        this.blockAccess = getBlockAccess(this.proxyWorld);
        this.proxyWorld.setFakeWorld(this.blockAccess);
        setUpPosMap();
    }

    private IBlockAccess getBlockAccess(final ProxyWorld proxyWorld) {
        return new IBlockAccess() {
            @Nullable
            public TileEntity getTileEntity(BlockPos pos) {
                IBlockState state = this.getBlockState(pos);
                if (!state.getBlock().hasTileEntity(state)) return null;
                else {
                    TileEntity tile = state.getBlock().createTileEntity(proxyWorld, state);
                    if(Objects.nonNull(tile))
                        tile.setWorld(proxyWorld);
                    return tile;
                }
            }

            public int getCombinedLight(BlockPos pos, int lightValue) {
                return 255;
            }

            public IBlockState getBlockState(BlockPos pos) {
                return AncientStargateRenderer.this.relativePosMap.getOrDefault(pos,Blocks.AIR.getDefaultState());
            }

            public boolean isAirBlock(BlockPos pos) {
                IBlockState blockState = this.getBlockState(pos);
                return blockState.getBlock().isAir(blockState, this, pos);
            }

            public Biome getBiome(BlockPos pos) {
                return Biomes.PLAINS;
            }

            public int getStrongPower(BlockPos pos, EnumFacing direction) {
                return 0;
            }

            public WorldType getWorldType() {
                return WorldType.FLAT;
            }

            public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
                return this.getBlockState(pos).isSideSolid(this, pos, side);
            }
        };
    }

    private IBlockState getState(String res, int meta) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(res));
        return getState(item,meta);
    }

    private IBlockState getState(@Nullable Item item, int meta) {
        return Objects.nonNull(item) ? Block.getBlockFromItem(item).getStateFromMeta(meta) : Blocks.AIR.getDefaultState();
    }

    private void addPos(BlockPos pos, IBlockState state) {
        this.relativePosMap.put(pos,state);
    }

    private void setUpPosMap() {
        BlockPos pos = BlockPos.ORIGIN;
        addPos(pos,Blocks.BEDROCK.getDefaultState());
        addGatePositions(pos);
        addPlatformPositions();
    }

    private void addGatePositions(BlockPos pos) {
        pos = pos.add(0,1,0);
        pos = addGateHorizontal(pos,this.stargateBaseState);
        pos = addGateSides(pos,this.stargateRingState1);
        pos = addGateSides(pos,this.stargateRingState2);
        pos = addGateSides(pos,this.stargateRingState1);
        addGateHorizontal(pos,this.stargateRingState2);
    }

    private BlockPos addGateHorizontal(BlockPos pos, IBlockState center) {
        addPos(pos,center);
        pos = pos.add(1,0,0);
        addPos(pos,this.stargateRingState1);
        pos = pos.add(-2,0,0);
        addPos(pos,this.stargateRingState1);
        pos = pos.add(3,0,0);
        addPos(pos,this.stargateRingState2);
        pos = pos.add(-4,0,0);
        addPos(pos,this.stargateRingState2);
        return pos.add(2,1,0);
    }

    private BlockPos addGateSides(BlockPos pos, IBlockState state) {
        pos = pos.add(2,0,0);
        addPos(pos,state);
        pos = pos.add(-4,0,0);
        addPos(pos,state);
        return pos.add(2,1,0);
    }

    private void addPlatformPositions() {
        for(int x=-3; x<=3; x++) {
            for(int z=-3; z<=3; z++) {
                if(x==0 && z==0) continue;
                addPos(new BlockPos(x,0,z),Math.abs(x)==3 && Math.abs(z)==3 ? this.cornerState : this.heavyDutyState);
            }
        }
    }

    public void render(float partialTick) {
        if(this.glListId==-1) initializeDisplayList();
        GlStateManager.callList(this.glListId);
        ForgeHooksClient.setRenderLayer(BlockRenderLayer.SOLID);
        TileEntityRendererDispatcher renderer = TileEntityRendererDispatcher.instance;
        renderer.renderEngine = Minecraft.getMinecraft().renderEngine;
        for(BlockPos relativePos : this.relativePosMap.keySet()) {
            TileEntity tile = this.proxyWorld.getTileEntity(relativePos);
            if(Objects.nonNull(tile)) {
                tile.setWorld(this.proxyWorld);
                tile.setPos(relativePos);
                if(tile instanceof ITickable) ((ITickable)tile).update();
                updateSGBaseTile(tile);
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
        ForgeHooksClient.setRenderLayer(null);
    }

    private void updateSGBaseTile(TileEntity tile) {
        if(tile instanceof SGBaseTE) {
            SGBaseTE sgTile = (SGBaseTE)tile;
            ((ISGBaseTE)sgTile).dimhoppertweaks$mergeWithoutAddressing();
            if(!sgTile.hasChevronUpgrade) sgTile.applyChevronUpgrade(new ItemStack(SGCraft.sgChevronUpgrade),null);
            sgTile.state = SGState.Connected;
        }
    }

    private void initializeDisplayList() {
        BlockPos pos = new BlockPos(0,1,0);
        IBlockState state = this.relativePosMap.get(pos);
        if(state.getBlock() instanceof SGBaseBlock) state.getBlock().onBlockAdded(this.proxyWorld,pos,state);
        this.glListId = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.glListId, 4864);
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        buffer.begin(7, DefaultVertexFormats.BLOCK);
        GlStateManager.disableAlpha();
        this.renderLayer(dispatcher, buffer, BlockRenderLayer.SOLID);
        GlStateManager.enableAlpha();
        this.renderLayer(dispatcher, buffer, BlockRenderLayer.CUTOUT_MIPPED);
        this.renderLayer(dispatcher, buffer, BlockRenderLayer.CUTOUT);
        GlStateManager.shadeModel(7424);
        this.renderLayer(dispatcher, buffer, BlockRenderLayer.TRANSLUCENT);
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
        GlStateManager.glEndList();
    }

    private void renderLayer(BlockRendererDispatcher dispatcher, BufferBuilder buffer, BlockRenderLayer layer) {
        for(BlockPos relativePos : this.relativePosMap.keySet()) {
            IBlockState state = this.proxyWorld.getBlockState(relativePos);
            Block block = state.getBlock();
            if(block.canRenderInLayer(state,layer) && !(block instanceof SGBlock)) {
                ForgeHooksClient.setRenderLayer(layer);
                try {
                    dispatcher.renderBlock(state,relativePos,this.blockAccess,buffer);
                } catch (Exception ex) {
                    DHTRef.LOGGER.error("Caught unknown error while rendering ancient stargate preview!",ex);
                }
                ForgeHooksClient.setRenderLayer(null);
            }
        }
    }
}
