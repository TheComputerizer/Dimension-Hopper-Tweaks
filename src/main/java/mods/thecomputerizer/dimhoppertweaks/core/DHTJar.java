package mods.thecomputerizer.dimhoppertweaks.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.jar.JarFile;

public class DHTJar {

    private static final Logger LOGGER = LogManager.getLogger("DHT Jar");

    public static @Nullable JarFile findModsJar(String startsWith) {
        File file = null;
        try {
            file = findModsFile(startsWith);
            return Objects.nonNull(file) ? new JarFile(file) : null;
        } catch(IOException ex) {
            LOGGER.error("Unable to create JarFile instance from file {}!",file,ex);
            return null;
        }
    }

    public static @Nullable File findModsFile(String startsWith) {
        File mods = new File("mods");
        if((!mods.exists() || !mods.isDirectory()) && !mods.mkdirs()) {
            LOGGER.error("Unable to find or make the mods directory");
            return null;
        }
        File[] files = mods.listFiles((dir,name) -> name.startsWith(startsWith));
        File found = Objects.nonNull(files) && files.length>=1 ? files[0] : null;
        if(Objects.isNull(found)) LOGGER.error("Unable to find mods file that start with {}!",startsWith);
        return found;
    }
}
