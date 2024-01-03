package mods.thecomputerizer.dimhoppertweaks.client.render;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEffects {
    public static final ResourceLocation GRAYSCALE_SHADER = DHTRef.res("shaders/post/dynamic_color_overlay.json");
    public static float FOV_ADJUST = 0f;
    public static float COLOR_CORRECTION = 0f;
    public static float COLOR_CORRECTION_OVERRIDE = 0f;
    public static float SCREEN_SHAKE = 0f;
    public static float MINING_SPEED = 1f;

    public static float getFOVAdjustment(float fov) {
        return fov*((FOV_ADJUST/2f)+0.5f);
    }

    public static float getColorCorrection() {
        return 1f-(COLOR_CORRECTION_OVERRIDE>0 ? COLOR_CORRECTION_OVERRIDE : COLOR_CORRECTION);
    }

    public static boolean isScreenShaking() {
        return SCREEN_SHAKE>0;
    }

    public static float getScreenShake(boolean positive) {
        float factor = (1f-SCREEN_SHAKE)/2f;
        return positive ? factor : factor*-1;
    }
}