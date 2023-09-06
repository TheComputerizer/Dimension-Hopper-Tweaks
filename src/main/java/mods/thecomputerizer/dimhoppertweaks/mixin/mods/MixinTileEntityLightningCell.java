package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import sblectric.lightningcraft.tiles.TileEntityBase;
import sblectric.lightningcraft.tiles.TileEntityLightningCell;
import sblectric.lightningcraft.util.Effect;
import sblectric.lightningcraft.util.WeatherUtils;

import java.util.List;

@Mixin(value = TileEntityLightningCell.class, remap = false)
public abstract class MixinTileEntityLightningCell extends TileEntityBase {

    @Shadow public abstract boolean isAirTerminalPresent();

    @Shadow public double storedPower;

    @Shadow public double maxPower;

    @Shadow public boolean creative;

    @Shadow public int cooldownTime;

    @Shadow public double efficiency;

    /**
     * @author The_Computerizer
     * @reason Fix LE power not being able to get to the maximum value
     */
    @Overwrite
    public void update() {
        boolean dosave = false;
        if (this.world.isRemote) this.isAirTerminalPresent();
        else {
            if (this.storedPower > this.maxPower)
                this.storedPower = this.maxPower;
            if (this.storedPower < 0)
                this.storedPower = 0;
            if (this.creative)
                this.storedPower = this.maxPower;
            if (this.cooldownTime > 0) {
                --this.cooldownTime;
                dosave = true;
            }
            if (this.isAirTerminalPresent() && this.cooldownTime<=0) {
                double chance = 1.0E-5;
                if (this.world.isThundering()) chance = 0.001;
                if (this.random.nextDouble() <= chance && this.storedPower < this.maxPower)
                    Effect.lightningGen(this.world, this.pos.up());
                AxisAlignedBB box = new AxisAlignedBB(this.getX()-5d,this.getY()-2d,this.getZ()-5d,
                        this.getX()+5d,this.getY()+4d,this.getZ()+5d);
                List<EntityLightningBolt> bolts = WeatherUtils.getLightningBoltsWithinAABB(this.world,box);
                if (!bolts.isEmpty() && this.storedPower < this.maxPower) {
                    for (EntityLightningBolt bolt : bolts) {
                        if (bolt.isDead) return;
                        this.world.removeEntity(bolt);
                    }
                    this.storedPower = Math.min(this.maxPower,this.storedPower+100d*this.efficiency);
                    this.cooldownTime = 20;
                    dosave = true;
                }
            }
            if (dosave) this.markDirty();
        }
    }
}
