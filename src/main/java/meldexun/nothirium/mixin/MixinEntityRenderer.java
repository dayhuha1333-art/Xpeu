package meldexun.nothirium.mixin;

import meldexun.nothirium.ChunkRenderOptimizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    // Метод renderWorld (SRG: func_175068_a)
    @Inject(method = "renderWorld", at = @At("RETURN"))
    private void onRenderWorld(float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            ChunkRenderOptimizer.renderOptimizedChunks(mc.player, partialTicks);
        }
    }
}
