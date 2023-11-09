package mods.thecomputerizer.dimhoppertweaks.common.skills.farming;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.base.LevelLockHandler;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.itemstages.ItemStages;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

public class HungryFarmer extends ExtendedEventsTrait {

    public HungryFarmer() {
        super("hungry_farmer",2,3,FARMING,20,"farming|64");
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if(Objects.nonNull(player) && !player.isCreative() && !player.isSpectator()) {
            PlayerData data = PlayerDataHandler.get(player);
            if(Objects.nonNull(data) && data.getSkillInfo(this.getParentSkill()).isUnlocked(this)) {
                if(player.canEat(false)) {
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
    }

    private boolean canUseItem(EntityPlayer player, ItemStack stack) {
        Item item = stack.getItem();
        if(stack.isEmpty() || !(item instanceof ItemFood)) return false;
        return LevelLockHandler.canPlayerUseItem(player,stack) &&
                GameStageHelper.hasStage(player,ItemStages.getStage(stack)) && isItemWhitelisted(player,item);
    }

    private boolean isItemWhitelisted(EntityPlayer player, Item item) {
        ISkillCapability cap = SkillWrapper.getSkillCapability(player);
        return Objects.nonNull(cap) && cap.canAutoFeed(item);
    }
}
