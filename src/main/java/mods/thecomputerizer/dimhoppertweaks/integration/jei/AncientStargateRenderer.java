package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import gcewing.sg.BaseOrientation;
import gcewing.sg.SGCraft;
import gcewing.sg.block.SGBaseBlock;
import gcewing.sg.tileentity.SGBaseTE;
import gcewing.sg.util.SGState;
import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ISGBaseTE;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "deprecation"})
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AncientStargateRenderer extends ProxyWorldRender {

    private static final Map<Integer,Supplier<AncientStargateRenderer>> ANCIENT_GATE_RENDERS = makeDimensionRenders();

    private static Map<Integer,Supplier<AncientStargateRenderer>> makeDimensionRenders() {
        Map<Integer,Supplier<AncientStargateRenderer>> renders = new HashMap<>();
        renders.put(-28,() -> new AncientStargateRenderer(2, DHTJeiPlugin.getCornerStack(-28))); //moon
        renders.put(-1502,() -> new AncientStargateRenderer(3, DHTJeiPlugin.getCornerStack(-1502))); //phobos
        renders.put(-31,() -> new AncientStargateRenderer(3, DHTJeiPlugin.getCornerStack(-31))); //venus
        renders.put(-30,() -> new AncientStargateRenderer(3, DHTJeiPlugin.getCornerStack(-30))); //asteroids
        renders.put(-29,() -> new AncientStargateRenderer(3, DHTJeiPlugin.getCornerStack(-29))); //mars
        renders.put(0,() -> new AncientStargateRenderer(3, DHTJeiPlugin.getCornerStack(0))); //overworld
        renders.put(816,() -> new AncientStargateRenderer(4, DHTJeiPlugin.getCornerStack(816))); //lunalus
        renders.put(-1506,() -> new AncientStargateRenderer(5, DHTJeiPlugin.getCornerStack(-1506))); //ganymede
        renders.put(-1501,() -> new AncientStargateRenderer(5, DHTJeiPlugin.getCornerStack(-1501))); //europa
        renders.put(-1500,() -> new AncientStargateRenderer(5, DHTJeiPlugin.getCornerStack(-1500))); //io
        renders.put(-15,() -> new AncientStargateRenderer(5, DHTJeiPlugin.getCornerStack(-15))); //jupiter
        renders.put(-1510,() -> new AncientStargateRenderer(6, DHTJeiPlugin.getCornerStack(-1510))); //titania
        renders.put(-1509,() -> new AncientStargateRenderer(6, DHTJeiPlugin.getCornerStack(-1509))); //oberon
        renders.put(-1508,() -> new AncientStargateRenderer(6, DHTJeiPlugin.getCornerStack(-1508))); //titan
        renders.put(-16,() -> new AncientStargateRenderer(6, DHTJeiPlugin.getCornerStack(-16))); //saturn
        renders.put(66,() -> new AncientStargateRenderer(8, DHTJeiPlugin.getCornerStack(66))); //erebus
        renders.put(-19,() -> new AncientStargateRenderer(9, DHTJeiPlugin.getCornerStack(-19))); //pluto
        return renders;
    }

    public static @Nullable AncientStargateRenderer getForDimension(int dimension) {
        Supplier<AncientStargateRenderer> supplier = ANCIENT_GATE_RENDERS.get(dimension);
        return Objects.nonNull(supplier) ? supplier.get() : null;
    }

    private final IBlockState stargateBaseState;
    private final IBlockState stargateRingState1;
    private final IBlockState stargateRingState2;
    private final IBlockState heavyDutyState;
    private final IBlockState cornerState;

    public AncientStargateRenderer(int heavyDutyLevel, ItemStack cornerStack) {
        super();
        this.heavyDutyState = getState("contenttweaker:rocket_block_"+heavyDutyLevel,0);
        this.stargateBaseState = getState("sgcraft:stargatebase",0).withProperty(BaseOrientation.Orient4WaysByState.FACING,EnumFacing.NORTH);
        this.stargateRingState1 = getState("sgcraft:stargatering",0);
        this.stargateRingState2 = getState("sgcraft:stargatering",1);
        this.cornerState = Block.getBlockFromItem(cornerStack.getItem()).getStateFromMeta(cornerStack.getMetadata());
        setUpPosMap();
    }

    private void setUpPosMap() {
        BlockPos pos = BlockPos.ORIGIN;
        addStatePos(pos,Blocks.BEDROCK.getDefaultState());
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
        addStatePos(pos,center);
        pos = pos.add(1,0,0);
        addStatePos(pos,this.stargateRingState1);
        pos = pos.add(-2,0,0);
        addStatePos(pos,this.stargateRingState1);
        pos = pos.add(3,0,0);
        addStatePos(pos,this.stargateRingState2);
        pos = pos.add(-4,0,0);
        addStatePos(pos,this.stargateRingState2);
        return pos.add(2,1,0);
    }

    private BlockPos addGateSides(BlockPos pos, IBlockState state) {
        pos = pos.add(2,0,0);
        addStatePos(pos,state);
        pos = pos.add(-4,0,0);
        addStatePos(pos,state);
        return pos.add(2,1,0);
    }

    private void addPlatformPositions() {
        for(int x=-3; x<=3; x++) {
            for(int z=-3; z<=3; z++) {
                if(x==0 && z==0) continue;
                addStatePos(new BlockPos(x,0,z),Math.abs(x)==3 && Math.abs(z)==3 ? this.cornerState : this.heavyDutyState);
            }
        }
    }

    @Override
    protected void updateTileExtra(TileEntity tile) {
        if(tile instanceof SGBaseTE) {
            SGBaseTE sgTile = (SGBaseTE)tile;
            ((ISGBaseTE)sgTile).dimhoppertweaks$mergeWithoutAddressing();
            if(!sgTile.hasChevronUpgrade) sgTile.applyChevronUpgrade(new ItemStack(SGCraft.sgChevronUpgrade),null);
            sgTile.state = SGState.Connected;
        }
    }

    @Override
    protected void setUpState(IBlockState state, BlockPos pos) {
        if(state.getBlock() instanceof SGBaseBlock) state.getBlock().onBlockAdded(this.proxyWorld,pos,state);
    }
}
