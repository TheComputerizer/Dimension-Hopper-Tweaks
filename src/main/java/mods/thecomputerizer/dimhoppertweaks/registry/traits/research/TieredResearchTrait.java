package mods.thecomputerizer.dimhoppertweaks.registry.traits.research;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TieredResearchTrait extends ExtendedEventsTrait {

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

    public TieredResearchTrait(String type, int level, int xOffset, int yOffset) {
        this(type,level,getLevelName(level),xOffset,yOffset);
    }

    private TieredResearchTrait(String type, int level, String levelName, int xOffset, int yOffset) {
        super(type+"_mk_"+levelName,level-1+xOffset,yOffset,RESEARCH,8,"research|"+(32*level));
        this.stageName = type+levelName.toUpperCase();
        if(type.matches("lightning")) setLightningTexture(level);
    }

    private void setLightningTexture(int level) {
        String modid = "lightningcraft";
        String texturePath = "ingot_skyfather";
        if(level==3) texturePath = "ingot_mystic";
        else if(level==4) {
            modid = "extendedcrafting";
            texturePath = "material_ultimate_ingot";
        }
        else if(level==5) {
            modid = "avaritia";
            texturePath = "resource/infinity_catalyst";
        }
        setIcon(new ResourceLocation(modid,"textures/items/"+texturePath+".png"));
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
