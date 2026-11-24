package meldexun.nothirium;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;

public class ChunkRenderOptimizer {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void renderOptimizedChunks(EntityPlayerSP player, float partialTicks) {
        if (!Config.xrayEnabled || player == null || mc.world == null) return;
        if (Config.renderOnlyInHand && player.getHeldItemMainhand().getItem() != Items.DIAMOND_PICKAXE) return;

        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        int radius = Config.renderDistanceInChunks;
        ChunkPos playerChunk = new ChunkPos(player.getPosition());

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0F);
        GlStateManager.translate(-px, -py, -pz);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        for (int cx = playerChunk.x - radius; cx <= playerChunk.x + radius; cx++) {
            for (int cz = playerChunk.z - radius; cz <= playerChunk.z + radius; cz++) {
                if (!mc.world.isChunkGeneratedAt(cx, cz)) continue;
                Chunk chunk = mc.world.getChunk(cx, cz);
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 256; y++) {
                            BlockPos pos = new BlockPos(chunk.x * 16 + x, y, chunk.z * 16 + z);
                            IBlockState state = chunk.getBlockState(pos);
                            Block block = state.getBlock();
                            String id = block.getRegistryName().toString();
                            if (!Config.blockColors.containsKey(id)) continue;

                            int color = Config.blockColors.get(id);
                            float r = ((color >> 16) & 0xFF) / 255f;
                            float g = ((color >> 8) & 0xFF) / 255f;
                            float b = (color & 0xFF) / 255f;
                            float a = 0.6f;

                            double x1 = pos.getX(), y1 = pos.getY(), z1 = pos.getZ();
                            double x2 = x1 + 1, y2 = y1 + 1, z2 = z1 + 1;

                            drawLine(buffer, x1, y1, z1, x2, y1, z1, r, g, b, a);
                            drawLine(buffer, x2, y1, z1, x2, y1, z2, r, g, b, a);
                            drawLine(buffer, x2, y1, z2, x1, y1, z2, r, g, b, a);
                            drawLine(buffer, x1, y1, z2, x1, y1, z1, r, g, b, a);

                            drawLine(buffer, x1, y2, z1, x2, y2, z1, r, g, b, a);
                            drawLine(buffer, x2, y2, z1, x2, y2, z2, r, g, b, a);
                            drawLine(buffer, x2, y2, z2, x1, y2, z2, r, g, b, a);
                            drawLine(buffer, x1, y2, z2, x1, y2, z1, r, g, b, a);

                            drawLine(buffer, x1, y1, z1, x1, y2, z1, r, g, b, a);
                            drawLine(buffer, x2, y1, z1, x2, y2, z1, r, g, b, a);
                            drawLine(buffer, x2, y1, z2, x2, y2, z2, r, g, b, a);
                            drawLine(buffer, x1, y1, z2, x1, y2, z2, r, g, b, a);
                        }
                    }
                }
            }
        }

        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static void drawLine(BufferBuilder buffer, double x1, double y1, double z1,
                                 double x2, double y2, double z2, float r, float g, float b, float a) {
        buffer.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        buffer.pos(x2, y2, z2).color(r, g, b, a).endVertex();
    }
}
