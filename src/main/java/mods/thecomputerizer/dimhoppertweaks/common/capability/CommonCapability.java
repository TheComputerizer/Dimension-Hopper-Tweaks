package mods.thecomputerizer.dimhoppertweaks.common.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class CommonCapability {

    public static <P extends ICapabilityProvider,I,C extends Capability<I>> void executeIfPresent(
            P provider, @Nullable C capability, Consumer<I> setter) {
        I capabilityInterface = getCapability(provider,capability);
        if(Objects.nonNull(capabilityInterface)) setter.accept(capabilityInterface);
    }

    public static <P extends ICapabilityProvider,I,C extends Capability<I>> @Nullable I getCapability(
            @Nullable P provider, @Nullable C capability) {
        return Objects.nonNull(provider) && Objects.nonNull(capability) ?
                provider.getCapability(capability,null) : null;
    }

    public static <P extends ICapabilityProvider,I,C extends Capability<I>,T> @Nullable T getNullable(
            P provider, @Nullable C capability, Function<I,T> getter) {
        I capabilityInterface = getCapability(provider,capability);
        return Objects.nonNull(capabilityInterface) ? getter.apply(capabilityInterface) : null;
    }

    public static <P extends ICapabilityProvider,I,C extends Capability<I>,T> T getOrDefault(
            P provider, @Nullable C capability, Function<I,T> getter, T defVal) {
        I capabilityInterface = getCapability(provider,capability);
        return Objects.nonNull(capabilityInterface) ? getter.apply(capabilityInterface) : defVal;
    }
}
