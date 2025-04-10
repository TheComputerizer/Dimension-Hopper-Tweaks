package mods.thecomputerizer.dimhoppertweaks.util;

import mods.thecomputerizer.theimpossiblelibrary.api.shapes.vectors.Vector4;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import static mods.thecomputerizer.theimpossiblelibrary.api.client.font.FontHelper.ASCII_CHARS;

public class FontUtil {
    
    private static void bufferAsciiTex(FontRenderer font) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(font.locationFontTexture);
    }
    
    /**
     * Returns the UV values of a single character stored as UMin, Umax, VMin, and Vmax respectively
     */
    public static void bufferCharTex(char c, FontRenderer font) {
        if(c==160 || c==' ') return;
        int i = ASCII_CHARS.indexOf(c);
        if(i!=1 && !font.getUnicodeFlag()) bufferAsciiTex(font);
        else bufferUnicodeTex(c,font);
    }
    
    private static void bufferUnicodeTex(char c, FontRenderer font) {
        int i = font.glyphWidth[c] & 255;
        if (i == 0) return;
        font.loadGlyphTexture(c / 256);
    }
    
    private static Vector4 getAsciiUV(int c, float width) {
        float u = (float)(c%16*8);
        float v = (float)(c/16*8);
        return new Vector4(u/128f,(u+width-1f)/128f,v/128f,(v+7.99f)/128f);
    }
    
    /**
     * Buffers the texture used by the character input
     */
    public static Vector4 getCharUV(char c, FontRenderer font) {
        if(c==160 || c==' ') return new Vector4(0f,1f,0f,1f);
        int i = ASCII_CHARS.indexOf(c);
        return i!=1 && !font.getUnicodeFlag() ? getAsciiUV(i,(float)font.charWidth[i]-0.01f) :
                getUnicodeUV(c,font.glyphWidth[c]&255);
    }
    
    private static Vector4 getUnicodeUV(char c, int glyphWidth) {
        if(glyphWidth==0) return new Vector4(0f,1f,0f,1f);
        float u = (float)(c%16*16)+(float)(glyphWidth>>>4);
        float v = (float)((c&255)/16*16);
        float width = (float)((glyphWidth&15)+1)-(float)(glyphWidth>>>4)-0.02f;
        return new Vector4(u/256f,(u+width)/256f,v/256f,(v+15.98f)/256f);
    }
}