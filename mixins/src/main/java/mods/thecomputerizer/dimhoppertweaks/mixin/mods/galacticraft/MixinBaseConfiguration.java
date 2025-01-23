package mods.thecomputerizer.dimhoppertweaks.mixin.mods.galacticraft;

import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseConfiguration;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseRoom.EnumRoomType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseRoom.EnumRoomType.EMPTY;
import static micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseRoom.EnumRoomType.ENGINEERING;

@Mixin(value = BaseConfiguration.class, remap = false)
public abstract class MixinBaseConfiguration {

    @Shadow private EnumRoomType[] roomTypes;

    @Shadow private int[] randomRoomTypes;

    /**
     * @author The_Computerizer
     * @reason Remove engineering room
     */
    @Overwrite
    public EnumRoomType getRandomRoom(int i) {
        EnumRoomType type = this.roomTypes[this.randomRoomTypes[i%this.randomRoomTypes.length]];
        return type==ENGINEERING ? EMPTY : type;
    }
}