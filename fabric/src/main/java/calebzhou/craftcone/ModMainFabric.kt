package calebzhou.craftcone

import net.fabricmc.api.ModInitializer

/**
 * Created  on 2022-11-09,8:55.
 */
class ModMainFabric :ModInitializer{
    override fun onInitialize() {
        ModMain.onInit()
    }
}