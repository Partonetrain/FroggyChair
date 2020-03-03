package info.partonetrain.froggychair;

import info.partonetrain.froggychair.core.ModBlocks;
import info.partonetrain.froggychair.proxy.ClientProxy;
import info.partonetrain.froggychair.proxy.CommonProxy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(FroggyChair.MODID)
public class FroggyChair {
    public static final String MODID = "froggychair";

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public FroggyChair() {

        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        PROXY.onSetupCommon();
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        PROXY.onSetupClient();
    }


}
