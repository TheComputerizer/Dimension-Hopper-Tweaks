package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import lumien.randomthings.config.Numbers;
import lumien.randomthings.item.ItemTimeInABottle;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.ItemTimeInABottleAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemTimeInABottle.class)
public class MixinItemTimeInABottle implements ItemTimeInABottleAccess {

    @Unique private ItemTimeInABottle dimhoppertweaks$cast() {
        return (ItemTimeInABottle)(Object)this;
    }

    @Unique private boolean dimhoppertweaks$checkReversal(ItemStack stack, World world, Entity entity) {
        if(!world.isRemote && (world.provider.getDimension()==44 || world.provider.getDimension()==45) &&
                entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            return player.getHeldItemOffhand()==stack || player.getHeldItemMainhand()==stack;
        }
        return false;
    }

    @Unique private void dimhoppertweaks$updateTime(ItemStack stack, int time) {
        NBTTagCompound timeData = stack.getOrCreateSubCompound("timeData");
        int stored = timeData.getInteger("storedTime");
        boolean canAdd = (time>=0 && stored<622080000) || (time<0 && stored>0);
        if (canAdd) {
            stored = MathHelper.clamp(stored+time,0,622080000);
            timeData.setInteger("storedTime",stored);
        }
    }

    /**
     * @author The_Computerizer
     * @reason Add dream timer hooks
     */
    @Overwrite
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        if (!world.isRemote) {
            int secondWorth = Numbers.TIME_IN_A_BOTTLE_SECOND;
            if(dimhoppertweaks$checkReversal(stack, world, entity)) dimhoppertweaks$updateTime(stack, -2);
            else if (secondWorth == 0 || world.getTotalWorldTime() % (long) secondWorth == 0L)
                dimhoppertweaks$updateTime(stack, 20);
            if (world.getTotalWorldTime() % 60L == 0L && entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)entity;
                for(int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                    ItemStack invStack = player.inventory.getStackInSlot(i);
                    if (invStack.getItem() == dimhoppertweaks$cast() && invStack != stack) {
                        NBTTagCompound otherTimeData = invStack.getOrCreateSubCompound("timeData");
                        NBTTagCompound myTimeData = stack.getOrCreateSubCompound("timeData");
                        int myTime = myTimeData.getInteger("storedTime");
                        int theirTime = otherTimeData.getInteger("storedTime");
                        if (myTime < theirTime) {
                            myTimeData.setInteger("storedTime", 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean dimhoppertweaks$hasTime(ItemStack stack) {
        return stack.getOrCreateSubCompound("timeData").getInteger("storedTime")>0;
    }
}
