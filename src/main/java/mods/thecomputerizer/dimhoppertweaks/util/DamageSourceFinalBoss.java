package mods.thecomputerizer.dimhoppertweaks.util;

import morph.avaritia.util.DamageSourceInfinitySword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class DamageSourceFinalBoss extends DamageSourceInfinitySword {

    public DamageSourceFinalBoss(Entity source) {
        super(source);
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entity) {
        return new TextComponentString("§5§l§kYOU CAN'T READ THIS LOL");
    }
}
