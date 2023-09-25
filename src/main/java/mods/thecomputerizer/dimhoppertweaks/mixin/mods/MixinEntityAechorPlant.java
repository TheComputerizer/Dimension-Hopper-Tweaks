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

    @Shadow protected abstract int getPetalCountInState(boolean state);

    @Shadow private boolean[] petals;

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
                        int petalIndex = this.getRandomPetal(true);
                        if(petalIndex>=0) this.setPetalState(petalIndex,false);
                        --remainingPetals;
                    }
                }
            }
        }
    }

    /**
     * @author The_Computerizer
     * @reason Fix negative rand issues
     */
    @Overwrite
    private int getRandomPetal(boolean state) {
        int total = this.getPetalCountInState(state);
        if(total<=0) return -1;
        int nth = this.rand.nextInt(total);
        int selected = -1;
        int i = 0;
        for(int k=0; i<this.petals.length; ++i) {
            boolean present = this.petals[i];
            if(present == state) {
                if(k==nth) {
                    selected = i;
                    break;
                }
                ++k;
            }
        }
        return selected;
    }
}
