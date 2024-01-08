package mods.thecomputerizer.dimhoppertweaks.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.dimhoppertweaks.IStringConsumer")
public interface IStringConsumer {

    @ZenMethod
    void accept(String str);
}
