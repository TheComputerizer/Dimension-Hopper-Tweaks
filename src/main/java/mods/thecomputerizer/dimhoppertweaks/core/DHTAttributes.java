package mods.thecomputerizer.dimhoppertweaks.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;

public class DHTAttributes {

    private static final Logger LOGGER = LogManager.getLogger("DHT Attributes");
    private static final Attributes ATTRIBUTES = getAttributes();
    public static final Name DEPENDENCIES = new Name("DHT_DEPENDENCIES");
    public static final Name NAME = new Name("DHT_NAME");
    public static final Name VERSION = new Name("DHT_VERSION");

    public static String getAttribute(Name name) {
        return Objects.nonNull(ATTRIBUTES) ? ATTRIBUTES.getValue(name) : "";
    }

    private static @Nullable Attributes getAttributes() {
        String fileName = "dimhoppertweaks";
        try(JarFile jar = DHTJar.findModsJar(fileName)) {
            if(Objects.isNull(jar)) throw new IOException("Jar file is null!");
            fileName = jar.getName();
            return Objects.nonNull(jar.getManifest()) ? jar.getManifest().getMainAttributes() : null;
        } catch(IOException ex) {
            LOGGER.error("Unable to get attributes for {}!",fileName);
            return null;
        }
    }
}