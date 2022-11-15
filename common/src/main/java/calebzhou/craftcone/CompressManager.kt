package calebzhou.craftcone

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtAccounter
import net.minecraft.nbt.NbtIo
import net.minecraft.util.FastBufferedInputStream
import java.io.DataInputStream
import java.io.InputStream
import java.util.zip.GZIPInputStream

/**
 * Created  on 2022-11-12,21:30.
 */
object CompressManager {
    @JvmStatic
    fun readGzipCompressedFile(zippedStream:InputStream): CompoundTag {
        val dataInputStream = DataInputStream(FastBufferedInputStream(GZIPInputStream(zippedStream)))

        val tag: CompoundTag = try {
            NbtIo.read(dataInputStream, NbtAccounter.UNLIMITED)
        } catch (ex: Throwable) {
            try {
                dataInputStream.close()
            } catch (var4: Throwable) {
                ex.addSuppressed(var4)
            }
            throw ex
        }

        if (dataInputStream != null) {
            dataInputStream.close()
        }

        return tag
    }
}