package mods.thecomputerizer.dimhoppertweaks.mixin.mods.artisanautomation;

import com.codetaylor.mc.artisanautomation.modules.automator.tile.automator.AutomatorCraftingContext;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingContext;
import mekanism.common.MekFakePlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;
import java.util.Optional;

@Mixin(value = AutomatorCraftingContext.class, remap = false)
public abstract class MixinAutomatorCraftingContext implements ICraftingContext {
    
    @Shadow @Final private World world;
    @Unique private EntityPlayer dimhoppertweaks$fakePlayer;
    
    @Override
    public Optional<EntityPlayer> getPlayer() {
        if(Objects.isNull(this.dimhoppertweaks$fakePlayer) && this.world instanceof WorldServer)
            this.dimhoppertweaks$fakePlayer = MekFakePlayer.getInstance((WorldServer)this.world).get();
        return Objects.nonNull(this.dimhoppertweaks$fakePlayer) ? Optional.of(this.dimhoppertweaks$fakePlayer) : Optional.empty();
    }
}
