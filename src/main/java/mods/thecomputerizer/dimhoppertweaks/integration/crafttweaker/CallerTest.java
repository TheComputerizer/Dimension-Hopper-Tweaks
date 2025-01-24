package mods.thecomputerizer.dimhoppertweaks.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.dimhoppertweaks.CallerTest")
public class CallerTest {
    
    @ZenMethod
    public static void acceptIngredientConsumer(IIngredientConsumer consumer, IIngredient ingr) {
        consumer.accept(ingr);
    }
    
    @ZenMethod
    public static void acceptIngredientArrayConsumer(IIngredientArrayConsumer consumer, IIngredient[] ingrs) {
        consumer.accept(ingrs);
    }
    
    @ZenMethod
    public static void acceptIngredientNumbersConsumer(IIngredientNumbersConsumer consumer, IIngredient[] ingrs, int[] ints) {
        consumer.accept(ingrs,ints);
    }

    @ZenMethod
    public static void acceptStackConsumer(IItemStackConsumer consumer, IItemStack stack) {
        consumer.accept(stack);
    }
    
    @ZenMethod
    public static void acceptStackArrayConsumer(IItemStackArrayConsumer consumer, IItemStack[] stacks) {
        consumer.accept(stacks);
    }

    @ZenMethod
    public static void acceptStringConsumer(IStringConsumer consumer, String str) {
        consumer.accept(str);
    }
}
