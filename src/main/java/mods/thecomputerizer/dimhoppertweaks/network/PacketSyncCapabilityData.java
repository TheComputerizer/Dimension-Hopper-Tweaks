package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PacketSyncCapabilityData extends MessageImpl {

    private float miningSpeed;
    private Set<Item> autoFeedItems;

    public PacketSyncCapabilityData() {}

    public PacketSyncCapabilityData(float miningSpeed, Set<Item> autoFeedItems) {
        this.miningSpeed = miningSpeed;
        this.autoFeedItems = autoFeedItems;
    }

    @Override
    public IMessage handle(MessageContext ctx) {
        ClientPacketHandlers.handleCapData(this.miningSpeed,this.autoFeedItems);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.miningSpeed = buf.readFloat();
        this.autoFeedItems = readItems(buf);
    }

    public Set<Item> readItems(ByteBuf buf) {
        Set<Item> items = new HashSet<>();
        int size = buf.readInt();
        while(size>0) {
            ResourceLocation itemRes = NetworkUtil.readResourceLocation(buf);
            if(ForgeRegistries.ITEMS.containsKey(itemRes)) items.add(ForgeRegistries.ITEMS.getValue(itemRes));
            size--;
        }
        return items;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(this.miningSpeed);
        writeItems(buf,this.autoFeedItems);
    }

    private void writeItems(ByteBuf buf, Set<Item> itemSet) {
        int size = itemSet.size();
        for(Item item : itemSet)
            if(Objects.isNull(item.getRegistryName())) size--;
        buf.writeInt(size);
        for(Item item : itemSet) {
            ResourceLocation itemReg = item.getRegistryName();
            if(Objects.nonNull(itemReg)) NetworkUtil.writeResourceLocation(buf,itemReg);
        }
    }
}
