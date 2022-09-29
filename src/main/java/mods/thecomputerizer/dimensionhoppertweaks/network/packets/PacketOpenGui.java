package mods.thecomputerizer.dimensionhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimensionhoppertweaks.network.gui.GuiType;
import mods.thecomputerizer.dimensionhoppertweaks.network.gui.IGuiConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

public class PacketOpenGui implements IMessageHandler<PacketOpenGui.PacketOpenGuiMessage, IMessage> {

    @Override
    public IMessage onMessage(PacketOpenGui.PacketOpenGuiMessage message, MessageContext ctx) {
        Minecraft.getMinecraft().displayGuiScreen(message.construct());
        return null;
    }

    public static class PacketOpenGuiMessage implements IMessage {

        private GuiType type;
        private IGuiConstructor constructor;

        public PacketOpenGuiMessage() {
        }

        public PacketOpenGuiMessage(GuiType type, IGuiConstructor constructor) {
            this.type = type;
            this.constructor = constructor;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            try {
                this.constructor = GuiType.get((String) buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8)).initializeConstructor(buf);
            } catch (Exception e) {
                throw new RuntimeException("Error initializing gui constructor",e);
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(this.type.getId().length());
            buf.writeCharSequence(this.type.getId(), StandardCharsets.UTF_8);
            this.constructor.encode(buf);
        }

        private GuiScreen construct() {
            return this.constructor.construct();
        }
    }
}
