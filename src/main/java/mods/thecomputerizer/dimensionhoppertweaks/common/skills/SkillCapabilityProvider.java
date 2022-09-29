package mods.thecomputerizer.dimensionhoppertweaks.common.skills;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("ConstantConditions")
public class SkillCapabilityProvider implements ICapabilitySerializable<NBTBase> {

    private final EntityPlayerMP player;

    @CapabilityInject(ISkillCapability.class)
    public static final Capability<ISkillCapability> SKILL_CAPABILITY = null;
    private final ISkillCapability impl = SKILL_CAPABILITY.getDefaultInstance();

    public SkillCapabilityProvider(EntityPlayerMP player) {
        this.player = player;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability==SKILL_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == SKILL_CAPABILITY ? SKILL_CAPABILITY.cast(this.impl) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return SKILL_CAPABILITY.getStorage().writeNBT(SKILL_CAPABILITY,this.impl,null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        ((SkillCapabilityStorage)SKILL_CAPABILITY.getStorage()).setPlayer(this.player);
        SKILL_CAPABILITY.getStorage().readNBT(SKILL_CAPABILITY, this.impl, null, nbt);
    }
}
