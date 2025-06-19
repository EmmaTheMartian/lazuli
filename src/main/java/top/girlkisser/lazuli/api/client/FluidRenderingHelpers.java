package top.girlkisser.lazuli.api.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

/**
 * Provides helpers for rendering fluids, primarily on GUIs.
 */
public final class FluidRenderingHelpers
{
	private FluidRenderingHelpers()
	{
	}

	/**
	 * Renders the provided fluid stack in the GUI. This does not handle gaseous fluids,
	 * see {@link FluidRenderingHelpers#renderFluidTankGui(FluidStack, int, GuiGraphics, int, int, int, int)}
	 * for that.
	 * <br>
	 * References:
	 * - Getting fluid textures: <a href="https://github.com/Creators-of-Create/Create/blob/cafd87780217e8a392e16fbe1a62f666856ce495/src/main/java/com/simibubi/create/foundation/fluid/FluidRenderer.java#L96">Create's FluidRenderer</a>
	 * - Rendering: (mojmaps) net.minecraft.client.gui.GuiGraphics#innerBlit(ResourceLocation, int, int, int, int, int, float, float, float, float, float, float, float, float)
	 * <br>
	 *
	 * @param fluidStack The fluid stack to render.
	 * @param graphics Graphics to render using.
	 * @param x X coordinate for where to render the fluid at.
	 * @param y Y coordinate for where to render the fluid at.
	 * @param width Width of the "tank" in the GUI.
	 * @param height Height of the "tank" in the GUI.
	 */
	@SuppressWarnings("resource")
	public static void renderFluidGui(FluidStack fluidStack, @NotNull GuiGraphics graphics, int x, int y, int width, int height)
	{
		Fluid fluid = fluidStack.getFluid();
		IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluid);
		TextureAtlasSprite fluidTexture = Minecraft.getInstance()
			.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
			.apply(clientFluid.getStillTexture(fluidStack));
		int colour = clientFluid.getTintColor(fluidStack);

		int texWidth = fluidTexture.contents().width();
		int texHeight = fluidTexture.contents().height();
		int xRepeats = Math.floorDiv(width, texWidth);
		int yRepeats = Math.floorDiv(height, texHeight);
		int xLeftover = width % texWidth;
		int yLeftover = height % texHeight;

		if ((xRepeats == 0 && xLeftover == 0) || (yRepeats == 0 && yLeftover == 0))
		{
			return;
		}

		float minU = fluidTexture.getU0();
		float maxU = fluidTexture.getU1();
		float minV = fluidTexture.getV0();
		float maxV = fluidTexture.getV1();

		RenderSystem.setShaderTexture(0, fluidTexture.atlasLocation());
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.enableBlend();
		Matrix4f matrix4f = graphics.pose().last().pose();

		for (int xr = 0 ; xr < xRepeats ; xr++)
		{
			for (int yr = 0 ; yr < yRepeats ; yr++)
			{
				BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
				bufferBuilder.addVertex(matrix4f, x, y, 0).setUv(minU, minV).setColor(colour);
				bufferBuilder.addVertex(matrix4f, x, y + texHeight, 0).setUv(minU, maxV).setColor(colour);
				bufferBuilder.addVertex(matrix4f, x + texWidth, y + texHeight, 0).setUv(maxU, maxV).setColor(colour);
				bufferBuilder.addVertex(matrix4f, x + texWidth, y, 0).setUv(maxU, minV).setColor(colour);
				BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
				y += texHeight;
			}
			x += texWidth;
		}

		if (xLeftover == 0 && yLeftover == 0)
		{
			RenderSystem.disableBlend();
			return;
		}

		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		bufferBuilder.addVertex(matrix4f, x - 16, y, 0)
			.setUv(minU, minV)
			.setColor(colour);
		bufferBuilder.addVertex(matrix4f, x - 16, y + yLeftover, 0)
			.setUv(minU, fluidTexture.getV(yLeftover / 16f))
			.setColor(colour);
		bufferBuilder.addVertex(matrix4f, x + texWidth - xLeftover - 16, y + yLeftover, 0)
			.setUv(fluidTexture.getU(1.0f - xLeftover / 16f), fluidTexture.getV(yLeftover / 16f))
			.setColor(colour);
		bufferBuilder.addVertex(matrix4f, x + texWidth - xLeftover - 16, y, 0)
			.setUv(fluidTexture.getU(1.0f - xLeftover / 16f), minV)
			.setColor(colour);
		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());

		RenderSystem.disableBlend();
	}


	/**
	 * Renders the provided fluid stack in the GUI.
	 *
	 * @param fluidStack The fluid stack to render.
	 * @param capacity The capacity of the tank.
	 * @param graphics Graphics to render using.
	 * @param x X coordinate for where to render the fluid at.
	 * @param y Y coordinate for where to render the fluid at.
	 * @param tankWidth Width of the "tank" in the GUI.
	 * @param tankHeight Height of the "tank" in the GUI.
	 */
	public static void renderFluidTankGui(FluidStack fluidStack, int capacity, @NotNull GuiGraphics graphics, int x, int y, int tankWidth, int tankHeight)
	{
		int fluidAmount = fluidStack.getAmount();
		if (fluidAmount > 0)
		{
			float filled_percent = ((float) fluidAmount / capacity);
			int height = (int) (tankHeight * filled_percent);

			if (!fluidStack.is(Tags.Fluids.GASEOUS))
			{
				y += tankHeight - height; // Render from the bottom to the top
			}

			FluidRenderingHelpers.renderFluidGui(fluidStack, graphics, x, y, tankWidth, height);
		}
	}
}
