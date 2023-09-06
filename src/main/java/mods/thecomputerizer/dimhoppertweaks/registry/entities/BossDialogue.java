package mods.thecomputerizer.dimhoppertweaks.registry.entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class BossDialogue {
    public BossDialogue() {}

    public void introOne(EntityPlayer player) {
        player.sendMessage(new TextComponentString("First dialogue message"));
    }

    public void introTwo(EntityPlayer player) {
        player.sendMessage(new TextComponentString("Second dialogue message"));
    }

    public void introThree(EntityPlayer player) {
        player.sendMessage(new TextComponentString("Third dialogue message"));
    }

    public void introFour(EntityPlayer player) {
        player.sendMessage(new TextComponentString("Final dialogue message"));
    }
}
