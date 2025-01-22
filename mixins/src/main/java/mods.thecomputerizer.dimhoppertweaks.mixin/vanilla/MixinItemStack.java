package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static java.util.concurrent.TimeUnit.SECONDS;
import static net.minecraft.inventory.EntityEquipmentSlot.MAINHAND;
import static net.minecraft.inventory.EntityEquipmentSlot.OFFHAND;
import static org.spongepowered.asm.mixin.injection.callback.LocalCapture.CAPTURE_FAILSOFT;

@SuppressWarnings("DataFlowIssue")
@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Unique
    private static final Cache<ItemStack,ImmutableMultimap<String,AttributeModifier>> dimhoppertweaks$CACHE =
            CacheBuilder.newBuilder().weakKeys().expireAfterAccess(1,SECONDS).build();

    @Shadow public abstract Item getItem();

    @Shadow public abstract boolean isEmpty();

    @Shadow public abstract int getItemDamage();

    @Shadow public abstract int getMaxDamage();

    @Inject(cancellable = true, at = @At("RETURN"), method = "getAttributeModifiers", locals = CAPTURE_FAILSOFT)
    private void utGetAttributeModifiers(
            EntityEquipmentSlot equipmentSlot, CallbackInfoReturnable<Multimap<String,AttributeModifier>> info,
            Multimap<String,AttributeModifier> m) {
        if(!this.isEmpty() && (equipmentSlot!=MAINHAND && equipmentSlot!=OFFHAND) && this.getItem().isDamageable()) {
            ImmutableMultimap<String,AttributeModifier> cached = dimhoppertweaks$CACHE.getIfPresent((ItemStack)(Object)this);
            if(Objects.nonNull(cached)) info.setReturnValue(cached);
            ImmutableMultimap.Builder<String,AttributeModifier> copy = ImmutableMultimap.builder();
            float retDegrade = (float)(((double)this.getMaxDamage()-(double)this.getItemDamage())/Math.max(this.getMaxDamage(),1d));
            for(String e : m.keySet())
                for(AttributeModifier eam : m.get(e)) {
                    AttributeModifier degradedEAM = new AttributeModifier(eam.getID(),eam.getName(),(retDegrade)*eam.getAmount(),eam.getOperation());
                    copy.put(e,degradedEAM);
                }
            cached = copy.build();
            dimhoppertweaks$CACHE.put((ItemStack)(Object)this,cached);
            info.setReturnValue(cached);
        }
    }
}