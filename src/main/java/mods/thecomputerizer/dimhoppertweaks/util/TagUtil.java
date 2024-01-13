package mods.thecomputerizer.dimhoppertweaks.util;

import crafttweaker.api.data.*;
import crafttweaker.mc1120.data.StringIDataParser;
import net.minecraft.nbt.*;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public class TagUtil {
    public static boolean tagsMatch(@Nullable NBTTagCompound tag, @Nullable NBTTagCompound toMatch, boolean nullMatchesAny) {
        if(Objects.isNull(toMatch)) return nullMatchesAny || Objects.isNull(tag);
        if(Objects.isNull(tag)) return false;
        for(String key : toMatch.getKeySet()) {
            if(!tag.hasKey(key) || !innerMatch(tag.getTag(key),toMatch.getTag(key),nullMatchesAny)) return false;
        }
        return true;
    }

    private static boolean innerMatch(NBTBase tag, NBTBase toMatch, boolean nullMatchesAny) {
        if(toMatch.toString().equals(tag.toString())) return true;
        if(toMatch instanceof NBTPrimitive) {
            if(!(tag instanceof NBTPrimitive)) return false;
            return ((NBTPrimitive)toMatch).getDouble()==((NBTPrimitive)tag).getDouble();
        }
        if(toMatch instanceof NBTTagCompound) {
            if(!(tag instanceof NBTTagCompound)) return false;
            return tagsMatch((NBTTagCompound)tag,(NBTTagCompound)toMatch,nullMatchesAny);
        }
        return tag.equals(toMatch);
    }

    /**
     * The defKey parameter is only needed when the tag is not parsed as a map for some reason
     */
    public static @Nullable NBTTagCompound parseTag(String tagString, String defKey) {
        IData data = StringIDataParser.parse(tagString);
        if(Objects.isNull(data)) return null;
        NBTBase nbt = dataToTag(data);
        if(nbt instanceof NBTTagCompound) return (NBTTagCompound)nbt;
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag(defKey,nbt);
        return tag;
    }

    public static NBTBase dataToTag(IData data) {
        if(data instanceof DataBool) return dataBoolToTag((DataBool)data);
        if(data instanceof DataByte) return dataByteToTag((DataByte)data);
        if(data instanceof DataByteArray) return dataByteArrayToTag((DataByteArray)data);
        if(data instanceof DataDouble) return dataDoubleToTag((DataDouble)data);
        if(data instanceof DataFloat) return dataFloatToTag((DataFloat)data);
        if(data instanceof DataInt) return dataIntToTag((DataInt)data);
        if(data instanceof DataIntArray) return dataIntArrayToTag((DataIntArray)data);
        if(data instanceof DataList) return dataListToTag((DataList)data);
        if(data instanceof DataLong) return dataLongToTag((DataLong)data);
        if(data instanceof DataMap) return dataMapToTag((DataMap)data);
        if(data instanceof DataShort) return dataShortToTag((DataShort)data);
        return dataStringToTag((DataString)data);
    }

    public static NBTTagByte dataBoolToTag(DataBool data) {
        return new NBTTagByte((byte)(data.asBool() ? 1 : 0));
    }

    public static NBTTagByte dataByteToTag(DataByte data) {
        return new NBTTagByte(data.asByte());
    }

    public static NBTTagByteArray dataByteArrayToTag(DataByteArray data) {
        return new NBTTagByteArray(data.asByteArray());
    }

    public static NBTTagDouble dataDoubleToTag(DataDouble data) {
        return new NBTTagDouble(data.asDouble());
    }

    public static NBTTagFloat dataFloatToTag(DataFloat data) {
        return new NBTTagFloat(data.asFloat());
    }

    public static NBTTagInt dataIntToTag(DataInt data) {
        return new NBTTagInt(data.asInt());
    }

    public static NBTTagIntArray dataIntArrayToTag(DataIntArray data) {
        return new NBTTagIntArray(data.asIntArray());
    }

    public static NBTTagList dataListToTag(DataList data) {
        NBTTagList tag = new NBTTagList();
        for(IData element : data.asList())
            tag.appendTag(dataToTag(element));
        return tag;
    }

    public static NBTTagLong dataLongToTag(DataLong data) {
        return new NBTTagLong(data.asLong());
    }

    public static NBTTagCompound dataMapToTag(DataMap data) {
        NBTTagCompound tag = new NBTTagCompound();
        for(Map.Entry<String,IData> entry : data.asMap().entrySet())
            tag.setTag(entry.getKey(),dataToTag(entry.getValue()));
        return tag;
    }

    public static NBTTagShort dataShortToTag(DataShort data) {
        return new NBTTagShort(data.asShort());
    }

    public static NBTTagString dataStringToTag(DataString data) {
        return new NBTTagString(data.asString());
    }
}
