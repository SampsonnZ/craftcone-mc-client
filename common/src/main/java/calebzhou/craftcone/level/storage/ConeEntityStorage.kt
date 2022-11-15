package calebzhou.craftcone.level.storage

import calebzhou.craftcone.logger
import com.google.common.collect.ImmutableList
import com.mojang.datafixers.DataFixer
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.longs.LongSet
import net.minecraft.SharedConstants
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntArrayTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.util.thread.ProcessorMailbox
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.FallingBlockEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.chunk.storage.EntityStorage
import net.minecraft.world.level.entity.ChunkEntities
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 * Created  on 2022-11-14,10:26.
 */
class ConeEntityStorage(serverLevel: ServerLevel, path: Path, dataFixer: DataFixer, sync: Boolean, executor: Executor) :
    EntityStorage(serverLevel, path, dataFixer, sync, executor) {
    private val ENTITIES_TAG = "Entities"
    private val POSITION_TAG = "Position"
    private val worker: ConeIoWorker
    private val level: ServerLevel
    private val entityDeserializerQueue: ProcessorMailbox<Runnable>
    private val emptyChunks: LongSet = LongOpenHashSet()
    init {
        worker = ConeIoWorker(path,sync,"cone-entities")
        entityDeserializerQueue = ProcessorMailbox.create(executor, "cone-entity-deserializer");
        this.level = serverLevel;
    }
    //存储实体
    override fun storeEntities(entities: ChunkEntities<Entity>) {
        val chunkPos = entities.pos
        if (entities.isEmpty) {
            if (emptyChunks.add(chunkPos.toLong())) {
                worker.store(chunkPos, null)
            }
            return
        }
        val listTag = ListTag()
        entities.entities.forEach { entity ->
            var needStoreEntityInfo = true
            if (entity is Mob)
                needStoreEntityInfo = entity.hasCustomName()
            //不存储掉落物
            if (entity is ItemEntity)
                needStoreEntityInfo = false
            //不存储掉落方块
            if (entity is FallingBlockEntity)
                needStoreEntityInfo = false
            if (needStoreEntityInfo) {
                val compoundTag = CompoundTag()
                if (entity.save(compoundTag))
                    listTag.add(compoundTag)
            }
        }

        val compoundTag = CompoundTag()
        compoundTag.putInt("DataVersion", SharedConstants.getCurrentVersion().worldVersion)
        compoundTag.put(ENTITIES_TAG, listTag)
        writeChunkPos(compoundTag,chunkPos)
        worker.store(chunkPos, compoundTag).exceptionally {
            logger.error("Failed to store chunk {}", chunkPos, it)
            return@exceptionally null
        }
        ConeRegionFileStorage.write(chunkPos, compoundTag);
        emptyChunks.remove(chunkPos.toLong())
    }

    override fun loadEntities(pos: ChunkPos): CompletableFuture<ChunkEntities<Entity>> {
        return if (this.emptyChunks.contains(pos.toLong()))
              CompletableFuture.completedFuture(emptyChunk(pos))
        else this.worker.loadAsync(pos).thenApplyAsync({ tag ->
                if (tag.isEmpty) {
                    this.emptyChunks.add(pos.toLong())
                    return@thenApplyAsync emptyChunk(pos)
                } else {
                    try {
                        val chunkPos2 = readChunkPos(tag.get())
                        if (pos != chunkPos2) {
                            logger.error("Chunk file at {} is in the wrong location. (Expected {}, got {})", pos, pos, chunkPos2)
                        }
                    } catch (var6: Exception) {
                        logger.warn("Failed to parse chunk {} position info", pos, var6)
                    }
                    val compoundTag = upgradeChunkTag(tag.get())
                    val listTag = compoundTag.getList("Entities", 10)
                    val list: List<Entity?> =
                        EntityType.loadEntitiesRecursive(listTag, level)
                            .collect(ImmutableList.toImmutableList()) as List<Entity>
                    return@thenApplyAsync ChunkEntities<Entity>(pos, list)
                }
            }
        ) { task-> entityDeserializerQueue.tell(task) }
    }

    override fun flush(synchronize: Boolean) {
        this.worker.synchronize(synchronize).join()
        this.entityDeserializerQueue.runAll()
    }

    override fun close() {
        this.worker.close();
    }
    private fun upgradeChunkTag(tag: CompoundTag): CompoundTag {
        val i = getVersion(tag)
        return NbtUtils.update(fixerUpper, DataFixTypes.ENTITY_CHUNK, tag, i)
    }
    private fun readChunkPos(tag:CompoundTag): ChunkPos {
        val posArr = tag.getIntArray(POSITION_TAG)
        return ChunkPos(posArr[0],posArr[1])
    }
    private fun writeChunkPos(tag:CompoundTag,pos:ChunkPos){
        tag.put(POSITION_TAG,IntArrayTag(listOf(pos.x,pos.z)))
    }
    private fun emptyChunk(pos:ChunkPos):ChunkEntities<Entity>{
        return ChunkEntities(pos, listOf())
    }

}