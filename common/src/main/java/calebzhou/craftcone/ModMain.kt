package calebzhou.craftcone

import calebzhou.craftcone.ModMain.MOD_ID
import org.apache.logging.log4j.LogManager

/**
 * Created  on 2022-11-09,8:47.
 */
val logger = LogManager.getLogger(MOD_ID)
object ModMain {
    const val MOD_ID = "craftcone"

    @JvmStatic
    fun onInit(){
        logger.info("craftCONE is loaded")
    }
}