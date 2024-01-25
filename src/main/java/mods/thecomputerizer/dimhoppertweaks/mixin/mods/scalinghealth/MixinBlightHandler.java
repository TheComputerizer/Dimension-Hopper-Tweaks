package mods.thecomputerizer.dimhoppertweaks.mixin.mods.scalinghealth;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.silentchaos512.scalinghealth.entity.EntityBlightFire;
import net.silentchaos512.scalinghealth.event.BlightHandler;
import net.silentchaos512.scalinghealth.network.NetworkHandler;
import net.silentchaos512.scalinghealth.network.message.MessageMarkBlight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(value = BlightHandler.class, remap = false)
public abstract class MixinBlightHandler {

    @Shadow
    static void applyBlightPotionEffects(EntityLivingBase entity) {}

    /**
     * @author The_Computerizer
     * @reason Blight fire improvements
     */
    @Overwrite
    public static boolean isBlight(EntityLivingBase entity) {
        return Objects.nonNull(entity) && ((IEntityLivingBase)entity).dimhoppertweaks$isBlighted();
    }

    /**
     * @author The_Computerizer
     * @reason Blight fire improvements
     */
    @Overwrite
    public static void markBlight(EntityLivingBase entity, boolean isBlight) {
        if(Objects.nonNull(entity)) {
            entity.getEntityData().setBoolean("ScalingHealth.IsBlight",isBlight);
            ((IEntityLivingBase)entity).dimhoppertweaks$setBlight(isBlight);
        }
    }

    /**
     * @author The_Computerizer
     * @reason Blight fire improvements
     */
    @Overwrite
    static void spawnBlightFire(EntityLivingBase blight) {}

    /**
     * @author The_Computerizer
     * @reason Blight fire improvements
     */
    @Overwrite
    private static @Nullable EntityBlightFire getBlightFire(EntityLivingBase blight) {
        return null;
    }

    /**
     * @author The_Computerizer
     * @reason Blight fire improvements
     */
    @Overwrite
    @SubscribeEvent
    public void onBlightUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if(Objects.nonNull(entity) && !entity.world.isRemote && isBlight(entity)) {
            World world = entity.world;
            if((world.getTotalWorldTime()+(long)entity.getEntityId())%200L==0L) {
                MessageMarkBlight message = new MessageMarkBlight(entity,true);
                NetworkHandler.INSTANCE.sendToAllAround(message,new NetworkRegistry.TargetPoint(entity.dimension,
                        entity.posX,entity.posY,entity.posZ,128d));
                applyBlightPotionEffects(entity);
            }
        }
    }
}