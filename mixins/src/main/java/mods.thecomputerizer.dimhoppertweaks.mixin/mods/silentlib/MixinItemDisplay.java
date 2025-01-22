package mods.thecomputerizer.dimhoppertweaks.mixin.mods.silentlib;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.lib.guidebook.GuideBook;
import net.silentchaos512.lib.guidebook.page.ItemDisplay;
import net.silentchaos512.lib.util.AssetUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

import static net.minecraft.item.ItemStack.EMPTY;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@Mixin(value = ItemDisplay.class, remap = false)
public class MixinItemDisplay {
    
    @Shadow @Final public int x;
    @Shadow @Final public int y;
    @Shadow @Final public float scale;
    @Shadow public ItemStack stack;
    
    /**
     * @author The_Computerizer
     * @reason Fix null crash
     */
    @Overwrite
    @SideOnly(CLIENT)
    public void drawPre(GuideBook book) {
        if(Objects.isNull(this.stack)) this.stack = EMPTY;
        AssetUtil.renderStackToGui(this.stack,this.x,this.y,this.scale);
    }
}