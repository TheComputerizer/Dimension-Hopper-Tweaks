package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import mariot7.xlfoodmod.init.ItemListxlfoodmod;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class DelayedModAccess {

    public static Collection<String> getGameStages(EntityPlayer player) {
        if(Objects.isNull(player)) return new ArrayList<>();
        IStageData data = GameStageHelper.getPlayerData(player);
        return Objects.isNull(data) ? new ArrayList<>() : data.getStages();
    }

    public static void setGameStages(EntityPlayer player, Collection<String> stages) {
        for(String stage : stages) setGameStage(player,stage);
    }

    public static void setGameStage(EntityPlayer player, String stage) {
        if(Objects.nonNull(player)) GameStageHelper.addStage(player,stage);
    }

    public static boolean hasGameStage(EntityPlayer player, String stage) {
        return Objects.nonNull(player) && GameStageHelper.hasStage(player,stage);
    }
    public static ItemStack cheese() {
        return new ItemStack(ItemListxlfoodmod.cheese);
    }
}
