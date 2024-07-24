package mods.thecomputerizer.dimhoppertweaks.mixin.mods.access;

import com.kamefrede.rpsideas.items.components.ItemBioticSensor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(value = ItemBioticSensor.class, remap = false)
public interface ItemBioticSensorAccess {
    
    @Accessor("triggeredBioticsRemote")
    static Map<EntityPlayer,List<EntityLivingBase>> getTriggeredBioticsRemote() {
        throw new AssertionError();
    }
    
    @Accessor("triggeredBioticsNonRemote")
    static Map<EntityPlayer,List<EntityLivingBase>> getTriggeredBioticsNonRemote() {
        throw new AssertionError();
    }
}
