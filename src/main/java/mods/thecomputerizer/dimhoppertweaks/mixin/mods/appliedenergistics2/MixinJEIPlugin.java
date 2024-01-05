package mods.thecomputerizer.dimhoppertweaks.mixin.mods.appliedenergistics2;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import appeng.integration.modules.jei.JEIPlugin;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

@Mixin(value = JEIPlugin.class, remap = false)
public abstract class MixinJEIPlugin implements IModPlugin {

    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
        DHTRef.LOGGER.error("Is this even getting run?");
    }
}
