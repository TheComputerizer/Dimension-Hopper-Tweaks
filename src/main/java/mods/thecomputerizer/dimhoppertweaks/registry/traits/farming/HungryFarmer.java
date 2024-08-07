package mods.thecomputerizer.dimhoppertweaks.registry.traits.farming;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.base.LevelLockHandler;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.itemstages.ItemStages;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

import static net.minecraft.item.ItemStack.EMPTY;

public class HungryFarmer extends ExtendedEventsTrait {

    public HungryFarmer() {
        super("hungry_farmer",2,3,FARMING,20,"farming|64");
        setIcon("reskillable","unlockables","hungry_farmer");
    }

    @Override
    public void onFinishUsingItem(EntityPlayer player, ItemStack stack) {
        if(player.isSneaking() && stack.getItem() instanceof ItemFood) {
            ISkillCapability cap = SkillWrapper.getSkillCapability(player);
            if(Objects.nonNull(cap)) cap.togglePassiveFood((EntityPlayerMP)player,stack.getItem(),true);
        }
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if(Objects.nonNull(player) && !player.isCreative() && !player.isSpectator()) {
            PlayerData data = PlayerDataHandler.get(player);
            if(Objects.nonNull(data) && data.getSkillInfo(this.getParentSkill()).isUnlocked(this)) {
                if(player.canEat(false)) {
                    NonNullList<ItemStack> inventoryList = player.inventoryContainer.getInventory();
                    ItemStack curStack = EMPTY;
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
        String stage = ItemStages.getStage(stack);
        return LevelLockHandler.canPlayerUseItem(player,stack) && isItemWhitelisted(player,item) &&
                (Objects.isNull(stage) || GameStageHelper.hasStage(player,stage));
    }

    private boolean isItemWhitelisted(EntityPlayer player, Item item) {
        ISkillCapability cap = SkillWrapper.getSkillCapability(player);
        return Objects.nonNull(cap) && cap.canAutoFeed(item);
    }

    @Override
    public void onShiftRightClickFood(EntityPlayer player, ItemFood food) {
        ISkillCapability cap = SkillWrapper.getSkillCapability(player);
        if(Objects.nonNull(cap)) cap.togglePassiveFood((EntityPlayerMP)player,food,false);
    }
}
