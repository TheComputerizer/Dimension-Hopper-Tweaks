package mods.thecomputerizer.dimhoppertweaks.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.dimhoppertweaks.IItemSupplier")
public interface IItemSupplier {

    @ZenMethod
    IItemStack[] get();
}
