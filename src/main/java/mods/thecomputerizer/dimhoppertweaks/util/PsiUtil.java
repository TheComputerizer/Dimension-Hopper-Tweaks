package mods.thecomputerizer.dimhoppertweaks.util;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;

import java.util.Objects;

public class PsiUtil {

    public static IBlockState accountForOreStages(EntityPlayer player, IBlockState state) {
        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(state);
        return Objects.nonNull(stageInfo) && (Objects.isNull(player) ||
                !GameStageHelper.hasStage(player,stageInfo.getFirst())) ? stageInfo.getSecond() : state;
    }
}