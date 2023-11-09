package mods.thecomputerizer.dimhoppertweaks.util;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SideOnly(Side.CLIENT)
public class TextUtil {

    public static final String DARK_RED = String.valueOf(TextFormatting.DARK_RED);
    public static final String DARK_GRAY = String.valueOf(TextFormatting.DARK_GRAY);
    public static final String RED = String.valueOf(TextFormatting.RED);
    public static final String WHITE = String.valueOf(TextFormatting.WHITE);
    public static final String ITALICS = String.valueOf(TextFormatting.ITALIC);

    public static String getTranslated(String langKey, Object ... args) {
        return new TextComponentTranslation(langKey,args).getUnformattedText();
    }
}
