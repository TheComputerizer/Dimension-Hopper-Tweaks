package mods.thecomputerizer.dimensionhoppertweaks.common.skills;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.skill.Skill;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Arrays;

public class SkillCapabilityStorage implements Capability.IStorage<ISkillCapability> {

    public static final ImmutableList<String> SKILLS = ImmutableList.copyOf(Arrays.asList("mining", "gathering",
            "attack", "defense", "building", "agility", "farming", "magic", "void", "research"));
    private EntityPlayerMP player;

    public void setPlayer(EntityPlayerMP player) {
        this.player = player;
    }

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side) {
        return instance.writeNBT();
    }

    @Override
    public void readNBT(Capability<ISkillCapability> capability, ISkillCapability instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound compound = (NBTTagCompound)nbt;
        if(compound.hasKey("skills_num")) {
            for (int i = 0; i < compound.getInteger("skills_num"); i++) {
                String skill = compound.getString("skill_" + i);
                instance.setSkillXP(skill, compound.getInteger(skill + "_xp"), compound.getInteger(skill + "_level"));
            }
        } else {
            //handles when a player is first joining the world and does not yet have the capability
            for(String skill : SKILLS) addDefaultSkillValues(skill, instance);
            instance.setDrainSelection("mining",1);
        }
    }

    private void addDefaultSkillValues(String name, ISkillCapability instance) {
        String modid;
        if(name.matches("void") || name.matches("research")) modid = "compatskills";
        else modid = "reskillable";
        int level;
        Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation(modid,name));
        if(skill!=null) level = PlayerDataHandler.get(this.player).getSkillInfo(skill).getLevel()*100;
        else level = Integer.MAX_VALUE;
        instance.setSkillXP(name,0,level);
    }
}
