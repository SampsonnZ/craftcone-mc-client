package calebzhou.craftcone

import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

/**
 * Created  on 2022-11-09,8:55.
 */
class ModMainQuilt :ModInitializer{
    override fun onInitialize(mod: ModContainer) {
        ModMain.onInit()
    }
}