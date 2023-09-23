package mods.thecomputerizer.dimhoppertweaks.core;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {
    public static final String MODID = "dimhoppertweaks";
    public static final String NAME = "Dimension Hopper Tweaks";
    public static final String VERSION = "1.12.2-2.6.0";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2860,);" +
            "required-after:actuallyadditions;" +
            "required-after:aquaculture;" +
            "required-after:avaritia;" +
            "required-after:botania;" +
            "required-after:dimdoors;" +
            "required-after:gamestages;" +
            "required-after:geckolib3;" +
            "required-after:lockyzextradimensionsmod;" +
            "required-after:lightningcraft;" +
            "required-after:psi;" +
            "required-after:overloaded;" +
            "required-after:reskillable;" +
            "required-after:sgcraft;" +
            "required-after:tconstruct;" +
            "required-after:theimpossiblelibrary;" +
            "required-after:theoneprobe;" +
            "required-after:tp;" +
            "required-after:twilightforest;" +
            "required-after:xlfoodmod;";
    public static final Logger LOGGER = LogManager.getLogger("Dimension Hopper Twinkies");

    public static ResourceLocation res(String path) {
        return new ResourceLocation(MODID,path);
    }
}
