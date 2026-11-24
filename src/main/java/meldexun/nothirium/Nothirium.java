package meldexun.nothirium;

import meldexun.nothirium.mixin.MixinLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "nothirium", name = "Nothirium", version = "2.1.4", clientSideOnly = true)
public class Nothirium {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.load(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        RenderProfileManager.init();
        MinecraftForge.EVENT_BUS.register(new RenderProfileManager());
    }
}
