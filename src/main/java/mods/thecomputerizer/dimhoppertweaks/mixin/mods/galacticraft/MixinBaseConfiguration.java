package mods.thecomputerizer.dimhoppertweaks.mixin.mods.galacticraft;

import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseConfiguration;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.BaseRoom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BaseConfiguration.class, remap = false)
public abstract class MixinBaseConfiguration {

    @Shadow private BaseRoom.EnumRoomType[] roomTypes;

    @Shadow private int[] randomRoomTypes;

    /**
     * @author The_Computerizer
     * @reason Remove engineering room
     */
    @Overwrite
    public BaseRoom.EnumRoomType getRandomRoom(int i) {
        BaseRoom.EnumRoomType type = this.roomTypes[this.randomRoomTypes[i%this.randomRoomTypes.length]];
        return type==BaseRoom.EnumRoomType.ENGINEERING ? BaseRoom.EnumRoomType.EMPTY : type;
    }
}