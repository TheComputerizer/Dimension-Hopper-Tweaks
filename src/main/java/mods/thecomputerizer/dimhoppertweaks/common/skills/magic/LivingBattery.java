package mods.thecomputerizer.dimhoppertweaks.common.skills.magic;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ModItems;
import xyz.phanta.psicosts.capability.PsiCell;
import xyz.phanta.psicosts.init.PsioCaps;
import xyz.phanta.psicosts.init.PsioItems;

import java.util.Objects;

public class LivingBattery extends ExtendedEventsTrait {

    public LivingBattery() {
        super(Constants.res("living_battery"),1,3,new ResourceLocation("reskillable","magic"),
                120,"reskillable:magic|256","dimhoppertweaks:void|128","dimhoppertweaks:research|64",
                "reskillable:defense|32");
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent ev) {
        EntityPlayer player = ev.player;
        if(!player.world.isRemote) {
            ItemStack mainStack = player.getHeldItemMainhand();
            if(!tryFillingPSI(mainStack,player)) tryFillingMana(mainStack,player);
            ItemStack offStack = player.getHeldItemOffhand();
            if(!tryFillingPSI(offStack,player)) tryFillingMana(offStack,player);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean tryFillingPSI(ItemStack stack, EntityPlayer player) {
        if(stack.isEmpty() || stack.getItem()!=PsioItems.PSI_CELL) return false;
        PsiCell cell = stack.getCapability(PsioCaps.PSI_CELL,null);
        if(Objects.isNull(cell)) return false;
        if(cell.getPercentCharge()<1f && player.experienceTotal>0) {
            int amount = player.isSneaking() ? 100 : 10;
            cell.injectCharge(amount);
            player.addExperience(-amount);
        }
        return true;
    }

    private void tryFillingMana(ItemStack stack, EntityPlayer player) {
        if(stack.isEmpty() || stack.getItem()!=ModItems.manaTablet) return;
        ItemManaTablet tablet = (ItemManaTablet)stack.getItem();
        if(tablet.getMana(stack)<tablet.getMaxMana(stack)) {
            int amount = player.isSneaking() ? 100 : 10;
            tablet.addMana(stack,amount);
            player.addExperience(-amount);
        }
    }
}
