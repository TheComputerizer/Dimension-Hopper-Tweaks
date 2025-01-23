package mods.thecomputerizer.dimhoppertweaks.mixin.mods.botania;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntityPixie;
import net.minecraft.entity.EntityFlying;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import vazkii.botania.common.entity.EntityPixie;

import java.util.Objects;

import static vazkii.botania.common.Botania.proxy;

@Mixin(value = EntityPixie.class, remap = false)
public abstract class MixinEntityPixie extends EntityFlying implements IEntityPixie {

    @Unique private boolean dimhoppertweaks$bypassesTarget = false;
    @Shadow public abstract int getType();

    public MixinEntityPixie(World world) {
        super(world);
    }

    @Override
    public void dimhoppertweaks$setBypassesTarget(boolean bypassesTarget) {
        this.dimhoppertweaks$bypassesTarget = bypassesTarget;
    }

    /**
     * @author The_Computerizer
     * @reason Custom Pixie interactions
     */
    @Overwrite
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(!this.world.isRemote && ((Objects.isNull(this.getAttackTarget()) &&
                !this.dimhoppertweaks$bypassesTarget) || this.ticksExisted > 200)) this.setDead();
        boolean dark = this.getType()==1;
        if(this.world.isRemote)
            for(int i=0;i<4;i++)
                proxy.sparkleFX(this.posX+(Math.random()-0.5d)*0.25d,
                        this.posY+0.5d+(Math.random()-0.5d)*0.25d,this.posZ+(Math.random()-0.5d)*0.25d,
                        dark ? 0.1f : 1f,dark ? 0.025f : 0.25f,dark ? 0.09f : 0.9f,
                        0.1f+(float)Math.random()*0.25f,12);
    }
}