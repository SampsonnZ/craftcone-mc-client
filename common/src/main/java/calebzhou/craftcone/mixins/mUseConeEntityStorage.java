package calebzhou.craftcone.mixins;

import calebzhou.craftcone.level.storage.ConeEntityStorage;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.storage.EntityStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.file.Path;
import java.util.concurrent.Executor;

/**
 * Created  on 2022-11-14,10:31.
 */
@Mixin(ServerLevel.class)
public class mUseConeEntityStorage {
    @Redirect(method = "<init>",at=@At(value = "NEW",target = "net.minecraft.world.level.chunk.storage.EntityStorage"))
    private EntityStorage sad(ServerLevel serverLevel, Path path, DataFixer dataFixer, boolean bl, Executor executor){
        return new ConeEntityStorage(serverLevel,path,dataFixer,bl,executor);
    }
}