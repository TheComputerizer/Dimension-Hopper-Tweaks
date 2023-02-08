package mods.thecomputerizer.dimhoppertweaks.util;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;

public class PsiUtil {

    public static IBlockState accountForOreStages(EntityPlayer player, IBlockState state) {
        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(state);
        return stageInfo != null && (player == null || !GameStageHelper.hasStage(player, stageInfo.getFirst())) ?
                stageInfo.getSecond() : state;
    }
}