package mods.thecomputerizer.dimhoppertweaks.mixin.mods.twilightforest;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import slimeknights.tconstruct.library.utils.ToolHelper;
import twilightforest.compat.tcon.trait.TraitSynergy;
import twilightforest.util.TFItemStackUtils;

import static twilightforest.enums.CompressedVariant.STEELLEAF;
import static twilightforest.item.TFItems.TOOL_STEELEAF;
import static twilightforest.item.TFItems.block_storage;
import static twilightforest.item.TFItems.steeleaf_ingot;

@Mixin(value = TraitSynergy.class, remap = false)
public abstract class MixinTraitSynergy {

    @Shadow
    private static boolean needsRepair(ItemStack itemStack) {
        return false;
    }

    @Shadow
    private static int averageInt(float value) {
        return 0;
    }

    /**
     * @author The_Computerizer
     * @reason Nerf steeleaf
     */
    @Overwrite
    public void onUpdate(ItemStack tool, World world, Entity entity, int slot, boolean isSelected) {
        if(!world.isRemote && entity instanceof EntityPlayer && !(entity instanceof FakePlayer)) {
            EntityPlayer player = (EntityPlayer)entity;
            if(!InventoryPlayer.isHotbar(slot) && player.getHeldItemOffhand()!=tool) return;
            if(!needsRepair(tool)) return;
            int healPower = 0;
            NonNullList<ItemStack> playerInv = player.inventory.mainInventory;
            for(int i=0;i<9;i++) {
                if(i!=slot) {
                    ItemStack stack = playerInv.get(i);
                    if(stack.getItem()==steeleaf_ingot || (stack.getItem()==block_storage &&
                            stack.getMetadata()==STEELLEAF.ordinal()))
                        healPower+=stack.getCount()/2;
                    else if(TFItemStackUtils.hasToolMaterial(stack,TOOL_STEELEAF)) healPower++;
                }
            }
            ToolHelper.healTool(tool,averageInt((float)healPower*0.00390625f),player);
        }
    }
}