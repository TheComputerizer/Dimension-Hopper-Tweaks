package mods.thecomputerizer.dimensionhoppertweaks.common.skills;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Arrays;

public class SkillCapabilityStorage implements Capability.IStorage<ISkillCapability> {

    public static final ImmutableList<String> SKILLS = ImmutableList.copyOf(Arrays.asList("mining", "gathering",
            "attack", "defense", "building", "agility", "farming", "magic", "void", "research"));

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side) {
        return instance.writeNBT();
    }

    @Override
    public void readNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound compound = (NBTTagCompound) nbt;
        if (compound.hasKey("skills_num") && compound.getInteger("skills_num") == SKILLS.size()) {
            for (int i = 0; i < compound.getInteger("skills_num"); i++) {
                String skill = compound.getString("skill_" + i);
                instance.setSkillXP(skill, compound.getInteger(skill + "_xp"), compound.getInteger(skill + "_level"));
            }
        }
    }
}
