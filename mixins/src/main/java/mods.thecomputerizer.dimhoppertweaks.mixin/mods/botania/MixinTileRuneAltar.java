package mods.thecomputerizer.dimhoppertweaks.mixin.mods.botania;

import mods.thecomputerizer.dimhoppertweaks.registry.items.RecipeFunction;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;

import java.util.List;
import java.util.Objects;

import static net.minecraft.item.ItemStack.EMPTY;
import static vazkii.botania.common.block.ModBlocks.livingrock;
import static vazkii.botania.common.block.ModBlocks.runeAltar;
import static vazkii.botania.common.item.ModItems.rune;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(value = TileRuneAltar.class, remap = false)
public abstract class MixinTileRuneAltar extends TileSimpleInventory {

    @Shadow RecipeRuneAltar currentRecipe;
    @Shadow public int manaToGet;
    @Shadow int mana;
    @Shadow List<ItemStack> lastRecipe;
    @Shadow public abstract void recieveMana(int mana);
    @Shadow public abstract void saveLastRecipe();

    /**
     * @author The_Computerizer
     * @reason Add Recipe Function support
     */
    @Overwrite
    public void onWanded(EntityPlayer player, ItemStack wand) {
        if(!this.world.isRemote) {
            RecipeRuneAltar recipe = null;
            if(Objects.nonNull(this.currentRecipe)) recipe = this.currentRecipe;
            else {
                for(RecipeRuneAltar recipe_ : BotaniaAPI.runeAltarRecipes) {
                    if (recipe_.matches(this.itemHandler)) {
                        recipe = recipe_;
                        break;
                    }
                }
            }
            if(this.manaToGet>0 && this.mana>=this.manaToGet) {
                List<EntityItem> items = this.world.getEntitiesWithinAABB(EntityItem.class,new AxisAlignedBB(this.pos,this.pos.add(1,1,1)));
                EntityItem rock = null;
                for(EntityItem item : items) {
                    if(!item.isDead && !item.getItem().isEmpty() && item.getItem().getItem()==Item.getItemFromBlock(livingrock)) {
                        rock = item;
                        break;
                    }
                }
                if(Objects.nonNull(rock)) {
                    int mana = recipe.getManaUsage();
                    this.recieveMana(-mana);
                    ItemStack output = recipe.getOutput().copy();
                    this.saveLastRecipe();
                    if(output.getItem() instanceof RecipeFunction) {
                        RecipeFunction item = (RecipeFunction)output.getItem();
                        item.addInputs(output,this.lastRecipe);
                        output = item.transformStack(output);
                    }
                    EntityItem outputItem = new EntityItem(this.world,(double)this.pos.getX()+0.5d,
                            (double)this.pos.getY()+1.5d,(double)this.pos.getZ()+0.5d,output);
                    this.world.spawnEntity(outputItem);
                    this.currentRecipe = null;
                    this.world.addBlockEvent(this.getPos(),runeAltar,1,60);
                    this.world.addBlockEvent(this.getPos(),runeAltar,2,0);
                    for(int i=0; i<this.getSizeInventory(); i++) {
                        ItemStack stack = this.itemHandler.getStackInSlot(i);
                        if(!stack.isEmpty()) {
                            if(stack.getItem()==rune && (Objects.isNull(player) || !player.capabilities.isCreativeMode)) {
                                EntityItem outputRune = new EntityItem(this.world,(double)this.getPos().getX()+0.5,
                                        (double)this.getPos().getY()+1.5,(double)this.getPos().getZ()+0.5,stack.copy());
                                this.world.spawnEntity(outputRune);
                            }
                            this.itemHandler.setStackInSlot(i,EMPTY);
                        }
                    }
                    rock.getItem().shrink(1);
                }
            }
        }
    }
}