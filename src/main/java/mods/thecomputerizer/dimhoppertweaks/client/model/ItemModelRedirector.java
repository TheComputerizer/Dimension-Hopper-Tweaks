package mods.thecomputerizer.dimhoppertweaks.client.model;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;


public class ItemModelRedirector implements ICustomModelLoader {
    
    @Override public void onResourceManagerReload(IResourceManager manager) {
    }

    @Override public boolean accepts(ResourceLocation res) {
        return false;
    }

    @Override public IModel loadModel(ResourceLocation res) throws Exception {
        return null;
    }
}