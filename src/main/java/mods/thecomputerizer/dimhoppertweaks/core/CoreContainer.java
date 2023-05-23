package mods.thecomputerizer.dimhoppertweaks.core;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class CoreContainer extends DummyModContainer {

    public CoreContainer() {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "dimhoppertweakscore";
        meta.name = "Dimension Hopper Twinkies";
        meta.description = "Loads Early Mixin Stuff for Dimension Hopper Tweaks";
        meta.version = Constants.VERSION;
        meta.authorList.add("The_Computerizer");
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}