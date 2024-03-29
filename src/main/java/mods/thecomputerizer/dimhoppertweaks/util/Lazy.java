package mods.thecomputerizer.dimhoppertweaks.util;

import java.util.Objects;
import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T> {
    private volatile Object lock;
    private volatile Supplier<T> creator;
    private volatile T thing;

    private Lazy(final Supplier<T> creator) {
        this.lock = new Object();
        this.creator = creator;
        this.thing = null;
    }

    public static <T> Lazy<T> of(final Supplier<T> creator) {
        return new Lazy<>(creator);
    }

    @Override
    public T get() {
        Object localLock = this.lock;
        if(Objects.nonNull(this.creator)) {
            synchronized (localLock) {
                this.thing = this.creator.get();
                this.creator = null;
                this.lock = null;
            }
        }
        return this.thing;
    }
}
