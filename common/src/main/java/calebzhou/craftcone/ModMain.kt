package calebzhou.craftcone

import calebzhou.craftcone.consts.FileConsts
import calebzhou.craftcone.consts.SharedConsts.MOD_ID
import calebzhou.craftcone.consts.SharedConsts.MOD_NAME
import com.google.gson.GsonBuilder
import org.apache.logging.log4j.LogManager
import java.sql.Connection
import java.sql.DriverManager

/**
 * Created  on 2022-11-09,8:47.
 */
val logger = LogManager.getLogger(MOD_NAME)
val gson = GsonBuilder().serializeNulls().create()
object ModMain {
    lateinit var mainDb :Connection

    @JvmStatic
    fun onInit(){
        logger.info("$MOD_NAME is loading")
        if (!FileConsts.CRAFTCONE_FOLDER.exists()) {
            logger.info("$MOD_NAME can't find directory “.minecraft/${MOD_ID}”. Creating it...")
            FileConsts.CRAFTCONE_FOLDER.mkdir()
        }
        DriverManager.registerDriver(org.sqlite.JDBC())
        mainDb=DriverManager.getConnection("jdbc:sqlite:${FileConsts.CRAFTCONE_FOLDER.absolutePath}/main_data.db")


        logger.info("$MOD_NAME is loaded successfully")
    }
}