package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import sblectric.lightningcraft.ref.Metal.Rod;

import java.awt.*;

import static sblectric.lightningcraft.ref.Metal.*;

@Mixin(value = Rod.class, remap = false)
public class MixinMetalRod {
    
    @Unique private static final int COLOR_AWAKENED_DRACONIUM = new Color(251,192,45).getRGB();
    @Unique private static final int COLOR_DEFAULT = new Color(255,255,255).getRGB();
    @Unique private static final int COLOR_INSANIUM = new Color(160,72,177).getRGB();
    @Unique private static final int COLOR_ULTIMATE = new Color(26,35,126).getRGB();
    @Unique private static final int COLOR_INFINITY = new Color(0,0,0).getRGB();
    
    @Unique private static final String NAME_AWAKENED_DRACONIUM = "Draconium";
    @Unique private static final String NAME_DEFAULT = "[none]";
    @Unique private static final String NAME_INSANIUM = "Insanium";
    @Unique private static final String NAME_ULTIMATE = "Ultimate";
    @Unique private static final String NAME_INFINITY = "Infinity";
    
    /**
     * @author The_Computerizer
     * @reason Add colors for custom materials
     */
    @Overwrite
    public static int getColorFromMeta(int meta) {
        switch(meta) {
            case 0: return COLOR_IRON;
            case 1: return COLOR_STEEL;
            case 2: return COLOR_LEAD;
            case 3: return COLOR_TIN;
            case 4: return COLOR_ALUM;
            case 5: return COLOR_GOLD;
            case 6: return COLOR_COPPER;
            case 7: return COLOR_ELEC;
            case 8: return COLOR_SKY;
            case 9: return COLOR_MYSTIC;
            case 10: return COLOR_AWAKENED_DRACONIUM;
            case 11: return COLOR_INSANIUM;
            case 12: return COLOR_ULTIMATE;
            case 13: return COLOR_INFINITY;
            default: return COLOR_DEFAULT;
        }
    }
    
    /**
     * @author The_Computerizer
     * @reason Add names for custom materials
     */
    @Overwrite
    public static String getNameFromMeta(int meta) {
        switch(meta) {
            case 0: return NAME_IRON;
            case 1: return NAME_STEEL;
            case 2: return NAME_LEAD;
            case 3: return NAME_TIN;
            case 4: return NAME_ALUM;
            case 5: return NAME_GOLD;
            case 6: return NAME_COPPER;
            case 7: return NAME_ELEC;
            case 8: return NAME_SKY;
            case 9: return NAME_MYSTIC;
            case 10: return NAME_AWAKENED_DRACONIUM;
            case 11: return NAME_INSANIUM;
            case 12: return NAME_ULTIMATE;
            case 13: return NAME_INFINITY;
            default: return NAME_DEFAULT;
        }
    }
}
