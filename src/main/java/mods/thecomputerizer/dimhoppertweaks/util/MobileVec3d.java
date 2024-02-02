package mods.thecomputerizer.dimhoppertweaks.util;

import net.minecraft.util.math.Vec3d;

import java.util.Collection;

public class MobileVec3d {

    public static Collection<Vec3d> stepAll(Collection<Vec3d> store, Collection<MobileVec3d> vecs) {
        for(MobileVec3d mobile : vecs)
            store.add(mobile.step());
        return store;
    }

    private final Vec3d base;
    private final Vec3d step;
    private Vec3d curPos;

    public MobileVec3d(Vec3d base, Vec3d step) {
        this.base = base;
        this.step = step;
        this.curPos = base;
    }

    public Vec3d step() {
        this.curPos = this.curPos.add(this.step);
        return this.curPos;
    }
}
