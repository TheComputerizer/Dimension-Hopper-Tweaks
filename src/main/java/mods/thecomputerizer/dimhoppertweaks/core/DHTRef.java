package mods.thecomputerizer.dimhoppertweaks.core;

import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DHTRef {

    public static final float CHEST_REPLACEMENT_CHANCE = 0.02f;
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2860,);"+
            "required-after:actuallyadditions;"+
            "required-after:aoa3;"+
            "required-after:aquaculture;"+
            "required-after:avaritia;"+
            "required-after:betterquesting;"+
            "required-after:botania;"+
            "required-after:cavern;"+
            "required-after:compactmachines3;"+
            "required-after:dimdoors;"+
            "required-after:extendedcrafting;"+
            "required-after:extrautils2;"+
            "required-after:gamestages;"+
            "required-after:geckolib3;"+
            "required-after:infernalmobs;"+
            "required-after:itemstages;"+
            "required-after:jaopca;"+
            "required-after:lightningcraft;"+
            "required-after:lockyzextradimensionsmod;"+
            "required-after:loottweaker;"+
            "required-after:mekanism;"+
            "required-after:mist;"+
            "required-after:mobends;"+
            "required-after:nodami;"+
            "required-after:openblocks;"+
            "required-after:orestages;"+
            "required-after:overloaded;"+
            "required-after:packagedauto;"+
            "required-after:packagedexcrafting;"+
            "required-after:psi;"+
            "required-after:randomthings;"+
            "required-after:recipestages;"+
            "required-after:reskillable;"+
            "required-after:scalinghealth;"+
            "required-after:sgcraft;"+
            "required-after:silentgems;"+
            "required-after:silentlib;"+
            "required-after:scalinghealth;"+
            "required-after:tconstruct;"+
            "required-after:theimpossiblelibrary;"+
            "required-after:theoneprobe;"+
            "required-after:toolprogression;"+
            "required-after:tp;"+
            "required-after:twilightforest;"+
            "required-after:vintagefix;"+
            "required-after:xlfoodmod;"+
            "required-after:zollerngalaxy;";
    public static final Logger LOGGER = LogManager.getLogger("Dimension Hopper Twinkies");
    public static final String MODID = "dimhoppertweaks";
    public static final String NAME = "Dimension Hopper Tweaks";
    public static final String VERSION = "1.12.2-2.7.0";

    public static String modID(String str) {
        return String.format(str,MODID);
    }

    public static String[] modIDs(String ... strs) {
        for(int i=0; i<strs.length; i++)
            strs[i] = modID(strs[i]);
        return strs;
    }

    public static ResourceLocation res(String path) {
        return new ResourceLocation(MODID,path);
    }
}
