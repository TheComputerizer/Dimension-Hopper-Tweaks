package mods.thecomputerizer.dimhoppertweaks.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class SmiteStick extends DHTCommand {

    public SmiteStick() {
        super("stickofsmite");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length==0) sendMessage(sender,true,null);
        EntityPlayer player = getPlayer(server,sender,args[0]);
        ItemStack stack = new ItemStack(Items.STICK);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("ench",buildEnchantmentTag());
        tag.setTag("display",buildDisplayTag());
        stack.setTagCompound(tag);
        EntityItem item = player.dropItem(stack,false);
        if(Objects.nonNull(item)) {
            item.setNoPickupDelay();
            item.setOwner(player.getName());
        }
    }

    private NBTTagCompound buildDisplayTag() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("LocName","item.dimhoppertweaks.stick_of_smite.name");
        return tag;
    }

    private NBTTagList buildEnchantmentTag() {
        NBTTagList list = new NBTTagList();
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("lvl",100);
        tag.setInteger("id",getEnchantmentID());
        list.appendTag(tag);
        return list;
    }

    private int getEnchantmentID() {
        ResourceLocation res = new ResourceLocation("lightningcraft","thor");
        if(ForgeRegistries.ENCHANTMENTS.containsKey(res)) {
            Enchantment enchant = ForgeRegistries.ENCHANTMENTS.getValue(res);
            return Objects.nonNull(enchant) ? Enchantment.getEnchantmentID(enchant) : 0;
        }
        return 0;
    }
}
