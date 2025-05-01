package mods.thecomputerizer.dimhoppertweaks.client.particle;

import mods.thecomputerizer.dimhoppertweaks.registry.ParticleRegistry;
import mods.thecomputerizer.dimhoppertweaks.util.FontUtil;
import mods.thecomputerizer.theimpossiblelibrary.api.shapes.vectors.Vector4;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@SideOnly(CLIENT)
public class ParticleAscii extends Particle {

    private static final String POTENTIAL_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final double rangeFactor;
    private char curChar;
    private Vector4 charUV;

    public ParticleAscii(World world, double x, double y, double z, double velocityX, double velocityY,
                         double velocityZ, float maxAge, double rangeFactor, float scale) {
        super(world,x,y,z,velocityX,velocityY,velocityZ);
        this.rangeFactor = rangeFactor;
        randomizeInitialPos();
        this.particleTexture = ParticleRegistry.getFontAtlas();
        this.particleScale = scale+(this.rand.nextFloat()/(1f/(scale/2f)));
        this.particleMaxAge = (int)(maxAge-(this.rand.nextFloat()*(maxAge/4f)));
        this.particleAlpha = 1.0f;
        this.curChar = '0';
        this.charUV = FontUtil.getCharUV(this.curChar,Minecraft.getMinecraft().fontRenderer);
        setRBGColorF(0.2f,1f,0.2f);
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
        if(this.particleAge++ >= this.particleMaxAge) setExpired();
        move(this.motionX,this.motionY,this.motionZ);
        this.motionX*=0.8999999761581421;
        this.motionY*=0.8999999761581421;
        this.motionZ*=0.8999999761581421;
        if(this.onGround) {
            this.motionX*=0.699999988079071;
            this.motionZ*=0.699999988079071;
        }
        this.curChar = POTENTIAL_CHARS.charAt(this.rand.nextInt(POTENTIAL_CHARS.length()));
        this.charUV = FontUtil.getCharUV(this.curChar, Minecraft.getMinecraft().fontRenderer);
    }

    @Override public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX,
            float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        FontUtil.bufferCharTex(this.curChar,Minecraft.getMinecraft().fontRenderer);
        double minU = particleTexture.getMinU()+this.charUV.x().doubleValue();
        double maxU = particleTexture.getMinU()+this.charUV.y().doubleValue();
        double minV = particleTexture.getMinV()+this.charUV.z().doubleValue();
        double maxV = particleTexture.getMinV()+this.charUV.w().doubleValue();
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
        buffer.pos(x-scaledLRDirX-scaledUDDirX,y-scaledUDDirY,z-scaledLRDirZ-scaledUDDirZ)
                .tex(maxU,maxV).color(this.particleRed,this.particleGreen,this.particleBlue,this.particleAlpha)
                .lightmap(skyLight,blockLight).endVertex();
        buffer.pos(x-scaledLRDirX+scaledUDDirX,y+scaledUDDirY,z-scaledLRDirZ+scaledUDDirZ)
                .tex(maxU,minV).color(this.particleRed,this.particleGreen,this.particleBlue,this.particleAlpha)
                .lightmap(skyLight,blockLight).endVertex();
        buffer.pos(x+scaledLRDirX+scaledUDDirX,y+scaledUDDirY,z+scaledLRDirZ+scaledUDDirZ)
                .tex(minU,minV).color(this.particleRed,this.particleGreen,this.particleBlue,this.particleAlpha)
                .lightmap(skyLight,blockLight).endVertex();
        buffer.pos(x+scaledLRDirX-scaledUDDirX,y-scaledUDDirY,z+scaledLRDirZ-scaledUDDirZ)
                .tex(minU,maxV).color(this.particleRed,this.particleGreen,this.particleBlue,this.particleAlpha)
                .lightmap(skyLight,blockLight).endVertex();
    }

    public static class Factory implements IParticleFactory {

        @Override public @Nullable ParticleAscii createParticle(int id, World world, double posX, double posY,
                double posZ, double velocityX, double velocityY, double velocityZ, int... args) {
            return new ParticleAscii(Minecraft.getMinecraft().world,posX,posY,posZ,velocityX,velocityY,velocityZ,
                    args.length>=1 ? (float)args[0] : 100f,args.length>=2 ? (double)args[1] : 32d,
                    args.length>=3 ? ((float)args[2])/100f : 0.5f);
        }
    }
}