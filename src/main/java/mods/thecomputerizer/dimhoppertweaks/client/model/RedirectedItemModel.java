package mods.thecomputerizer.dimhoppertweaks.client.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.animation.IClip;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class RedirectedItemModel implements IModel {
    
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return IModel.super.getDependencies();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return IModel.super.getTextures();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return null;
    }

    @Override
    public IModelState getDefaultState() {
        return IModel.super.getDefaultState();
    }

    @Override
    public Optional<? extends IClip> getClip(String name) {
        return IModel.super.getClip(name);
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        return IModel.super.process(customData);
    }

    @Override
    public IModel smoothLighting(boolean value) {
        return IModel.super.smoothLighting(value);
    }

    @Override
    public IModel gui3d(boolean value) {
        return IModel.super.gui3d(value);
    }

    @Override
    public IModel uvlock(boolean value) {
        return IModel.super.uvlock(value);
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures) {
        return IModel.super.retexture(textures);
    }

    @Override
    public Optional<ModelBlock> asVanillaModel() {
        return IModel.super.asVanillaModel();
    }
}