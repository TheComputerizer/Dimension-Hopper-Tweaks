package mods.thecomputerizer.dimhoppertweaks.mixin.mods.botania;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.common.item.relic.ItemDice;
import vazkii.botania.common.item.relic.ItemRelic;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
@Mixin(value = ItemDice.class, remap = false)
public abstract class MixinItemDice extends ItemRelic {

    @Shadow public static ItemStack[] relicStacks;

    public MixinItemDice(String name) {
        super(name);
    }

    /**
     * @author The_Computerizer
     * @reason Allow for relic dupicates
     */
    @Overwrite
    public @Nonnull ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(this.isRightPlayer(player,stack)) {
            if(world.isRemote) return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
            else {
                world.playSound(null,player.posX,player.posY,player.posZ,SoundEvents.ENTITY_ARROW_SHOOT,
                        SoundCategory.PLAYERS,0.5f,0.4f/(world.rand.nextFloat()*0.4f+0.8f));
                List<Integer> possible = Arrays.asList(0,1,2,3,4,5);
                int relic = possible.get(world.rand.nextInt(possible.size()));
                player.sendMessage((new TextComponentTranslation("botaniamisc.diceRoll",relic+1))
                        .setStyle((new Style()).setColor(TextFormatting.DARK_GREEN)));
                return ActionResult.newResult(EnumActionResult.SUCCESS,relicStacks[relic].copy());
            }
        } else return ActionResult.newResult(EnumActionResult.PASS,stack);
    }
}