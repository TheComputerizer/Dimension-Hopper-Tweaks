package mods.thecomputerizer.dimhoppertweaks.registry.traits.farming;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.base.LevelLockHandler;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.capability.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.itemstages.ItemStages;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

public class PotionMaster extends ExtendedEventsTrait {

    public PotionMaster() {
        super("potion_master",2,0,FARMING,32,"farming|128","magic|96");
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if(Objects.nonNull(player) && !player.isCreative() && !player.isSpectator()) {
            PlayerData data = PlayerDataHandler.get(player);
            if(Objects.nonNull(data) && data.getSkillInfo(this.getParentSkill()).isUnlocked(this)) {
                NonNullList<ItemStack> inventoryList = player.inventoryContainer.getInventory();
                ItemStack curStack = ItemStack.EMPTY;
                for(ItemStack stack : inventoryList) {
                    if(canUseItem(player,stack)) {
                        curStack = stack;
                        break;
                    }
                }
                if(!curStack.isEmpty()) curStack.getItem().onItemUseFinish(curStack,player.world,player);
            }
        }
    }

    private boolean canUseItem(EntityPlayer player, ItemStack stack) {
        if(stack.isEmpty() || !(stack.getItem() instanceof ItemPotion)) return false;
        String stage = ItemStages.getStage(stack);
        return LevelLockHandler.canPlayerUseItem(player,stack) && isItemWhitelisted(player,stack) &&
                (Objects.isNull(stage) || GameStageHelper.hasStage(player,stage));
    }

    private boolean isItemWhitelisted(EntityPlayer player, ItemStack potionStack) {
        ISkillCapability cap = SkillWrapper.getSkillCapability(player);
        return Objects.nonNull(cap) && cap.canAutoDrink((EntityPlayerMP)player,potionStack);
    }

    @Override
    public void onShiftRightClickPotion(EntityPlayer player, ItemStack potionStack) {
        ISkillCapability cap = SkillWrapper.getSkillCapability(player);
        if(Objects.nonNull(cap)) cap.togglePassivePotion((EntityPlayerMP)player,potionStack);
    }
}
