package calebzhou.craftcone.model

/**
 * Created  on 2022-11-09,17:05.
 */
@kotlinx.serialization.Serializable
data class CentralServerData(val name:String,val ip:String,val version: Int)
