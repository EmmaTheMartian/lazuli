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
	 * @return The texture to render the particle with.
	 */
	ResourceLocation texture();

	/**
	 * @return Where to render the particle at.
	 */
	Vector2f position();

	/**
	 * @return The size to render the particle at.
	 */
	Vector2i size();

	/**
	 * @return The total lifetime of this particle in ticks.
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
