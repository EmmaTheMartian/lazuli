package top.girlkisser.lazuli.api.client.particle2d;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Provides methods to manage particles in a 2D space.
 */
public interface IParticleEngine2D
{
	/**
	 * Add the given particle to the engine.
	 *
	 * @param particle The particle to add.
	 */
	void addParticle(IParticle2D particle);

	/**
	 * Render all particles using the provided {@link GuiGraphics}.
	 *
	 * @param graphics    The graphics to render using.
	 * @param partialTick The game's partial tick.
	 */
	void render(GuiGraphics graphics, float partialTick);

	/**
	 * Clear all particles from the engine.
	 */
	void clear();
}
