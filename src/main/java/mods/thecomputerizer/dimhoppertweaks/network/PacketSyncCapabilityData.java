package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

public class PacketSyncCapabilityData extends MessageImpl {

    private float miningSpeed;
    private Set<Item> autoFeedItems;
    private List<Tuple<Potion,Integer>> autoPotionItems;
    public PacketSyncCapabilityData() {}

    public PacketSyncCapabilityData(float miningSpeed, Set<Item> autoFeedItems, List<Tuple<Potion,Integer>> autoPotionItems) {
        this.miningSpeed = miningSpeed;
        this.autoFeedItems = autoFeedItems;
        this.autoPotionItems = autoPotionItems;
    }

    @Override
    public IMessage handle(MessageContext ctx) {
        ClientPacketHandlers.handleCapData(this.miningSpeed,this.autoFeedItems,this.autoPotionItems);
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
        this.autoPotionItems = readPotions(buf);
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

    public List<Tuple<Potion,Integer>> readPotions(ByteBuf buf) {
        List<Tuple<Potion,Integer>> potions = new ArrayList<>();
        int size = buf.readInt();
        while(size>0) {
            ResourceLocation potionRes = NetworkUtil.readResourceLocation(buf);
            if(ForgeRegistries.POTIONS.containsKey(potionRes)) {
                Potion potion = ForgeRegistries.POTIONS.getValue(potionRes);
                int amplifier = buf.readInt();
                if(Objects.nonNull(potion)) potions.add(new Tuple<>(potion,amplifier));
            }
            size--;
        }
        return potions;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(this.miningSpeed);
        writeItems(buf,this.autoFeedItems);
        writePotions(buf,this.autoPotionItems);
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

    private void writePotions(ByteBuf buf, List<Tuple<Potion,Integer>> potions) {
        int size = potions.size();
        for(Tuple<Potion,Integer> potionTuple : potions)
            if(Objects.isNull(potionTuple.getFirst().getRegistryName())) size--;
        buf.writeInt(size);
        for(Tuple<Potion,Integer> potionTuple : potions) {
            ResourceLocation potionRes = potionTuple.getFirst().getRegistryName();
            if(Objects.nonNull(potionRes)) {
                NetworkUtil.writeResourceLocation(buf,potionRes);
                buf.writeInt(potionTuple.getSecond());
            }
        }
    }
}
