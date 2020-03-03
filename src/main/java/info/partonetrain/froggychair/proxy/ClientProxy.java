package info.partonetrain.froggychair.proxy;

import info.partonetrain.froggychair.client.renderer.SeatRenderer;
import info.partonetrain.froggychair.core.ModEntities;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void onSetupClient() {
        super.onSetupClient();

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SEAT, SeatRenderer::new);
    }

}
