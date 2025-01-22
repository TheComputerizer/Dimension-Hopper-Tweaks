package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla_access;

import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModifiableAttributeInstance.class)
public interface ModifiableAttributeInstanceAccess {

    @Accessor double getCachedValue();
}