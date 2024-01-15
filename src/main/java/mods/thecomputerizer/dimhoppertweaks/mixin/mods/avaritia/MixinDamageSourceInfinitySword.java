package mods.thecomputerizer.dimhoppertweaks.mixin.mods.avaritia;

import morph.avaritia.util.DamageSourceInfinitySword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(value = DamageSourceInfinitySword.class, remap = false)
public abstract class MixinDamageSourceInfinitySword extends EntityDamageSource {

    public MixinDamageSourceInfinitySword(@Nullable Entity entity) {
        super("infinity",entity);
    }

    /**
     * @author The_Computerizer
     * @reason Handle null entity from the final boss kill command
     */
    @Overwrite
    public @Nonnull ITextComponent getDeathMessage(@Nullable EntityLivingBase entity) {
        ItemStack stack = this.damageSourceEntity instanceof EntityLivingBase ?
                ((EntityLivingBase)this.damageSourceEntity).getHeldItem(EnumHand.MAIN_HAND) : null;
        String s = "death.attack.infinity";
        if(Objects.nonNull(entity)) {
            int rando = entity.getEntityWorld().rand.nextInt(5);
            if(rando != 0) s = s+"."+rando;
        }
        return new TextComponentTranslation(s,Objects.nonNull(entity) ? entity.getDisplayName() : "UNKNOWN ENTITY",
                Objects.nonNull(stack) ? stack.getDisplayName() : "UNKNOWN ITEM");
    }
}