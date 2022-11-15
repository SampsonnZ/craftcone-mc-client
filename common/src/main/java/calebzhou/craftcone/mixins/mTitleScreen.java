package calebzhou.craftcone.mixins;

import calebzhou.craftcone.gui.CentralServerSelectScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created  on 2022-11-09,11:17.
 */
@Mixin(TitleScreen.class)
public class mTitleScreen {
    @Inject(method = "tick",at=@At("HEAD"))
    private void onTick(CallbackInfo ci){
        long handle = Minecraft.getInstance().getWindow().getWindow();
        if (InputConstants.isKeyDown(handle, InputConstants.KEY_RETURN)
                || InputConstants.isKeyDown(handle, InputConstants.KEY_NUMPADENTER)) {
            Minecraft.getInstance().setScreen(new CentralServerSelectScreen((Screen)(Object)this));
        }
    }
}
