package mods.thecomputerizer.dimhoppertweaks.client.render;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEffects {
    public static final ResourceLocation GRAYSCALE_SHADER = Constants.res("shaders/post/dynamic_color_overlay.json");
    private static float FOV_ADJUST = 0f;
    private static float COLOR_CORRECTION = 0f;
    private static float SCREEN_SHAKE = 0f;

    public static float getFOVAdjustment(float fov) {
        return fov*((FOV_ADJUST/2f)+0.5f);
    }

    public static float getColorCorrection() {
        return 1f-COLOR_CORRECTION;
    }

    public static boolean isScreenShaking() {
        return SCREEN_SHAKE>0;
    }

    public static float getScreenShake(boolean positive) {
        float factor = (1f-SCREEN_SHAKE)/2f;
        return positive ? factor : factor*-1;
    }
}