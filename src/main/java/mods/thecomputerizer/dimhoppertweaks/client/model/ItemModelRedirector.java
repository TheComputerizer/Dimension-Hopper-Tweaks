package mods.thecomputerizer.dimhoppertweaks.client.model;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemModelRedirector implements ICustomModelLoader {
    @Override
    public void onResourceManagerReload(IResourceManager manager) {
    }

    @Override
    public boolean accepts(ResourceLocation res) {
        return false;
    }

    @Override
    public IModel loadModel(ResourceLocation res) throws Exception {
        return null;
    }
}
