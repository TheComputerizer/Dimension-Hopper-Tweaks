package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mcjty.lib.network.NetworkTools;
import mcjty.lib.varia.Logging;
import mcjty.rftools.craftinggrid.CraftingRecipe;
import mcjty.rftools.craftinggrid.CraftingRecipe.CraftMode;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.AutoInfusionTableEntity;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

import static net.minecraft.item.ItemStack.EMPTY;
import static net.minecraftforge.fml.relauncher.Side.SERVER;

public class PacketAutoInfusion extends MessageImpl {
    
    private BlockPos pos;
    private int index;
    private ItemStack[] items;
    private boolean keepOne;
    private CraftMode mode;
    
    public PacketAutoInfusion() {}
    
    public PacketAutoInfusion(BlockPos pos, int index, InventoryCrafting inv, ItemStack result, boolean keepOne,
            CraftMode mode) {
        this.pos = pos;
        this.index = index;
        this.items = new ItemStack[11];
        if(Objects.nonNull(inv)) {
            for(int i=0;i<10;i++) {
                ItemStack slot = inv.getStackInSlot(i);
                this.items[i] = slot.isEmpty() ? EMPTY : slot.copy();
            }
        } else
            for(int i=0;i<10;i++)
                this.items[i] = EMPTY;
        this.items[10] = result.isEmpty() ? EMPTY : result.copy();
        this.keepOne = keepOne;
        this.mode = mode;
    }
    
    @Override public void fromBytes(ByteBuf buf) {
        this.pos = NetworkTools.readPos(buf);
        this.keepOne = buf.readBoolean();
        this.mode = CraftMode.values()[buf.readByte()];
        this.index = buf.readByte();
        int l = buf.readByte();
        if(l==0) this.items = null;
        else {
            this.items = new ItemStack[l];
            for(int i=0;i<l;i++)
                this.items[i] = buf.readBoolean() ? NetworkTools.readItemStack(buf) : EMPTY;
        }
    }
    
    @Override public IMessage handle(MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        if(Objects.nonNull(player)) {
            TileEntity te = player.getEntityWorld().getTileEntity(this.pos);
            if (!(te instanceof AutoInfusionTableEntity)) {
                Logging.logError("Wrong type of tile entity (expected AutoInfusionTableEntity)!");
            } else {
                AutoInfusionTableEntity entity = (AutoInfusionTableEntity)te;
                entity.noRecipesWork = false;
                if(this.index!=-1) updateRecipe(entity);
                
            }
        }
        return null;
    }
    
    @Override public Side getSide() {
        return SERVER;
    }
    
    @Override public void toBytes(ByteBuf buf) {
        NetworkTools.writePos(buf,this.pos);
        buf.writeBoolean(this.keepOne);
        buf.writeByte(this.mode.ordinal());
        buf.writeByte(this.index);
        if(Objects.nonNull(this.items)) {
            buf.writeByte(this.items.length);
            for(ItemStack item : this.items) {
                if(item.isEmpty()) buf.writeBoolean(false);
                else {
                    buf.writeBoolean(true);
                    NetworkTools.writeItemStack(buf,item);
                }
            }
        } else buf.writeByte(0);
    }
    
    private void updateRecipe(AutoInfusionTableEntity entity) {
        CraftingRecipe recipe = entity.getRecipe(this.index);
        recipe.setRecipe(this.items,this.items[10]);
        recipe.setKeepOne(this.keepOne);
        recipe.setCraftMode(this.mode);
        entity.selectRecipe(this.index);
        entity.markDirtyClient();
    }
}