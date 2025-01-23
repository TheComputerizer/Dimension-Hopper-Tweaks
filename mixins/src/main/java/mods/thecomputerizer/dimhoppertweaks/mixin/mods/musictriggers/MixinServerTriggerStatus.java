package mods.thecomputerizer.dimhoppertweaks.mixin.mods.musictriggers;

import mods.thecomputerizer.musictriggers.server.channels.ServerTriggerStatus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.BossInfoServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(value = ServerTriggerStatus.class, remap = false)
public abstract class MixinServerTriggerStatus {
    
    @Shadow @Final private static Map<String,ServerTriggerStatus> SERVER_DATA;
    
    @Shadow protected abstract void runChecks();
    
    /**
     * @author The_Computerizer
     * @reason Remove the lag generator
     */
    @Overwrite
    public static void bossBarInstantiated(BossInfoServer info, Class<? extends EntityLivingBase> entityClass) {}
    
  
    @Redirect(at = @At(value="INVOKE", target="Ljava/util/Set;removeIf(Ljava/util/function/Predicate;)Z"),
            method = "runServerChecks")
    private static boolean runServerChecks(Set<?> instance, Predicate<?> predicate) {
        return true;
    }
}