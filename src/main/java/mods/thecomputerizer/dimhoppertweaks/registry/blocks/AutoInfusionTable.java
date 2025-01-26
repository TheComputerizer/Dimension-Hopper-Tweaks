package mods.thecomputerizer.dimhoppertweaks.registry.blocks;

import mcjty.lib.api.Infusable;
import mcjty.lib.crafting.INBTPreservingIngredient;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.rftools.blocks.GenericRFToolsBlock;
import mcjty.rftools.blocks.crafter.CrafterBaseTE;
import mods.thecomputerizer.dimhoppertweaks.client.gui.GuiAutoInfusion;
import mods.thecomputerizer.dimhoppertweaks.common.containers.AutoInfusionContainer;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.AutoInfusionTableEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import static net.minecraft.block.material.Material.IRON;
import static net.minecraft.item.ItemStack.EMPTY;
import static net.minecraft.util.text.TextFormatting.GREEN;
import static net.minecraft.util.text.TextFormatting.WHITE;
import static net.minecraft.util.text.TextFormatting.YELLOW;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;

public class AutoInfusionTable extends GenericRFToolsBlock<AutoInfusionTableEntity,AutoInfusionContainer>
        implements Infusable, INBTPreservingIngredient {
    
    public AutoInfusionTable(String name, Class<? extends AutoInfusionTableEntity> tileClass) {
        super(IRON,tileClass,AutoInfusionContainer::new,name,true);
    }
    
    @SideOnly(CLIENT)
    @SuppressWarnings("ConstantValue")
    public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag flags) {
        super.addInformation(stack,player,list,flags);
        NBTTagCompound tag = stack.getTagCompound();
        if(Objects.nonNull(tag)) {
            NBTTagList items = tag.getTagList("Items",10);
            NBTTagList recipes = tag.getTagList("Recipes",10);
            int rc = 0;
            for(int i=0;i<items.tagCount();i++) {
                NBTTagCompound item = items.getCompoundTagAt(i);
                if(Objects.nonNull(item)) {
                    ItemStack s = new ItemStack(item);
                    if(!s.isEmpty()) rc++;
                }
            }
            list.add(GREEN+"Contents: "+rc+" stacks");
            rc = 0;
            for(int i=0;i<recipes.tagCount();i++) {
                NBTTagCompound recipe = recipes.getCompoundTagAt(i);
                NBTTagCompound result = recipe.getCompoundTag("Result");
                if(Objects.nonNull(result)) {
                    ItemStack s = new ItemStack(result);
                    if(!s.isEmpty()) rc++;
                }
            }
            list.add(GREEN +"Recipes: "+rc+" recipes");
        }
        if(!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54)) list.add(WHITE + "<Press Shift>");
        else {
            list.add(WHITE+"This machine can handle up to 4 recipes");
            list.add(WHITE+"at once and allows recipes to use the crafting results");
            list.add(WHITE+"of previous steps.");
            list.add(YELLOW +"Infusing bonus: reduced power consumption.");
        }
    }
    
    @Override
    public Container createServerContainer(EntityPlayer player, TileEntity tile) {
        CrafterBaseTE crafter = (CrafterBaseTE)tile;
        crafter.getInventoryHelper().setStackInSlot(10,EMPTY);
        for(int i=0;i<10;i++) crafter.getInventoryHelper().setStackInSlot(i,EMPTY);
        return super.createServerContainer(player,tile);
    }
    
    @SideOnly(CLIENT)
    public BiFunction<AutoInfusionTableEntity,AutoInfusionContainer,GenericGuiContainer<? super AutoInfusionTableEntity>> getGuiFactory() {
        return GuiAutoInfusion::new;
    }
}
