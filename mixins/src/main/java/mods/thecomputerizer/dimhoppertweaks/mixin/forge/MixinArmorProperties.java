package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.System.out;
import static net.minecraft.entity.SharedMonsterAttributes.ARMOR_TOUGHNESS;
import static net.minecraft.item.ItemStack.EMPTY;

@Mixin(value = ArmorProperties.class, remap = false)
public abstract class MixinArmorProperties {

    @Shadow @Final private static boolean DEBUG;

    @Shadow
    private static void StandardizeList(ArmorProperties[] armor, double damage) {}

    /**
     * @author The_Computerizer
     * @reason Add defense skill passives
     */
    @Overwrite
    public static float applyArmor(EntityLivingBase entity, NonNullList<ItemStack> inventory, DamageSource source, double damage) {
        if(DEBUG) out.println("Start: "+damage);
        double prestigeFactor = 0d;
        if(entity instanceof EntityPlayer)
            prestigeFactor = SkillWrapper.getPrestigeFactor((EntityPlayer)entity,"defense")-1d;
        double totalArmor = entity.getTotalArmorValue();
        double totalToughness = entity.getEntityAttribute(ARMOR_TOUGHNESS).getAttributeValue();
        if(source.isUnblockable()) {
            totalArmor = 0;
            totalToughness = 0;
        }
        ArrayList<ArmorProperties> dmgVals = new ArrayList<>();
        for(int slot=0; slot<inventory.size(); slot++) {
            ItemStack stack = inventory.get(slot);
            if(stack.isEmpty()) continue;
            ArmorProperties prop = null;
            if(stack.getItem() instanceof ISpecialArmor) {
                if(!source.isUnblockable() || ((ISpecialArmor) stack.getItem()).handleUnblockableDamage(entity,stack,source,damage,slot)) {
                    ISpecialArmor armor = (ISpecialArmor)stack.getItem();
                    prop = armor.getProperties(entity,stack,source,damage,slot).copy();
                    totalArmor += prop.Armor;
                    totalToughness += prop.Toughness;
                }
            }
            else if(stack.getItem() instanceof ItemArmor && !source.isUnblockable()) {
                ItemArmor armor = (ItemArmor)stack.getItem();
                prop = new ArmorProperties(0,0,MAX_VALUE);
                prop.Armor = armor.damageReduceAmount;
                prop.Toughness = armor.toughness;
            }
            if(Objects.nonNull(prop)) {
                prop.Slot = slot;
                dmgVals.add(prop);
            }
        }
        if(!dmgVals.isEmpty()) {
            ArmorProperties[] props = dmgVals.toArray(new ArmorProperties[0]);
            StandardizeList(props,damage);
            int level = props[0].Priority;
            double ratio = 0;
            for(ArmorProperties prop : props) {
                if(level!=prop.Priority) {
                    damage -= (damage*ratio);
                    ratio = 0;
                    level = prop.Priority;
                }
                ratio += prop.AbsorbRatio;
                double absorb = damage*prop.AbsorbRatio;
                if(absorb>0) {
                    ItemStack stack = inventory.get(prop.Slot);
                    int itemDamage = (int)Math.max(1, absorb);
                    if(stack.getItem() instanceof ISpecialArmor)
                        ((ISpecialArmor)stack.getItem()).damageArmor(entity,stack,source,itemDamage,prop.Slot);
                    else {
                        if(DEBUG) out.println("Item: "+stack+" Absorbed: "+absorb+" Damaged: "+itemDamage);
                        stack.damageItem(itemDamage, entity);
                    }
                    if(stack.isEmpty()) inventory.set(prop.Slot,EMPTY);
                }
            }
            damage -= (damage * ratio);
        }
        if(damage>0 && (totalArmor>0 || totalToughness>0)) {
            double armorDamage = Math.max(1f,damage/4f);
            for(int i=0; i<inventory.size(); i++) {
                if(inventory.get(i).getItem() instanceof ItemArmor) {
                    inventory.get(i).damageItem((int)armorDamage,entity);
                    if(inventory.get(i).getCount()==0) inventory.set(i,EMPTY);
                }
            }
            if(prestigeFactor>0d)
                DHTConfigHelper.devInfo("ARMOR PROPERTIES: `BASE ARMOR {} | BASE TOUGHNESS {} | "+
                        "PRESTIGE FACTOR {}`", totalArmor,totalToughness,prestigeFactor);
            if(totalArmor>0d && prestigeFactor>0d)
                totalArmor = dimhoppertweaks$adjustVal(prestigeFactor, totalArmor);
            if(totalToughness>0d && prestigeFactor>0d)
                totalToughness = dimhoppertweaks$adjustVal(prestigeFactor, totalToughness);
            damage = CombatRules.getDamageAfterAbsorb((float)damage,(float)totalArmor,(float)totalToughness);
        }
        if(DEBUG) out.println("Return: "+(int)(damage)+" "+damage);
        return (float)(damage);
    }

    @Unique
    private static double dimhoppertweaks$adjustVal(double prestigeFactor, double armorVal) {
        return armorVal+(armorVal*prestigeFactor*0.1d);
    }
}