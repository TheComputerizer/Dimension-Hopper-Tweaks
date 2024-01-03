package mods.thecomputerizer.dimhoppertweaks.mixin.mods.reskillable;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.client.gui.GuiSkills;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;

@Mixin(value = GuiSkills.class, remap = false)
public class MixinGuiSkills {

    @Shadow private List<Skill> enabledSkills;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void dimhoppertweaks$init(CallbackInfo ci) {
        this.enabledSkills.sort(Comparator.comparing(Skill::getName));
    }
}
