package mods.thecomputerizer.dimhoppertweaks.common.skills.magic;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ModItems;
import xyz.phanta.psicosts.capability.PsiCell;
import xyz.phanta.psicosts.init.PsioCaps;
import xyz.phanta.psicosts.init.PsioItems;

import java.util.Objects;

public class LivingBattery extends ExtendedEventsTrait {

    public LivingBattery() {
        super("living_battery",1,3,MAGIC,120,"magic|256","void|128","research|64","defense|32");
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent ev) {
        EntityPlayer player = ev.player;
        int amount = player.isSneaking() ? 100 : 10;
        if(!player.world.isRemote && player.experienceTotal>=amount) {
            ItemStack mainStack = player.getHeldItemMainhand();
            if(!tryFillingPSI(mainStack,player,amount)) tryFillingMana(mainStack,player,amount);
            ItemStack offStack = player.getHeldItemOffhand();
            if(!tryFillingPSI(offStack,player,amount)) tryFillingMana(offStack,player,amount);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean tryFillingPSI(ItemStack stack, EntityPlayer player, int amount) {
        if(stack.isEmpty() || stack.getItem()!=PsioItems.PSI_CELL) return false;
        PsiCell cell = stack.getCapability(PsioCaps.PSI_CELL,null);
        if(Objects.isNull(cell)) return false;
        if(cell.getPercentCharge()<1f) {
            cell.injectCharge(amount);
            player.addExperience(-amount);
        }
        return true;
    }

    private void tryFillingMana(ItemStack stack, EntityPlayer player, int amount) {
        if(stack.isEmpty() || stack.getItem()!=ModItems.manaTablet) return;
        ItemManaTablet tablet = (ItemManaTablet)stack.getItem();
        if(tablet.getMana(stack)<tablet.getMaxMana(stack)) {
            tablet.addMana(stack,amount);
            player.addExperience(-amount);
        }
    }
}
