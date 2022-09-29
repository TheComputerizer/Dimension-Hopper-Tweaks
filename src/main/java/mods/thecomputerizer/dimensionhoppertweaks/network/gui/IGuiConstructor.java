package mods.thecomputerizer.dimensionhoppertweaks.network.gui;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;

public interface IGuiConstructor {
    void encode(ByteBuf buf);
    GuiScreen construct();
}
