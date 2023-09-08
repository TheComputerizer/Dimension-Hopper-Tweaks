package mods.thecomputerizer.dimhoppertweaks.common.skills;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
                instance.setPrestigeLevel(skill,compound.getInteger(skill+"_prestige"));
            }
        }
        if(compound.hasKey("recent_gathering_num")) {
            Map<Item, MutableInt> gatheringList = new HashMap<>();
            for (int i = 0; i < compound.getInteger("recent_gathering_num"); i++) {
                Item item = Item.getByNameOrId(compound.getString("recent_gathering_resource_" + i));
                int count = compound.getInteger("recent_gathering_counter_"+i);
                if(Objects.nonNull(item)) gatheringList.put(item,new MutableInt(count));
            }
            instance.syncGatheringItems(gatheringList);
        }
        if(compound.hasKey("skill_to_drain") && compound.hasKey("drain_levels"))
            instance.setDrainSelection(compound.getString("skill_to_drain"), compound.getInteger("drain_levels"), null);
        if(compound.hasKey("twilight_respawn")) instance.setTwilightRespawn(BlockPos.fromLong(compound.getLong("twilight_respawn")));
        instance.initDreamTimer(compound.getInteger("dream_timer"));
    }
}
