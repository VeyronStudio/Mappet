package toraylife.mappetextras;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toraylife.mappetextras.modules.veyron.client.VeyronOverlayRenderer;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    private final VeyronOverlayRenderer veyronOverlayRenderer = new VeyronOverlayRenderer();

    public ClientProxy() {
    }
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(this.veyronOverlayRenderer);
    }

    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
