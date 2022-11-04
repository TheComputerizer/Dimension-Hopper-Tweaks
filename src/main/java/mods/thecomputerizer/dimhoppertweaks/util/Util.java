package mods.thecomputerizer.dimhoppertweaks.util;

import javax.annotation.Nonnull;

public final class Util {
    private Util() {}

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public static <T> T sneakyNull() {
        return null;
    }
}
