package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

@Mixin(value = DimensionManager.class, remap = false)
public abstract class MixinDimensionManager {

    @Shadow @Final private static ConcurrentMap<World, World> weakWorldMap;
    @Shadow @Final private static Int2ObjectMap<WorldServer> worlds;
    @Shadow @Final private static Multiset<Integer> leakedWorlds;

    @Shadow
    public static Integer[] getIDs() {
        return new Integer[0];
    }

    @Unique
    private static String dimhoppertweaks$getDimensionInfo(World world) {
        String folderName = world.getWorldInfo().getWorldName();
        WorldProvider provider = world.provider;
        String dimension = Objects.nonNull(provider) ? String.valueOf(provider.getDimension()) : "UNKNOWN";
        String name = Objects.nonNull(provider) ? provider.getDimensionType().getName() : "UNKNOWN";
        String isServerWorld = String.valueOf(world instanceof WorldServer);
        return String.format("Folder: `%s` | Dimension ID: `%s` | Dimension Name: %s | Is ServerWorld: %s",folderName,
                dimension,name,isServerWorld);
    }

    /**
     * @author The_Computerizer
     * @reason Add more info to the log when a leak occurs
     */
    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Overwrite
    public static Integer[] getIDs(boolean check) {
        if(check) {
            List<World> allWorlds = Lists.newArrayList(weakWorldMap.keySet());
            allWorlds.removeAll(worlds.values());
            for(ListIterator<World> li = allWorlds.listIterator(); li.hasNext(); ) {
                World w = li.next();
                leakedWorlds.add(System.identityHashCode(w));
            }
            for(World w : allWorlds) {
                int leakCount = leakedWorlds.count(System.identityHashCode(w));
                if(leakCount==5)
                    FMLLog.log.debug("The world {} ({}) may have leaked: first encounter (5 occurrences).\n",
                            Integer.toHexString(System.identityHashCode(w)),dimhoppertweaks$getDimensionInfo(w));
                else if(leakCount%5==0)
                    FMLLog.log.debug("The world {} ({}) may have leaked: seen {} times.\n",
                            Integer.toHexString(System.identityHashCode(w)),dimhoppertweaks$getDimensionInfo(w),leakCount);
            }
        }
        return getIDs();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;" +
            "error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), method = "initDimension")
    private static void dimhoppertweaks$ignoreErroringDimensions(Logger logger, String s, Object o, Object o1) {}
}