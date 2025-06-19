package top.girlkisser.lazuli.api.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A basic {@link FluidType} implementation.
 * <br/>
 * Based on <a href="https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Course-121-Module-5/blob/2c56e563d3411f9b585c1f3208653196a977087f/src/main/java/net/kaupenjoe/mccourse/fluid/BaseFluidType.java">Kaupenjoe's BaseFluidType</a>.
 */
public class BasicFluidType extends FluidType
{
	/** The texture to use for the fluid's still texture. */
	public final ResourceLocation stillTexture;
	/** The texture to use for the fluid's flowing texture. */
	public final ResourceLocation flowingTexture;
	/** The texture to use for the fluid's overlay texture. */
	public final ResourceLocation overlayTexture;
	/**
	 * The fluid type's tint colour as a packed integer.
	 * <br/>
	 * TODO: Convert this to an UnpackedColour
	 */
	public final int tintColour;
	/**
	 * The fluid type's fog colour as a {@link Vector3f}.
	 * <br/>
	 * TODO: Convert this to an UnpackedColour
	 */
	public final Vector3f fogColour;

	/**
	 * A basic {@link FluidType} implementation.
	 *
	 * @param stillTexture The fluid's still texture.
	 * @param flowingTexture The fluid's flowing texture.
	 * @param overlayTexture The fluid's overlay texture.
	 * @param tintColour The tint to apply to the textures.
	 * @param fogColour The fog colour when swimming in the fluid.
	 * @param properties The fluid's properties.
	 */
	public BasicFluidType(ResourceLocation stillTexture, ResourceLocation flowingTexture, ResourceLocation overlayTexture, int tintColour, Vector3f fogColour, Properties properties)
	{
		super(properties);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.overlayTexture = overlayTexture;
		this.tintColour = tintColour;
		this.fogColour = fogColour;
	}

	/**
	 * Get {@link IClientFluidTypeExtensions} for the fluid.
	 *
	 * @param fluidType The fluid type.
	 * @return The client extensions.
	 */
	public static IClientFluidTypeExtensions getClientExtensionsFor(BasicFluidType fluidType)
	{
		return new IClientFluidTypeExtensions()
		{
			@Override
			public ResourceLocation getStillTexture()
			{
				return fluidType.stillTexture;
			}

			@Override
			public ResourceLocation getFlowingTexture()
			{
				return fluidType.flowingTexture;
			}

			@Override
			public ResourceLocation getOverlayTexture()
			{
				return fluidType.overlayTexture;
			}

			@Override
			public int getTintColor()
			{
				return fluidType.tintColour;
			}

			@Override
			public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColour)
			{
				return fluidType.fogColour;
			}

			@Override
			public void modifyFogRender(Camera camera, FogRenderer.FogMode fogMode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape fogShape)
			{
				RenderSystem.setShaderFogStart(1f);
				RenderSystem.setShaderFogEnd(6f); // Distance when the fog starts
			}
		};
	}
}
