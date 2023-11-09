package mods.thecomputerizer.dimhoppertweaks.common.skills;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Arrays;

public class SkillCapabilityStorage implements Capability.IStorage<ISkillCapability> {

    public static final ImmutableList<String> SKILLS = ImmutableList.copyOf(Arrays.asList("mining","gathering", "attack",
            "defense","building","agility","farming","magic","void","research"));


    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side) {
        return instance.writeToNBT();
    }

    @Override
    public void readNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side, NBTBase nbt) {
        instance.readFromNBT((NBTTagCompound)nbt);
    }
}
