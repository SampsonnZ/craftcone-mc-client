package calebzhou.craftcone;

import calebzhou.craftcone.consts.SharedConsts;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Created  on 2022-11-09,22:04.
 */
@Mod(SharedConsts.MOD_ID)
public class ModMainForge {
    public ModMainForge(){
        EventBuses.registerModEventBus(SharedConsts.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ModMain.onInit();
    }
}
