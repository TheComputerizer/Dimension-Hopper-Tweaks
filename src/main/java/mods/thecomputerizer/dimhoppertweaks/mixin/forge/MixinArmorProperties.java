package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
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
        if(DEBUG) System.out.println("Start: "+damage);
        double prestigeFactor = 0d;
        if(entity instanceof EntityPlayer)
            prestigeFactor = SkillWrapper.getPrestigeFactor((EntityPlayer)entity,"defense")-1d;
        double totalArmor = entity.getTotalArmorValue();
        double totalToughness = entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue();
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
                prop = new ArmorProperties(0,0,Integer.MAX_VALUE);
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
                        if(DEBUG) System.out.println("Item: "+stack+" Absorbed: "+absorb+" Damaged: "+itemDamage);
                        stack.damageItem(itemDamage, entity);
                    }
                    if(stack.isEmpty()) inventory.set(prop.Slot, ItemStack.EMPTY);
                }
            }
            damage -= (damage * ratio);
        }
        if(damage>0 && (totalArmor>0 || totalToughness>0)) {
            double armorDamage = Math.max(1f,damage/4f);
            for(int i=0; i<inventory.size(); i++) {
                if(inventory.get(i).getItem() instanceof ItemArmor) {
                    inventory.get(i).damageItem((int)armorDamage,entity);
                    if(inventory.get(i).getCount()==0) inventory.set(i,ItemStack.EMPTY);
                }
            }
            if(totalArmor>0d && prestigeFactor>0d)
                totalArmor = dimhopeprtweaks$adjustVal(prestigeFactor,totalArmor,0.25d);
            if(totalToughness>0d && prestigeFactor>0d)
                totalToughness = dimhopeprtweaks$adjustVal(prestigeFactor,totalToughness,0.1d);
            damage = CombatRules.getDamageAfterAbsorb((float)damage,(float)totalArmor,(float)totalToughness);
        }
        if(DEBUG) System.out.println("Return: "+(int)(damage)+" "+damage);
        return (float)(damage);
    }

    @Unique
    private static double dimhopeprtweaks$adjustVal(double prestigeFactor, double armorVal, double modifier) {
        return armorVal+(armorVal*prestigeFactor*modifier);
    }
}