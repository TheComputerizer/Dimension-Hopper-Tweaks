package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.api.network.NetworkHelper;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

import static net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS;
import static net.minecraftforge.fml.common.registry.ForgeRegistries.POTIONS;

public class PacketSyncCapabilityData extends MessageAPI<MessageContext> {

    private final float miningSpeed;
    private final boolean skillKey;
    private final boolean resourcesKey;
    private final Set<Item> autoFeedItems;
    private final List<Entry<Potion,Integer>> autoPotionItems;

    public PacketSyncCapabilityData(float miningSpeed, boolean skillKey, boolean resourcesKey, Set<Item> autoFeedItems,
                                    List<Entry<Potion,Integer>> autoPotionItems) {
        this.miningSpeed = miningSpeed;
        this.skillKey = skillKey;
        this.resourcesKey = resourcesKey;
        this.autoFeedItems = autoFeedItems;
        this.autoPotionItems = autoPotionItems;
    }
    
    public PacketSyncCapabilityData(ByteBuf buf) {
        this.miningSpeed = buf.readFloat();
        this.skillKey = buf.readBoolean();
        this.resourcesKey = buf.readBoolean();
        this.autoFeedItems = readItems(buf);
        this.autoPotionItems = readPotions(buf);
    }

    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        ClientPacketHandlers.handleCapData(this.miningSpeed,this.skillKey,this.resourcesKey,this.autoFeedItems,
                this.autoPotionItems);
        return null;
    }

    public Set<Item> readItems(ByteBuf buf) {
        Set<Item> items = new HashSet<>();
        int size = buf.readInt();
        while(size>0) {
            ResourceLocation itemRes = new ResourceLocation(NetworkHelper.readString(buf));
            if(ITEMS.containsKey(itemRes)) items.add(ITEMS.getValue(itemRes));
            size--;
        }
        return items;
    }

    public List<Entry<Potion,Integer>> readPotions(ByteBuf buf) {
        List<Entry<Potion,Integer>> potions = new ArrayList<>();
        int size = buf.readInt();
        while(size>0) {
            ResourceLocation potionRes = new ResourceLocation(NetworkHelper.readString(buf));
            if(POTIONS.containsKey(potionRes)) {
                Potion potion = POTIONS.getValue(potionRes);
                int amplifier = buf.readInt();
                if(Objects.nonNull(potion)) potions.add(new SimpleImmutableEntry<>(potion,amplifier));
            }
            size--;
        }
        return potions;
    }

    @Override public void encode(ByteBuf buf) {
        buf.writeFloat(this.miningSpeed);
        buf.writeBoolean(this.skillKey);
        buf.writeBoolean(this.resourcesKey);
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
            if(Objects.nonNull(itemReg)) NetworkHelper.writeString(buf,itemReg.toString());
        }
    }

    private void writePotions(ByteBuf buf, List<Entry<Potion,Integer>> potions) {
        int size = potions.size();
        for(Entry<Potion,Integer> potionTuple : potions)
            if(Objects.isNull(potionTuple.getKey().getRegistryName())) size--;
        buf.writeInt(size);
        for(Entry<Potion,Integer> potionTuple : potions) {
            ResourceLocation potionRes = potionTuple.getKey().getRegistryName();
            if(Objects.nonNull(potionRes)) {
                NetworkHelper.writeString(buf,potionRes.toString());
                buf.writeInt(potionTuple.getValue());
            }
        }
    }
}