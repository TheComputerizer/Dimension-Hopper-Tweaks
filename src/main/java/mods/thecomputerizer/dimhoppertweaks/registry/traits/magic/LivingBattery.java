package mods.thecomputerizer.dimhoppertweaks.registry.traits.magic;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import vazkii.botania.common.item.ItemManaTablet;
import xyz.phanta.psicosts.capability.PsiCell;
import xyz.phanta.psicosts.init.PsioCaps;

import java.util.Objects;

import static vazkii.botania.common.item.ModItems.manaTablet;
import static xyz.phanta.psicosts.init.PsioItems.PSI_CELL;

public class LivingBattery extends ExtendedEventsTrait {

    public LivingBattery() {
        super("living_battery",1,3,MAGIC,120,"magic|256","void|128","research|64","defense|32");
        setIcon("psicosts","items","creative_cell");
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent ev) {
        EntityPlayer player = ev.player;
        if(!player.world.isRemote) {
            int amount = player.isSneaking() ? 100 : 10;
            if(hasEnoughXP(player,amount)) {
                ItemStack mainStack = player.getHeldItemMainhand();
                if(!tryFillingPSI(mainStack,player,amount)) tryFillingMana(mainStack,player,amount);
                ItemStack offStack = player.getHeldItemOffhand();
                if(!tryFillingPSI(offStack,player,amount)) tryFillingMana(offStack,player,amount);
            }
        }
    }

    private boolean hasEnoughXP(EntityPlayer player, int drainAmount) {
        int totalXP = 0;
        for(int l=1; l<player.experienceLevel; l++) {
            totalXP+=getXPBarCap(l);
            if(totalXP>=drainAmount) return true;
        }
        return (totalXP+(int)(player.experience*(float)getXPBarCap(player.experienceLevel)))>=drainAmount;
    }
    
    private int getXPBarCap(int level) {
        return level>=30 ? 112+(level-30)*9 : (level>=15 ? 37+(level-15)*5 : 7+level*2);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean tryFillingPSI(ItemStack stack, EntityPlayer player, int amount) {
        if(stack.isEmpty() || stack.getItem()!=PSI_CELL) return false;
        PsiCell cell = stack.getCapability(PsioCaps.PSI_CELL,null);
        if(Objects.isNull(cell)) return false;
        if(cell.getPercentCharge()<1f) {
            cell.injectCharge(amount);
            player.addExperience(-amount);
        }
        return true;
    }

    private void tryFillingMana(ItemStack stack, EntityPlayer player, int amount) {
        if(stack.isEmpty() || stack.getItem()!=manaTablet) return;
        ItemManaTablet tablet = (ItemManaTablet)stack.getItem();
        if(tablet.getMana(stack)<tablet.getMaxMana(stack)) {
            tablet.addMana(stack,amount);
            player.addExperience(-amount);
        }
    }
}
