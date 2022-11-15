package calebzhou.craftcone.level.storage

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap
import net.minecraft.world.level.chunk.storage.RegionFile
import java.nio.file.Path


/**
 * Created  on 2022-11-11,10:06.
 */
class ConeRegionFileStorage(val folder:Path,val sync:Boolean):AutoCloseable {
    private val FILE_EXTENSION = ".cma"
    private val MAX_CACHE_SIZE = 256
    private val regionCache = Long2ObjectLinkedOpenHashMap<ConeRegionFile>()

    //private val testFolder = File(FileConsts.CRAFTCONE_FOLDER,"saves")
   /* @JvmStatic
    fun write(chunkPos: ChunkPos,chunkData:CompoundTag?){
        val fileName = "r.${chunkPos.regionX}.${chunkPos.regionZ}.json"
        if (chunkData != null) {
            val json  = chunkData.asString
            FileUtils.write(File(testFolder,fileName),json,Charsets.UTF_8,true)
        }
    }

    @JvmStatic
    fun write7z(mapFolderPath:Path){
        val sevenZOutput = SevenZOutputFile(File(mapFolderPath.toFile(),"map.7z"))

        Files.walk(mapFolderPath).forEach { path->
            val file = path.toFile()
            // Directory is not streamed, but its files are streamed into 7z file with
            // folder in it's path
            // Directory is not streamed, but its files are streamed into 7z file with
            // folder in it's path
            if (!file.isDirectory) {
                println("Seven Zipping file - $file")
                try {
                    FileInputStream(file).use { fis ->
                        val entry_1: SevenZArchiveEntry = sevenZOutput.createArchiveEntry(file, file.toString())
                        sevenZOutput.putArchiveEntry(entry_1)
                        sevenZOutput.write(Files.readAllBytes(file.toPath()))
                        sevenZOutput.closeArchiveEntry()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        sevenZOutput.finish()
        sevenZOutput.close()
    }
*/
    override fun close() {
        TODO("Not yet implemented")
    }
/*
    @JvmStatic
    fun read(chunkPos: ChunkPos): CompoundTag {
        val fileName = "r.${chunkPos.regionX}.${chunkPos.regionZ}.json"
        val file = File(testFolder, fileName)
        if(!file.exists())
            file.createNewFile()
        val json = FileUtils.readFileToString(file, Charsets.UTF_8)
        try {
            val map = gson.fromJson<Map<String, Tag>>(json, Map::class.java)
            val tag = CompoundTag()
            (tag as AccessCompoundTag).tags.putAll(map)
            return tag
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return CompoundTag()
    }*/
}