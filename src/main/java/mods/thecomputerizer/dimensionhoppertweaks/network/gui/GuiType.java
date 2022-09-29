package mods.thecomputerizer.dimensionhoppertweaks.network.gui;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public enum GuiType {

    TOKEN_EXCHANGE("token_exchange", TokenExchangeConstructor.class);

    private static final Map<String, GuiType> GUI_TYPES = Maps.newHashMap();
    private final String id;
    private final Class<? extends IGuiConstructor> constructorClass;
    GuiType(String id, Class<? extends IGuiConstructor> constructorClass) {
        this.id = id;
        this.constructorClass = constructorClass;
    }

    public String getId() {
        return this.id;
    }

    public IGuiConstructor initializeConstructor(ByteBuf buf) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.constructorClass.getConstructor(ByteBuf.class).newInstance(buf);
    }

    public static GuiType get(String id) {
        return GUI_TYPES.get(id);
    }

    static {
        for (GuiType type : values()) {
            if (GUI_TYPES.containsKey(type.getId()))
                throw new Error("Tried to register duplicate gui type with id "+type.getId());
            else GUI_TYPES.put(type.getId(), type);
        }
    }
}
