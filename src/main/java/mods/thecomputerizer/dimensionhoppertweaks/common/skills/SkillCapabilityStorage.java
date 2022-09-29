package mods.thecomputerizer.dimensionhoppertweaks.common.skills;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SkillCapabilityStorage implements Capability.IStorage<ISkillCapability> {

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
        instance.setMiningXP(getArgs(compound,"reskillable","mining"));
        instance.setGatheringXP(getArgs(compound,"reskillable","gathering"));
        instance.setAttackXP(getArgs(compound,"reskillable","attack"));
        instance.setDefenseXP(getArgs(compound,"reskillable","defense"));
        instance.setBuildingXP(getArgs(compound,"reskillable","building"));
        instance.setAgilityXP(getArgs(compound,"reskillable","agility"));
        instance.setFarmingXP(getArgs(compound,"reskillable","farming"));
        instance.setMagicXP(getArgs(compound,"reskillable","magic"));
        instance.setVoidXP(getArgs(compound,"compatskills","void"));
        instance.setResearchXP(getArgs(compound,"compatskills","research"));
    }

    private int[] getArgs(NBTTagCompound compound, String modid, String name) {
        int[] ret = new int[2];
        ret[0] = compound.getInteger(name+"_xp");
        Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation(modid,name));
        if(skill!=null) ret[1] = PlayerDataHandler.get(this.player).getSkillInfo(skill).getLevel()*100;
        else ret[1] = Integer.MAX_VALUE;
        return ret;
    }
}
