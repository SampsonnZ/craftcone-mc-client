package calebzhou.craftcone.mixins;

import calebzhou.craftcone.CompressManager;
import calebzhou.craftcone.level.storage.ConeEntityStorage;
import calebzhou.craftcone.level.storage.ConeRegionFileStorage;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FastBufferedInputStream;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.chunk.storage.EntityStorage;
import net.minecraft.world.level.chunk.storage.IOWorker;
import net.minecraft.world.level.chunk.storage.RegionFileStorage;
import net.minecraft.world.level.entity.ChunkEntities;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.tukaani.xz.XZFormatException;

import java.io.*;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

/**
 * Created  on 2022-11-10,21:56.
 */

//不存储高度图跟亮度了
@Mixin(ChunkSerializer.class)
public abstract class mSaveSpaceReducer {
    //不存储方块亮度
    @Redirect(method = "write",at=@At(ordinal = 0,value = "INVOKE",target = "Lnet/minecraft/nbt/CompoundTag;putByteArray(Ljava/lang/String;[B)V"))
    private static void noBlockLight(CompoundTag instance, String key, byte[] value){

    }
    //不存储天空亮度
    @Redirect(method = "write",at=@At(ordinal =1,value = "INVOKE",target = "Lnet/minecraft/nbt/CompoundTag;putByteArray(Ljava/lang/String;[B)V"))
    private static void noSaveSkyLight(CompoundTag instance, String key, byte[] value){

    }
    //不存储高度图
    @Redirect(method = "write",at=@At(ordinal = 10,value = "INVOKE",target = "Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;"))
    private static Tag noSaveHeightMap(CompoundTag instance, String key, Tag value){
        return null;
    }
    //不存储PostProcessing
    @Redirect(method = "write",at=@At(ordinal = 8,value = "INVOKE",target = "Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;"))
    private static Tag noSavePostProcessing(CompoundTag instance, String key, Tag value){
        return null;
    }

}

@Mixin(EntityStorage.class)
abstract
class mSaveSpaceReducer2{
  /*  @Shadow @Final private LongSet emptyChunks;

    @Shadow @Final private IOWorker worker;
    @Overwrite
    public void storeEntities(ChunkEntities<Entity> entities) {
        ConeEntityStorage.storeEntities(entities,emptyChunks,worker);
    }*/

}

@Mixin(Entity.class)
class mSaveSpaceReducer3{
    //不存储实体的motion信息，节约空间
    @Redirect(method = "saveWithoutId",at=@At(ordinal = 2,value = "INVOKE",target = "Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;"))
    private Tag noStoreMotion(CompoundTag tag, String key, Tag value){
        return null;
    }
}
@Mixin(NbtIo.class)
abstract
class mSaveSpaceReducer4{
   /* @Shadow
    public static void write(CompoundTag compoundTag, DataOutput output) throws IOException {
    }

    //用LZMA2算法对nbt进行压缩 不用GZIP
    @Overwrite
    public static void writeCompressed(CompoundTag compoundTag, OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new XZCompressorOutputStream(outputStream)));
        try {
            write(compoundTag, dataOutputStream);
        } catch (Throwable var6) {
            try {
                dataOutputStream.close();
            } catch (Throwable var5) {
                var6.addSuppressed(var5);
            }

            throw var6;
        }

        dataOutputStream.close();
    }
    @Overwrite
    private static DataInputStream createDecompressorStream(InputStream zippedStream) throws IOException {
        try {
            return new DataInputStream(new BufferedInputStream(new XZCompressorInputStream(zippedStream)));
        } catch (XZFormatException e) {
            try {
                return new DataInputStream(new FastBufferedInputStream(new GZIPInputStream(zippedStream)));
            } catch (ZipException ex) {
                return new DataInputStream(new FastBufferedInputStream(zippedStream));
            }
        }
    }*/

}
@Mixin(LevelStorageSource.LevelStorageAccess.class)
interface AccessLevelStorageAccess{
    @Accessor
    LevelStorageSource.LevelDirectory getLevelDirectory();
}
@Mixin(MinecraftServer.class)
class mSaveSpaceReducer5{


}
@Mixin(RegionFileStorage.class)
class mSaveSpaceReducer6{


}
@Mixin(RegionFileStorage.class)
class mExportRegion2Json {
    @Inject(method = "write",at=@At("HEAD"))
    private void export(ChunkPos chunkPos, CompoundTag chunkData, CallbackInfo ci){
       // ConeRegionFileStorage.write(chunkPos, chunkData);
    }
}
