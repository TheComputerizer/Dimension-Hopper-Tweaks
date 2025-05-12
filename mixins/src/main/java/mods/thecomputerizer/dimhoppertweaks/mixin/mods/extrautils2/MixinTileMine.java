package mods.thecomputerizer.dimhoppertweaks.mixin.mods.extrautils2;

import com.mojang.authlib.GameProfile;
import com.rwtema.extrautils2.fakeplayer.XUFakePlayer;
import com.rwtema.extrautils2.tile.TileAdvInteractor;
import com.rwtema.extrautils2.tile.TileMine;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static de.ellpeck.actuallyadditions.mod.items.InitItems.itemPaxelCrystalBlue;

@Mixin(value = TileMine.class, remap = false)
public abstract class MixinTileMine extends TileAdvInteractor {

    @Redirect(at = @At(value = "NEW", target = "(Lnet/minecraft/item/Item;I)Lnet/minecraft/item/ItemStack;"),
            method = "<clinit>")
    private static ItemStack dimhoppertweaks$reassignMiningTool(Item item, int amount) {
        return new ItemStack(itemPaxelCrystalBlue,amount);
    }
    
    @Redirect(at = @At(value = "NEW", target ="(Lnet/minecraft/world/WorldServer;Lcom/mojang/authlib/GameProfile;"+
            "Ljava/lang/String;)Lcom/rwtema/extrautils2/fakeplayer/XUFakePlayer;"), method = "operate")
    private XUFakePlayer dimhoppertweaks$assignGameStages(WorldServer world, GameProfile owner, String type) {
        XUFakePlayer player = new XUFakePlayer(world,owner,type);
        DelayedModAccess.setGameStages(player,DelayedModAccess.getTileStages(world,this.pos));
        return player;
    }
}