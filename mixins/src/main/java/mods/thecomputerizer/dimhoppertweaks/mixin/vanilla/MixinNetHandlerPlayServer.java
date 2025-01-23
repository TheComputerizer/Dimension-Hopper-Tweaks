package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static java.lang.Double.MAX_VALUE;

@Mixin(NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {
    
    @ModifyConstant(constant = @Constant(doubleValue = 0.0625d, ordinal = 1), method = "processVehicleMove")
    private double dimhoppertweaks$fixMovedWrongly1(double constant) {
        return MAX_VALUE;
    }
    
    @ModifyConstant(constant = @Constant(doubleValue = 0.0625d, ordinal = 1), method = "processPlayer")
    private double dimhoppertweaks$fixMovedWrongly2(double constant) {
        return MAX_VALUE;
    }
}