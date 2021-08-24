package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class BossDialogue
{
    public static ITextComponent text;

    public static void dialogueOne(EntityPlayer player)
    {
        text = new TextComponentString("First dialogue message");
        player.sendMessage(text);
    }

    public static void dialogueTwo(EntityPlayer player)
    {
        text = new TextComponentString("Second dialogue message");
        player.sendMessage(text);
    }

    public static void dialogueThree(EntityPlayer player)
    {
        text = new TextComponentString("Third dialogue message");
        player.sendMessage(text);
    }

    public static void dialogueFour(EntityPlayer player)
    {
        text = new TextComponentString("Final dialogue message");
        player.sendMessage(text);
    }
}
