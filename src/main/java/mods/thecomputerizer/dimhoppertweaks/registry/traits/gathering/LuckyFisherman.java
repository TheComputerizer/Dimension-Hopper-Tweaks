package mods.thecomputerizer.dimhoppertweaks.registry.traits.gathering;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import ovh.corail.tombstone.item.ItemFishingRodOfMisadventure;

import java.util.Objects;

public class LuckyFisherman extends ExtendedEventsTrait {

    public LuckyFisherman() {
        super("lucky_fisherman",3,2,GATHERING,6,"gathering|12","magic|6");
        setIcon(new ResourceLocation("reskillable","textures/unlockables/lucky_fisherman.png"));
    }

    @Override
    public void onPlayerTick(PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if(player instanceof EntityPlayerMP && Objects.nonNull(player.fishEntity)) {
            boolean hasBadLuck = hasBadLuck(player.getHeldItemMainhand()) || hasBadLuck(player.getHeldItemOffhand());
            Potion luckPotion = hasBadLuck ? MobEffects.UNLUCK : MobEffects.LUCK;
            int amplifier = Math.max(0,(int)(SkillWrapper.getPrestigeFactor(player,"gathering")-1d));
            player.addPotionEffect(new PotionEffect(luckPotion,10,amplifier,true,true));
        }
    }

    private boolean hasBadLuck(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemFishingRodOfMisadventure;
    }
}
