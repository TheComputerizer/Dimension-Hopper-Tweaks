package mods.thecomputerizer.dimhoppertweaks.client.particle;

import mods.thecomputerizer.dimhoppertweaks.client.render.BetterBlightFireRenderer;
import mods.thecomputerizer.dimhoppertweaks.registry.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.lib.event.ClientTicks;
import net.silentchaos512.scalinghealth.lib.module.ModuleAprilTricks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Point4f;

@SideOnly(Side.CLIENT)
public class ParticleBlightFire extends Particle {

    private final double rangeFactor;
    private final boolean tomfoolery;
    private Point4f frameUV;

    public ParticleBlightFire(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ,
                         float maxAge, double rangeFactor, float scale) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.rangeFactor = rangeFactor;
        randomizeInitialPos();
        this.tomfoolery = ModuleAprilTricks.instance.isEnabled() && ModuleAprilTricks.instance.isRightDay();
        this.particleTexture = tomfoolery ? ParticleRegistry.getGrayFireAtlas() : ParticleRegistry.getFireAtlas();
        this.particleScale = scale+(this.rand.nextFloat()/(1f/(scale/2f)));
        this.particleMaxAge = (int)(maxAge-(this.rand.nextFloat()*(maxAge/4f)));
        this.particleAlpha = 1f;
        this.frameUV = getUVFrame();
        setRBGColorF(0.2f,1f,0.2f);
    }

    private Point4f getUVFrame() {
        int frame = ClientTicks.ticksInGame % 64;
        boolean isOffset = frame>31;
        if(isOffset) frame-=32;
        float minU = isOffset ? 0.5f : 0;
        return new Point4f(minU,minU+0.5f,(float)frame/32f,(float)(frame+1)/32f);
    }

    private double randomizeDouble(double original) {
        double factor = Math.log(this.rangeFactor) / Math.log(2);
        return (original-factor)+(this.rand.nextDouble()*factor*2);
    }

    private void randomizeInitialPos() {
        double x = randomizeDouble(this.posX);
        double y = randomizeDouble(this.posY);
        double z = randomizeDouble(this.posZ);
        setPosition(x,y,z);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
        if (this.particleAge++ >= this.particleMaxAge) setExpired();
        move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.8999999761581421;
        this.motionY *= 0.8999999761581421;
        this.motionZ *= 0.8999999761581421;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
        this.frameUV = getUVFrame();
    }

    @Override
    public void renderParticle(@Nonnull BufferBuilder buffer, @Nonnull Entity entityIn, float partialTicks,
                               float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        TextureManager manager = Minecraft.getMinecraft().getTextureManager();
        if(this.tomfoolery) manager.bindTexture(BetterBlightFireRenderer.TEXTURE_GRAY);
        else manager.bindTexture(BetterBlightFireRenderer.TEXTURE);
        double minU = particleTexture.getMinU()+this.frameUV.x;
        double maxU = particleTexture.getMinU()+this.frameUV.y;
        double minV = particleTexture.getMinV()+this.frameUV.z;
        double maxV = particleTexture.getMinV()+this.frameUV.w;
        double x = this.prevPosX+(this.posX-this.prevPosX)*partialTicks-interpPosX;
        double y = this.prevPosY+(this.posY-this.prevPosY)*partialTicks-interpPosY;
        double z = this.prevPosZ+(this.posZ-this.prevPosZ)*partialTicks-interpPosZ;
        double scaledUDDirX = rotationXY*this.particleScale;
        double scaledUDDirY = rotationZ*this.particleScale;
        double scaledUDDirZ = rotationXZ*this.particleScale;
        double scaledLRDirX = rotationX*this.particleScale;
        double scaledLRDirZ = rotationYZ*this.particleScale;
        int combinedBrightness = getBrightnessForRender(partialTicks);
        int skyLight = combinedBrightness >> 16 & 65535;
        int blockLight = combinedBrightness & 65535;
        buffer.pos(x - scaledLRDirX - scaledUDDirX, y - scaledUDDirY, z - scaledLRDirZ - scaledUDDirZ)
                .tex(maxU,maxV)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(skyLight, blockLight).endVertex();
        buffer.pos(x - scaledLRDirX + scaledUDDirX,y + scaledUDDirY,z - scaledLRDirZ + scaledUDDirZ)
                .tex(maxU, minV)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(skyLight, blockLight).endVertex();
        buffer.pos(x + scaledLRDirX + scaledUDDirX,y + scaledUDDirY,z + scaledLRDirZ + scaledUDDirZ)
                .tex(minU, minV)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(skyLight, blockLight).endVertex();
        buffer.pos(x + scaledLRDirX - scaledUDDirX,y - scaledUDDirY,z + scaledLRDirZ - scaledUDDirZ)
                .tex(minU, maxV)
                .color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha)
                .lightmap(skyLight, blockLight).endVertex();
    }

    public static class Factory implements IParticleFactory {
        @Nullable
        @Override
        public Particle createParticle(int id, @Nonnull World world, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ, @Nonnull int... args) {
            return new ParticleBlightFire(Minecraft.getMinecraft().world, posX, posY, posZ, velocityX, velocityY, velocityZ,
                    args.length>=1 ? (float)args[0] : 100f, args.length>=2 ? (double)args[1] : 32d,
                    args.length>=3 ? ((float)args[2])/100f : 0.5f);
        }
    }
}
