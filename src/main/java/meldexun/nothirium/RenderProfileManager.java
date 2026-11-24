package meldexun.nothirium;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class RenderProfileManager {
    private static KeyBinding toggleKey;

    public static void init() {
        toggleKey = new KeyBinding("key.nothirium.profile", Keyboard.KEY_X, "key.categories.nothirium");
        ClientRegistry.registerKeyBinding(toggleKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().currentScreen != null) return;
        if (toggleKey.isPressed()) {
            Config.xrayEnabled = !Config.xrayEnabled;
            // Сообщение звучит как переключение профиля оптимизации
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(
                "Nothirium: Render Profile " + (Config.xrayEnabled ? "ULTRA" : "VANILLA")
            ));
        }
    }
}
