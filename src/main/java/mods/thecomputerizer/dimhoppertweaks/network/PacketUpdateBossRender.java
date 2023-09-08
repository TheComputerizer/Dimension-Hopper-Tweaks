package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketUpdateBossRender extends MessageImpl {

    private int bossID;
    private int phase;
    private boolean isShieldUp;
    private String animationState;
    private int projectileChargeTime;

    public PacketUpdateBossRender() {}

    public PacketUpdateBossRender(int bossID, int phase, boolean shield, String animation, int projectileCharge) {
        this.bossID = bossID;
        this.phase = phase;
        this.isShieldUp = shield;
        this.animationState = animation;
        this.projectileChargeTime = projectileCharge;
    }

    @Override
    public IMessage handle(MessageContext messageContext) {
        ClientPacketHandlers.handleUpdateBossRender(this.bossID, this.phase, this.isShieldUp,
                this.animationState, this.projectileChargeTime);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.bossID = buf.readInt();
        this.phase = buf.readInt();
        this.isShieldUp = buf.readBoolean();
        this.animationState = NetworkUtil.readString(buf);
        this.projectileChargeTime = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.bossID);
        buf.writeInt(this.phase);
        buf.writeBoolean(this.isShieldUp);
        NetworkUtil.writeString(buf,this.animationState);
        buf.writeInt(this.projectileChargeTime);
    }
}
