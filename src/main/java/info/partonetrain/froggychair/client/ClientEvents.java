package info.partonetrain.froggychair.client;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import info.partonetrain.froggychair.FroggyChair;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = FroggyChair.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderOutline(DrawHighlightEvent.HighlightBlock event)
    {
        event.setCanceled(true);

        BlockRayTraceResult result = event.getTarget();
        BlockPos pos = result.getPos();
        BlockState state = Minecraft.getInstance().world.getBlockState(pos);
        VoxelShape collisionShape = state.getCollisionShape(Minecraft.getInstance().world, pos);
        ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
        double posX = renderInfo.getProjectedView().x;
        double posY = renderInfo.getProjectedView().y;
        double posZ = renderInfo.getProjectedView().z;
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.lineWidth(2.0F);
        RenderSystem.disableTexture();
        RenderSystem.depthMask(false);
        drawVoxelShapeParts(collisionShape, -posX + pos.getX(), -posY + pos.getY(), -posZ + pos.getZ(), 0.0F, 1.0F, 0.0F, 1.0F);
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawVoxelShapeParts(VoxelShape voxelShapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha)
    {
        List<AxisAlignedBB> boxes = voxelShapeIn.toBoundingBoxList();
        for(AxisAlignedBB box : boxes)
        {
            //WorldRenderer.drawShape(VoxelShapes.create(box), xIn, yIn, zIn, red, green, blue, alpha);
        }
    }

    private static void drawVoxelShape(MatrixStack matrixStack, IVertexBuilder builder, VoxelShape shape, double posX, double posY, double posZ, float red, float green, float blue, float alpha)
    {
        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        shape.forEachEdge((x1, y1, z1, x2, y2, z2) ->
        {
            builder.pos(matrix4f, (float) (x1 + posX), (float) (y1 + posY), (float) (z1 + posZ)).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, (float) (x2 + posX), (float) (y2 + posY), (float) (z2 + posZ)).color(red, green, blue, alpha).endVertex();
        });
    }
}
