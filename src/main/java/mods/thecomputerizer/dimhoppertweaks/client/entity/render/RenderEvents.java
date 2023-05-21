package mods.thecomputerizer.dimhoppertweaks.client.entity.render;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.TransformUtils;
import mods.thecomputerizer.dimhoppertweaks.Constants;
import mods.thecomputerizer.dimhoppertweaks.client.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

@SuppressWarnings("ConstantConditions")
@Mod.EventBusSubscriber(modid = Constants.MODID, value = Side.CLIENT)
public class RenderEvents {
    public static final List<RenderDelayedAOE> ATTACKS = Collections.synchronizedList(new ArrayList<>());

    @SubscribeEvent
    public static void renderAttacks(RenderWorldLastEvent event) {
        synchronized (ATTACKS) {
            for (RenderDelayedAOE attack : ATTACKS)
                attack.render(event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public static void tickTimers(TickEvent.ClientTickEvent event) {
        synchronized (ATTACKS) {
            if (event.phase == TickEvent.Phase.END) ATTACKS.removeIf(RenderDelayedAOE::tick);
        }
    }
}
