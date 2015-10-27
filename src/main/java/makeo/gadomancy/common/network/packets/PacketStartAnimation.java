package makeo.gadomancy.common.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.blocks.tiles.TileInfusionClaw;
import makeo.gadomancy.common.blocks.tiles.TileManipulatorPillar;
import makeo.gadomancy.common.blocks.tiles.TileNodeManipulator;
import makeo.gadomancy.common.utils.ExplosionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.Thaumcraft;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 11.10.2015 15:19
 */
public class PacketStartAnimation implements IMessage, IMessageHandler<PacketStartAnimation, IMessage> {

    public static final byte ID_INFUSIONCLAW = 0;
    public static final byte ID_EX_VORTEX = 1;
    public static final byte ID_BURST = 2;
    public static final byte ID_PILLAR_RUNES = 3;

    private byte annimationId;
    private int x;
    private int y;
    private int z;

    public PacketStartAnimation(byte annimationId, int x, int y, int z) {
        this.annimationId = annimationId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PacketStartAnimation() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        this.annimationId = buf.readByte();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(annimationId);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(PacketStartAnimation message, MessageContext ctx) {
        switch (message.annimationId) {
            case ID_INFUSIONCLAW:
                TileInfusionClaw tile = (TileInfusionClaw) Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
                if(tile != null) {
                    tile.animationStates[8] = 1;
                }
                break;
            case ID_EX_VORTEX:
                TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
                if(te == null || !(te instanceof TileExtendedNode)) return null;
                ExplosionHelper.VortexExplosion.vortexLightning((TileExtendedNode) te);
                break;
            case ID_BURST:
                Thaumcraft.proxy.burst(Minecraft.getMinecraft().theWorld, message.x + 0.5F, message.y + 0.5F, message.z + 0.5F, Minecraft.getMinecraft().theWorld.rand.nextInt(3) + 1);
                break;
            case ID_PILLAR_RUNES:
                TileManipulatorPillar.doRuneEffects(Minecraft.getMinecraft().theWorld, message.x, message.y, message.z);
                break;
        }
        return null;
    }
}
