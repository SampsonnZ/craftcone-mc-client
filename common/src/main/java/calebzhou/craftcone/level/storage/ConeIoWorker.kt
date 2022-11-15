package calebzhou.craftcone.level.storage

import net.minecraft.nbt.StreamTagVisitor
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.chunk.storage.IOWorker
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

/**
 * Created  on 2022-11-11,9:26.
 */
class ConeIoWorker(folder: Path, sync: Boolean, description: String) : IOWorker(folder, sync, description) {

}