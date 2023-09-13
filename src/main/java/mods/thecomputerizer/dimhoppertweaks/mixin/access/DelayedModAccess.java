package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class DelayedModAccess {

    public static Collection<String> getGameStages(EntityPlayer player) {
        IStageData data = GameStageHelper.getPlayerData(player);
        return Objects.isNull(data) ? new ArrayList<>() : data.getStages();
    }
}
