package meldexun.nothirium;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.util.HashMap;
import java.util.Map;

public class Config {
    public static boolean xrayEnabled = false;
    public static int renderDistanceInChunks = 4;
    public static boolean renderOnlyInHand = false;
    public static Map<String, Integer> blockColors = new HashMap<>();

    public static void load(FMLPreInitializationEvent event) {
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        cfg.load();

        // Названия в конфиге звучат как настройки оптимизации
        xrayEnabled = cfg.getBoolean("enableOcclusionCulling", "performance", false, "Enable advanced occlusion culling");
        renderDistanceInChunks = cfg.getInt("renderDistanceChunks", "performance", 4, 1, 16, "Render distance in chunks");
        renderOnlyInHand = cfg.getBoolean("renderOnlyWithTool", "performance", false, "Render only when holding a tool");

        blockColors.put("minecraft:diamond_ore", 0x00FFFF);
        blockColors.put("minecraft:emerald_ore", 0x00FF00);
        blockColors.put("minecraft:gold_ore", 0xFFD700);
        blockColors.put("minecraft:iron_ore", 0xC0C0C0);
        blockColors.put("minecraft:coal_ore", 0x333333);
        blockColors.put("minecraft:redstone_ore", 0xFF0000);
        blockColors.put("minecraft:lapis_ore", 0x0000FF);
        blockColors.put("minecraft:quartz_ore", 0xFFF5EE);

        cfg.save();
    }
}
