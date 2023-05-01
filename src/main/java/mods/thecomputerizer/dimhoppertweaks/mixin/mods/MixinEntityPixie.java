package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.EntityPixieAccess;
import net.minecraft.entity.EntityFlying;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityPixie;

import java.util.Objects;

@Mixin(EntityPixie.class)
public abstract class MixinEntityPixie extends EntityFlying implements EntityPixieAccess {

    @Shadow public abstract int getType();

    private boolean bypassesTarget = false;

    public MixinEntityPixie(World world) {
        super(world);
    }

    @Override
    public void setBypassesTarget(boolean bypassesTarget) {
        this.bypassesTarget = bypassesTarget;
    }

    @Overwrite
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (!this.world.isRemote && ((Objects.isNull(this.getAttackTarget()) && !this.bypassesTarget) || this.ticksExisted > 200)) {
            this.setDead();
        }

        boolean dark = this.getType() == 1;
        if (this.world.isRemote) {
            for(int i = 0; i < 4; ++i) {
                Botania.proxy.sparkleFX(this.posX + (Math.random() - 0.5) * 0.25, this.posY + 0.5 + (Math.random() - 0.5) * 0.25,
                        this.posZ + (Math.random() - 0.5) * 0.25, dark ? 0.1F : 1.0F, dark ? 0.025F : 0.25F, dark ? 0.09F : 0.9F, 0.1F + (float)Math.random() * 0.25F, 12);
            }
        }
    }
}
