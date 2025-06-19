package top.girlkisser.lazuli.api.client.particle2d;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * Provides methods for rendering particles in a 2D space.
 */
public interface IParticle2D
{
	/**
	 * The texture to render the particle with.
	 *
	 * @return The texture.
	 */
	ResourceLocation texture();

	/**
	 * Gets the position to render the particle at.
	 *
	 * @return The position.
	 */
	Vector2f position();

	/**
	 * Gets the size to render the particle at.
	 *
	 * @return The size.
	 */
	Vector2i size();

	/**
	 * Gets the total lifetime of this particle in ticks.
	 *
	 * @return The lifetime.
	 */
	int lifetime();

	/**
	 * Renders this particle using the provided {@link GuiGraphics}.
	 *
	 * @param instance    The {@link InstancedParticle2D} to render.
	 * @param graphics    The graphics to draw using.
	 * @param partialTick The game's partial tick.
	 */
	default void render(InstancedParticle2D instance, GuiGraphics graphics, float partialTick)
	{
		graphics.blitSprite(
			texture(),
			(int) position().x,
			(int) position().y,
			size().x,
			size().y
		);
		instance.lifetimeLeft--;
	}
}
