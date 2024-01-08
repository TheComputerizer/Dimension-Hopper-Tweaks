package mods.thecomputerizer.dimhoppertweaks.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.dimhoppertweaks.CallerTest")
public class CallerTest {

    @ZenMethod
    public static void acceptItemConsumer(IItemConsumer consumer, IItemStack items) {
        consumer.accept(items);
    }

    @ZenMethod
    public static void acceptStringConsumer(IStringConsumer consumer, String str) {
        consumer.accept(str);
    }
}
