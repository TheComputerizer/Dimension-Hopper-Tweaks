package mods.thecomputerizer.dimhoppertweaks.mixin.mods.extrautils2;

import binnie.core.gui.minecraft.ContainerCraftGUI;
import com.google.common.collect.Lists;
import com.rwtema.extrautils2.backend.XUItemFlat;
import com.rwtema.extrautils2.compatibility.StackHelper;
import com.rwtema.extrautils2.items.ItemUnstableIngots;
import com.rwtema.extrautils2.utils.datastructures.GetterSetter;
import com.rwtema.extrautils2.utils.datastructures.GetterSetter.ContainerSlot;
import com.rwtema.extrautils2.utils.datastructures.GetterSetter.InvSlot;
import com.rwtema.extrautils2.utils.datastructures.GetterSetter.PlayerHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.rwtema.extrautils2.items.ItemUnstableIngots.TIME_OUT;
import static net.minecraftforge.common.util.Constants.NBT.TAG_ANY_NUMERIC;

@Mixin(value = ItemUnstableIngots.class, remap = false)
public abstract class MixinItemUnstableIngots extends XUItemFlat {
    
    @Shadow private static int cooldown;
    
    @Shadow protected abstract void explodePlayer(EntityPlayer player);
    
    /**
     * @author The_Computerizer
     * @reason Fix crash with certain inventories
     */
    @Overwrite
    @SubscribeEvent
    public void checkForExplosion(PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.world;
        if(cooldown==0 || world.isRemote) return;
        Container container = player.openContainer;
        if(container instanceof ContainerCraftGUI) return;
        LinkedList<GetterSetter<ItemStack>> stacks = Lists.newLinkedList();
        InventoryPlayer inventory = player.inventory;
        for(int i=0;i<inventory.getSizeInventory();i++) stacks.add(new InvSlot(inventory, i));
        stacks.add(new PlayerHand(inventory));
        int windowId;
        if(Objects.isNull(container) || container==player.inventoryContainer) windowId = -1;
        else {
            windowId = container.windowId;
            List<Slot> slots = container.inventorySlots;
            for(Slot slot : slots) stacks.add(new ContainerSlot(slot));
        }
        LinkedList<GetterSetter<ItemStack>> newStacks = Lists.newLinkedList();
        for(GetterSetter<ItemStack> getterSetter : stacks) {
            ItemStack stack = getterSetter.get();
            if(StackHelper.isNull(stack) || stack.getItem()!=this) continue;
            NBTTagCompound tag = stack.getTagCompound();
            if (Objects.isNull(tag) || !tag.hasKey("time",TAG_ANY_NUMERIC)) continue;
            cooldown=TIME_OUT*2;
            if(tag.getLong("time")+TIME_OUT>world.getTotalWorldTime() &&
               tag.getInteger("dim")==world.provider.getDimension() &&
               tag.getInteger("container")==windowId) continue;
            newStacks.add(getterSetter);
        }
        if(newStacks.isEmpty()) return;
        for(GetterSetter<ItemStack> stackGetterSetter : newStacks) {
            ItemStack stack = stackGetterSetter.get();
            StackHelper.setStackSize(stack,0);
            stackGetterSetter.accept(StackHelper.empty());
        }
        explodePlayer(player);
        if(windowId!=-1) player.closeScreen();
        player.inventoryContainer.detectAndSendChanges();
    }
}