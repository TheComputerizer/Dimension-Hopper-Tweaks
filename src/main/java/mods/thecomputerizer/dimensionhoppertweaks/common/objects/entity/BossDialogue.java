package mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class BossDialogue
{
    public static void summonDialogue(EntityPlayer player, long curTime, long time)
    {
        long t = (curTime-time);
        ITextComponent dialogue;
        if (t < 0.4)
        {
            dialogue = new TextComponentString("Intro goes here").setStyle(new Style().setColor(TextFormatting.DARK_RED));
            dialogue.setStyle(new Style().setItalic(true));
            player.sendMessage(dialogue);
        }
        else if (t > 1.2 && t < 1.5)
        {
            dialogue = new TextComponentString("Intro part 2").setStyle(new Style().setColor(TextFormatting.DARK_RED));
            dialogue.setStyle(new Style().setItalic(true));
            player.sendMessage(dialogue);
        }
    }
}
