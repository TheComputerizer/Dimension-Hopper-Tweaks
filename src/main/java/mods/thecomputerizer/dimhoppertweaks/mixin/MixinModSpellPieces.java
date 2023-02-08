package mods.thecomputerizer.dimhoppertweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.base.ModSpellPieces;
import vazkii.psi.common.spell.trick.block.PieceTrickSmeltBlock;
import vazkii.psi.common.spell.trick.entity.PieceTrickSmeltItem;

@Mixin(ModSpellPieces.class)
public class MixinModSpellPieces {

    @Inject(at = @At(value = "HEAD"), method = "register", cancellable = true)
    private static void dimhoppertweaks_register(Class<? extends SpellPiece> clazz, String name, String group, boolean main,
                                                 CallbackInfoReturnable<ModSpellPieces.PieceContainer> cir) {
        if(clazz==PieceTrickSmeltBlock.class || clazz==PieceTrickSmeltItem.class) cir.setReturnValue(null);
    }
}
