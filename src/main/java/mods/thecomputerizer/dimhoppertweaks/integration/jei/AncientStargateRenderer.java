package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import gcewing.sg.block.SGBaseBlock;
import gcewing.sg.tileentity.SGBaseTE;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ISGBaseTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;


import java.util.List;

import static net.minecraft.init.Blocks.BEDROCK;
import static gcewing.sg.BaseOrientation.Orient4WaysByState.FACING;
import static gcewing.sg.SGCraft.sgChevronUpgrade;
import static gcewing.sg.util.SGState.Connected;
import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.math.BlockPos.ORIGIN;

@SuppressWarnings("unchecked")
public class AncientStargateRenderer extends ProxyWorldRender {
    
    public static AncientStargateRenderer fromRecipe(StargateRecipe recipe) {
        List<ItemStack> inputs = recipe.getInputs();
        return new AncientStargateRenderer(inputs.get(1),inputs.get(2));
    }

    private final IBlockState stargateBaseState;
    private final IBlockState stargateRingState1;
    private final IBlockState stargateRingState2;
    private final IBlockState heavyDutyState;
    private final IBlockState cornerState;

    public AncientStargateRenderer(ItemStack heavyDutyStack, ItemStack cornerStack) {
        super();
        this.heavyDutyState = getState(heavyDutyStack);
        this.stargateBaseState = getState("sgcraft:stargatebase",0).withProperty(FACING,NORTH);
        this.stargateRingState1 = getState("sgcraft:stargatering",0);
        this.stargateRingState2 = getState("sgcraft:stargatering",1);
        this.cornerState = getState(cornerStack);
        setUpPosMap();
    }

    private void setUpPosMap() {
        BlockPos pos = ORIGIN;
        addStatePos(pos,BEDROCK.getDefaultState());
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
            if(!sgTile.hasChevronUpgrade) sgTile.applyChevronUpgrade(new ItemStack(sgChevronUpgrade),null);
            sgTile.state = Connected;
        }
    }

    @Override
    protected void setUpState(IBlockState state, BlockPos pos) {
        if(state.getBlock() instanceof SGBaseBlock) state.getBlock().onBlockAdded(this.proxyWorld,pos,state);
    }
}
