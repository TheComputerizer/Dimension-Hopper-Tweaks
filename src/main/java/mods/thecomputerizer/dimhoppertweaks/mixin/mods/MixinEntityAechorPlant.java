package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.gildedgames.aether.api.registrar.ItemsAether;
import com.gildedgames.aether.common.entities.monsters.EntityAechorPlant;
import com.gildedgames.aether.common.entities.monsters.EntityAetherMob;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;

@Mixin(value = EntityAechorPlant.class, remap = false)
public abstract class MixinEntityAechorPlant extends EntityAetherMob {

    @Shadow private int petalGrowTimer;

    @Shadow protected abstract void setPetalState(int index, boolean state);

    @Shadow protected abstract int getRandomPetal(boolean state);

    @Shadow protected abstract int getPetalCountInState(boolean state);

    public MixinEntityAechorPlant(World world) {
        super(world);
    }

    /**
     * @author The_Computerizer
     * @reason Fix negative rand issues
     */
    @Overwrite
    protected void damageEntity(@Nonnull DamageSource aource, float amount) {
        float prevHealth = this.getHealth();
        super.damageEntity(aource,amount);
        if(this.getHealth() != prevHealth) {
            this.petalGrowTimer = 6000;
            if(!this.world.isRemote) {
                int targetPetals = (int)Math.floor(this.getHealth()/this.getMaxHealth()*10f);
                int remainingPetals = this.getPetalCountInState(true);
                if(remainingPetals>0) {
                    int damage = remainingPetals-targetPetals;
                    Block.spawnAsEntity(this.world,this.getPosition(),new ItemStack(ItemsAether.aechor_petal,damage/2));
                    while(remainingPetals > targetPetals) {
                        this.setPetalState(this.getRandomPetal(true),false);
                        --remainingPetals;
                    }
                }
            }
        }
    }
}
