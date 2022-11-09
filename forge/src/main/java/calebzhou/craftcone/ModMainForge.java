package calebzhou.craftcone;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Created  on 2022-11-09,22:04.
 */
@Mod(ModMain.MOD_ID)
public class ModMainForge {
    public ModMainForge(){
        EventBuses.registerModEventBus(ModMain.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ModMain.onInit();
    }
}
