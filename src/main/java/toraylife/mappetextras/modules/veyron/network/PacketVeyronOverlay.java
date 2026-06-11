package toraylife.mappetextras.modules.veyron.network;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toraylife.mappetextras.modules.veyron.client.VeyronOverlayRenderer;

public class PacketVeyronOverlay implements IMessage
{
    public NBTTagCompound data = new NBTTagCompound();

    public PacketVeyronOverlay()
    {}

    public PacketVeyronOverlay(NBTTagCompound data)
    {
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.data);
    }

    public static class ClientHandler extends ClientMessageHandler<PacketVeyronOverlay>
    {
        @Override
        @SideOnly(Side.CLIENT)
        public void run(EntityPlayerSP player, PacketVeyronOverlay message)
        {
            VeyronOverlayRenderer.setState(message.data);
        }
    }
}
