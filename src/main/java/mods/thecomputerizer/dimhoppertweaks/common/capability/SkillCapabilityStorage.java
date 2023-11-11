package mods.thecomputerizer.dimhoppertweaks.common.capability;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SkillCapabilityStorage implements Capability.IStorage<ISkillCapability> {

    public static final ImmutableList<String> SKILLS = makeOrderedSkillList();

    private static ImmutableList<String> makeOrderedSkillList() {
        List<String> skills = Arrays.asList("agility","attack","building","defense","farming","gathering","magic",
                "mining","research","void");
        Collections.sort(skills);
        return ImmutableList.copyOf(skills);
    }

    @Override
    public @Nullable NBTBase writeNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side) {
        return instance.writeToNBT();
    }

    @Override
    public void readNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side, NBTBase nbt) {
        instance.readFromNBT((NBTTagCompound)nbt);
    }
}
