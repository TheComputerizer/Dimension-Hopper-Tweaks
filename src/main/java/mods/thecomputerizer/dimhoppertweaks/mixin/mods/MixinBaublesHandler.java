package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.google.common.collect.Multimap;
import com.tmtravlr.qualitytools.QualityToolsHelper;
import com.tmtravlr.qualitytools.baubles.BaublesHandler;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(value = BaublesHandler.class, remap = false)
public abstract class MixinBaublesHandler {

    @Shadow private boolean baublesExists;

    @Shadow protected abstract ArrayList<String> getBaublesNamesForSlot(int slot);

    /**
     * @author The_Computerizer
     * @reason Fix null capabilities
     */
    @Overwrite
    public void addEquippedBaubles(EntityPlayer player, List<ItemStack> equipped) {
        if (this.baublesExists) {
            IBaublesItemHandler playerBaubles = BaublesApi.getBaublesHandler(player);
            if(Objects.nonNull(playerBaubles))
                for(int i = 0; i < playerBaubles.getSlots(); ++i)
                    equipped.add(playerBaubles.getStackInSlot(i));
        }
    }

    /**
     * @author The_Computerizer
     * @reason Fix null capabilities
     */
    @Overwrite
    public void applyAttributesFromBaubles(EntityPlayer player, Multimap<String, AttributeModifier> modifiersToRemove) {
        if (this.baublesExists) {
            IBaublesItemHandler baublesHandler = BaublesApi.getBaublesHandler(player);
            if(Objects.nonNull(baublesHandler))
                for(int i = 0; i < baublesHandler.getSlots(); ++i)
                    for(String slotName : this.getBaublesNamesForSlot(i))
                        QualityToolsHelper.applyAttributesForSlot(player,baublesHandler.getStackInSlot(i),slotName,modifiersToRemove);
        }

    }
}
