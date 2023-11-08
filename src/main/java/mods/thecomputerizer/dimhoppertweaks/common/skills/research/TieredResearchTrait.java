package mods.thecomputerizer.dimhoppertweaks.common.skills.research;

import codersafterdark.reskillable.api.unlockable.Trait;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TieredResearchTrait extends Trait {

    private static String getLevelName(int level) {
        switch(level) {
            case 1: return "one";
            case 2: return "two";
            case 3: return "three";
            case 4: return "four";
            case 5: return "five";
            case 6: return "six";
            case 7: return "seven";
            case 8: return "eight";
            case 9: return "nine";
            default: return "zero";
        }
    }

    private int timer = 0;
    private final String stageName;

    public TieredResearchTrait(String type, int level, int yOffset) {
        this(type,level,getLevelName(level),yOffset);
    }

    private TieredResearchTrait(String type, int level, String levelName, int yOffset) {
        super(Constants.res(type+"_mk_"+levelName),level-1,yOffset,Constants.res("research"), 8,
                "reskillable:research|"+(32*level));
        this.stageName = type+levelName.toUpperCase();
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent ev) {
        this.timer--;
        if(this.timer<=0) {
            if(!GameStageHelper.hasStage(ev.player,this.stageName))
                GameStageHelper.addStage(ev.player,this.stageName);
            this.timer = 20;
        }
    }
}
